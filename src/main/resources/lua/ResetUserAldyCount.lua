--获取用户当天回答问题数的key集合
local allUserAldyCountKeys = redis.call('keys',KEYS[1])
local reset = {}
local i = 1
for key, var in ipairs(allUserAldyCountKeys) do
  redis.log(redis.LOG_NOTICE, 'key:'..var)
	reset[i] = var
	i = i + 1
	reset[i] = 0
	i = i + 1
	if i >= 100 then
	  redis.log(redis.LOG_NOTICE, '-------')
		redis.call('MSET',unpack(reset))
		i = 1
		reset = {}
	end
end
if table.getn(reset) > 0 then
  redis.log(redis.LOG_NOTICE, '========')
	redis.call('MSET',unpack(reset))
end
return 'SUCCESS'