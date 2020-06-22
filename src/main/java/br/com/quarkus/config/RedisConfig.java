package br.com.quarkus.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;

import javax.ws.rs.Produces;

import br.com.quarkus.domain.BankingTransaction;

/**
 * Soh pra fazer funcionar
 */
@Dependent
public class RedisConfig {

    private final String redisUrl;

    public  RedisConfig( @ConfigProperty(name = "redis.url") String redisUrl) {
        this.redisUrl = redisUrl;
    }

    @Default @Produces
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.setCodec(org.redisson.codec.JsonJacksonCodec.INSTANCE);
        config.useSingleServer().setAddress(redisUrl);
        return Redisson.create(config);
    }

    @Default @Produces
    public RLiveObjectService rLiveObjectService(RedissonClient redissonClient) {
        final RLiveObjectService liveObjectService = redissonClient.getLiveObjectService();
        liveObjectService.registerClass(BankingTransaction.class);
        return liveObjectService;
    }
}
