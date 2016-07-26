--更新问题的答案选择比例 
local answerStr = redis.call('hget',KEYS[1],ARGV[1])
local answers = cjson.decode(answerStr)
local lable
for key, var in ipairs(answers) do
  if var['id'] == tonumber(ARGV[2]) then
  if var['percentage'] == nil then
  var['percentage'] = 1
  else
    var['percentage'] = var['percentage'] + 1
    lable = var['label']
  end
end
end
answerStr = cjson.encode(answers)
redis.call('hset',KEYS[1],ARGV[1],answerStr)
redis.log(redis.LOG_NOTICE, 'Incre answer`s count success')
--更新用户积分，用户每日回答问题总数
local userAldyQuCount = redis.call('incr',KEYS[3])
redis.log(redis.LOG_NOTICE, 'userAldyQuCount:'..userAldyQuCount)
--local score = getSocre(userAldyQuCount)
local score
if userAldyQuCount >= 0 and userAldyQuCount <= 10 then
  score = 10
elseif userAldyQuCount <= 20 then
  score = 5
else
  score = 1
end
redis.log(redis.LOG_NOTICE, 'score:'..score)
local userScore = redis.call('incrBy',KEYS[2],score)
redis.log(redis.LOG_NOTICE, 'userScore:'..userScore)
--记录用户获取积分的日志
local scoreLog = {}
scoreLog['score'] = score
scoreLog['time'] = ARGV[3]
local scoreLogStr = cjson.encode(scoreLog)
redis.log(redis.LOG_NOTICE, 'scoreLogStr:'..scoreLogStr)
redis.call('rpush',KEYS[4],scoreLogStr)
--更新用户已回答问题ids
redis.call('sadd',KEYS[5],ARGV[4])
redis.log(redis.LOG_NOTICE, KEYS[5]..' add '..ARGV[4])
redis.call('srem',KEYS[6],ARGV[4])
redis.log(redis.LOG_NOTICE, KEYS[4]..' remove '..ARGV[4])
--更新用户标签,标签用户
--local lable = redis.call('hget',KEYS[6],'labels')
if lable ~= nil and lable ~= '' then
	redis.log(redis.LOG_NOTICE, 'labelId:'..lable)
  redis.call('hset', KEYS[7], lable, ARGV[5])
  local labelUserKey = string.format(KEYS[8], lable)
  redis.log(redis.LOG_NOTICE, 'labelUserKey:'..labelUserKey)
  redis.call('hset', labelUserKey, ARGV[6], ARGV[5])
  --用户待回答问题ids添加标签对应的问题ids
  local labelQuKey = string.format(KEYS[10], lable)
  redis.log(redis.LOG_NOTICE, 'labelQuKey:'..labelQuKey)
  redis.call('sunionstore',KEYS[9],labelQuKey)
end
return userScore