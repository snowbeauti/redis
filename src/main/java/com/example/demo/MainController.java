package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.test.context.junit4.SpringRunner;

@Controller
public class MainController {
	
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

	@RequestMapping(value={"/test"})
	public String MngMain() {
		
		System.out.println("test");
		
        return "index";

    }
	
	  /** fake some cityInfos */
    private List<CityInfo> cityInfos;

    @Autowired
    private IGeoService geoService;

    public void init() {

        cityInfos = new ArrayList<>();

        cityInfos.add(new CityInfo("hefei", 117.17, 31.52));
        cityInfos.add(new CityInfo("anqing", 117.02, 30.31));
    }

    /**
     * <h2>测试 saveCityInfoToRedis 方法</h2>
     * */
    public void testSaveCityInfoToRedis() {

        System.out.println(geoService.saveCityInfoToRedis(cityInfos));
    }

    /**
     * <h2>测试 getCityPos 方法</h2>
     * 如果传递的 city 在 Redis 中没有记录, 会返回什么呢 ? 例如, 这里传递的 xxx
     * */
    public void testGetCityPos() {

        System.out.println(JSON.toJSONString(geoService.getCityPos(
                Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])
        )));
    }

    /**
     * <h2>测试 getTwoCityDistance 方法</h2>
     * */
    public void testGetTwoCityDistance() {

        System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", null).getValue());
        System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", Metrics.KILOMETERS).getValue());
    }

    /**
     * <h2>测试 getPointRadius 方法</h2>
     * */
    public void testGetPointRadius() {

        Point center = new Point(cityInfos.get(0).getLongitude(), cityInfos.get(0).getLatitude());
        Distance radius = new Distance(200, Metrics.KILOMETERS);
        Circle within = new Circle(center, radius);

        System.out.println(JSON.toJSONString(geoService.getPointRadius(within, null)));

        // order by 距离 limit 2, 同时返回距离中心点的距离
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
        System.out.println(JSON.toJSONString(geoService.getPointRadius(within, args)));
    }

    /**
     * <h2>测试 getMemberRadius 方法</h2>
     * */
    public void testGetMemberRadius() {

        Distance radius = new Distance(200, Metrics.KILOMETERS);

        System.out.println(JSON.toJSONString(geoService.getMemberRadius("suzhou", radius, null)));

        // order by 距离 limit 2, 同时返回距离中心点的距离
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
        System.out.println(JSON.toJSONString(geoService.getMemberRadius("suzhou", radius, args)));
    }

    /**
     * <h2>测试 getCityGeoHash 方法</h2>
     * */
    public void testGetCityGeoHash() {

        System.out.println(JSON.toJSONString(geoService.getCityGeoHash(
                Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])
        )));
    }
}
