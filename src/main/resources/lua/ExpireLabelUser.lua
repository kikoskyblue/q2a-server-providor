-- gets all fields from a hash as a dictionary
local hgetall = function (key)
  local bulk = redis.call('HGETALL', key)
  local result = {}
  local nextkey
  for i, v in ipairs(bulk) do
    if i % 2 == 1 then
      nextkey = v
    else
      result[nextkey] = v
    end
  end
  return result
end

-- gets multiple fields from a hash as a dictionary
local hmget = function (key, ...)
  if next(arg) == nil then return {} end
  local bulk = redis.call('HMGET', key, unpack(arg))
  local result = {}
  for i, v in ipairs(bulk) do result[ arg[i] ] = v end
  return result
end
--获取用户标签的key集合
local allUserLabelKeys = redis.call('keys',KEYS[1])
for key, var in ipairs(allUserLabelKeys) do
  redis.log(redis.LOG_NOTICE, 'Traverse user_label_key:'..var)
	local userLabel = hgetall(var)
	--redis.log(redis.LOG_NOTICE, 'userLabel:'..userLabel)
	for key2, var2 in pairs(userLabel) do
	  redis.log(redis.LOG_NOTICE, 'key2:'..key2..';var2:'..var2)
		if tonumber(var2) <= tonumber(ARGV[1]) then
		  --失效过期标签
			redis.call('HDEL',var,key2)
			redis.log(redis.LOG_NOTICE, 'Remove key:'..var..' field:'..key2)
			--更新用户待回答的问题ids
			local s,e = string.find(var, "%d+")
      local userId = string.sub(var,s,e)
      redis.log(redis.LOG_NOTICE, 'userId:'..userId)
      local userQuKey = string.format(KEYS[3],userId)
      redis.log(redis.LOG_NOTICE, 'userQuKey:'..userQuKey)
      local labelQuKey = string.format(KEYS[4],key2)
      redis.log(redis.LOG_NOTICE, 'labelQuKey:'..labelQuKey)
      local quIds = redis.call('SMEMBERS', labelQuKey)
      --redis.log(redis.LOG_NOTICE, 'Remove quIds:'..unpack(quIds))
      redis.call('SREM',userQuKey,unpack(quIds))
		end
	end
end
--获取标签用户的key集合
local allUserLabelKeys = redis.call('keys',KEYS[2])
for key, var in ipairs(allUserLabelKeys) do
  redis.log(redis.LOG_NOTICE, 'Traverse label_user_key:'..var)
  local labelUser = hgetall(var)
  for key2, var2 in pairs(labelUser) do
    if tonumber(var2) <= tonumber(ARGV[1]) then
      --失效过期用户
      redis.call('HDEL',var,key2)
      redis.log(redis.LOG_NOTICE, 'Remove key:'..var..' field:'..key2)
    end
  end
end
return 'SUCCESS'