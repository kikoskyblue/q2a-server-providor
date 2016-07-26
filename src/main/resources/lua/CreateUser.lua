--新建用户id
local userId = redis.call('incr',KEYS[1])
redis.log(redis.LOG_NOTICE, 'userId:'..userId)
--新建外部系统用户映射关系
redis.call('hset',KEYS[2],ARGV[1],userId)
redis.log(redis.LOG_NOTICE, KEYS[2]..' '..ARGV[1]..' '..'userId:'..userId)
--新建用户信息
local user = cjson.decode(ARGV[3])
user['userid'] = userId
local userKey = string.format(KEYS[3], userId)
redis.log(redis.LOG_NOTICE, 'userKey:'..userKey)
for key, var in pairs(user) do
  redis.call('hset',userKey,key,var)
end
--新建用户标签
local userLabelKey = string.format(KEYS[4],userId)
redis.log(redis.LOG_NOTICE, 'userLabelKey:'..userLabelKey)
redis.call('hset',userLabelKey,ARGV[2],ARGV[4])
--新建标签用户
redis.call('hset',KEYS[5],userId,ARGV[4])
--新建用户待回答问题ids
local userQukey = string.format(KEYS[6],userId)
redis.log(redis.LOG_NOTICE, 'userQukey:'..userQukey)
redis.call('sunionstore',userQukey,KEYS[7])
--新建用户积分
local userScoreKey = string.format(KEYS[8],userId)
redis.log(redis.LOG_NOTICE, 'userScoreKey:'..userScoreKey)
redis.call('set',userScoreKey,'0')
local userCashKey = string.format(KEYS[9],userId)
redis.log(redis.LOG_NOTICE, 'userCashKey:'..userCashKey)
redis.call('set',userCashKey,'0')
return userId