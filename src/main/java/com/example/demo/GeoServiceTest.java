package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBoot
public class GeoServiceTest {

	  /** fake some cityInfos */
    private List<CityInfo> cityInfos;

    @Autowired
    private IGeoService geoService;

    @Before
    public void init() {

        cityInfos = new ArrayList<>();

        cityInfos.add(new CityInfo("hefei", 117.17, 31.52));
        cityInfos.add(new CityInfo("anqing", 117.02, 30.31));
    }

    /**
     * <h2>测试 saveCityInfoToRedis 方法</h2>
     * */
    @Test
    public void testSaveCityInfoToRedis() {

        System.out.println(geoService.saveCityInfoToRedis(cityInfos));
    }

    /**
     * <h2>测试 getCityPos 方法</h2>
     * 如果传递的 city 在 Redis 中没有记录, 会返回什么呢 ? 例如, 这里传递的 xxx
     * */
    @Test
    public void testGetCityPos() {

        System.out.println(JSON.toJSONString(geoService.getCityPos(
                Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])
        )));
    }

    /**
     * <h2>测试 getTwoCityDistance 方法</h2>
     * */
    @Test
    public void testGetTwoCityDistance() {

        System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", null).getValue());
        System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", Metrics.KILOMETERS).getValue());
    }

    /**
     * <h2>测试 getPointRadius 方法</h2>
     * */
    @Test
    public void testGetPointRadius() {

        final Point center = new Point(cityInfos.get(0).getLongitude(), cityInfos.get(0).getLatitude());
        final Distance radius = new Distance(200, Metrics.KILOMETERS);
        final Circle within = new Circle(center, radius);

        System.out.println(JSON.toJSONString(geoService.getPointRadius(within, null)));

        // order by 距离 limit 2, 同时返回距离中心点的距离
        final RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
        System.out.println(JSON.toJSONString(geoService.getPointRadius(within, args)));
    }

    /**
     * <h2>测试 getMemberRadius 方法</h2>
     * */
    @Test
    public void testGetMemberRadius() {

        final Distance radius = new Distance(200, Metrics.KILOMETERS);

        System.out.println(JSON.toJSONString(geoService.getMemberRadius("suzhou", radius, null)));

        // order by 距离 limit 2, 同时返回距离中心点的距离
        final RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
        System.out.println(JSON.toJSONString(geoService.getMemberRadius("suzhou", radius, args)));
    }

    /**
     * <h2>测试 getCityGeoHash 方法</h2>
     * */
    @Test
    public void testGetCityGeoHash() {

        System.out.println(JSON.toJSONString(geoService.getCityGeoHash(
                Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])
        )));
    }
}

