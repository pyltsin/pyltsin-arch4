package com.otus.arch4

import com.google.code.ssm.Cache
import com.google.code.ssm.CacheFactory
import com.google.code.ssm.config.AbstractSSMConfiguration
import com.google.code.ssm.config.DefaultAddressProvider
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl
import com.google.code.ssm.providers.xmemcached.XMemcachedConfiguration
import com.google.code.ssm.spring.SSMCache
import com.google.code.ssm.spring.SSMCacheManager
import net.rubyeye.xmemcached.auth.AuthInfo
import net.rubyeye.xmemcached.utils.AddrUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.net.InetSocketAddress
import java.util.*


@ConditionalOnProperty(name = ["myapp.enableCache"], havingValue = "1", matchIfMissing = false)
@EnableCaching
@Configuration
class MemCachierConfig : AbstractSSMConfiguration() {
    @Autowired
    lateinit var env: Environment

    @Bean
    override fun defaultMemcachedClient(): CacheFactory {
        val serverString = env.getProperty("MEMCACHIER_SERVERS", "localhost:11211").replace(",", " ")
        val servers = AddrUtil.getAddresses(serverString)
        val authInfo = AuthInfo.plain(env.getProperty("MEMCACHIER_USERNAME", "test"),
                env.getProperty("MEMCACHIER_PASSWORD", "test"))
        val authInfoMap: MutableMap<InetSocketAddress, AuthInfo> = HashMap()
        for (server in servers) {
            authInfoMap[server] = authInfo
        }

        val conf = XMemcachedConfiguration()
        conf.isUseBinaryProtocol = true
        conf.authInfoMap = authInfoMap

        val cf = CacheFactory()
        cf.cacheClientFactory = MemcacheClientFactoryImpl()
        cf.addressProvider = DefaultAddressProvider(serverString)
        cf.configuration = conf
        return cf
    }

    @Bean
    fun cacheManager(): CacheManager {
        // Use SSMCacheManager instead of ExtendedSSMCacheManager if you do not
        // need to set per key expiration
        val cacheManager = SSMCacheManager()
        val cache: Cache = defaultMemcachedClient().getObject()!!
        // SSMCache(cache, 0, false) creates a cache with default key expiration
        // of 0 (no expiration) and flushing disabled (allowClear = false)
        cacheManager.caches = listOf(SSMCache(cache, 60, false))
        return cacheManager
    }
}
