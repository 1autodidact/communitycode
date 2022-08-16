local function close_redis(red)
    if not red then
        return
    end
    --释放连接(连接池实现)
    local pool_max_idle_time = 10000 --毫秒
    local pool_size = 100 --连接池大小
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then
        ngx.say("set keepalive error : ", err)
    end
end

local function errlog(...)
    ngx.log(ngx.ERR, "redis: ", ...)
end

local redis = require "resty.redis"  --引入redis模块
local red = redis:new()  --创建一个对象，注意是用冒号调用的

--设置超时（毫秒）
red:set_timeout(1000)
--建立连接
local ip = "127.0.0.1"
local port = 6379
local ok, err = red:connect(ip, port)
if not ok then
    close_redis(red)
    errlog("Cannot connect");
    return ngx.exit(ngx.HTTP_INTERNAL_SERVER_ERROR)
end
local auth,err = red:auth("redis123456")
   	 if not auth then
        		ngx.say("failed to authenticate: ", err)
   	 end
local key = "limit:frequency:login:"..ngx.var.uri

--得到此客户端IP的频次
local resp, err = red:get(key)
if not resp then
    close_redis(red)
    return ngx.exit(ngx.HTTP_INTERNAL_SERVER_ERROR) --redis 获取值失败
end

if resp == ngx.null then
    red:set(key, 1) -- 单位时间 第一次访问
    red:expire(key, 10) --10秒时间 过期
end

if type(resp) == "string" then
    if tonumber(resp) > 10 then -- 超过10次
        close_redis(red)
        return ngx.exit(ngx.HTTP_FORBIDDEN) --直接返回403
    end
end

--调用API设置key
ok, err = red:incr(key)
if not ok then
    close_redis(red)
    return ngx.exit(ngx.HTTP_INTERNAL_SERVER_ERROR) --redis 报错
end

close_redis(red)

