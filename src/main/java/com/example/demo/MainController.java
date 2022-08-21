package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

@Controller
public class MainController {
	
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    @Autowired
//    Redistemplate redistemplate;
    
	@RequestMapping(value={"/test"})
	public String MngMain() {
		
		System.out.println("test");
		
        return "index";

    }
	
	
   /**
     * 1. redis에 위치 정보 추가
     * 1. 온라인 매장 위치정보-geo
     * 2. 아울렛 정보
     */
	  public void geoadd(){
	        //1. 네트워크 위치 정보 저장 120.653208, 28.032606
	        Point point = new Point(120.653208, 28.032606);
	        redisTemplate.boundGeoOps("outlets").add(point, "가게이름");
	        //2. 네트워크 세부정보
	        Addressinfo addressinfo = new Addressinfo();
	        addressinfo.setTitle("가게이름");
	        addressinfo.setAddress("浙江省温州市鹿城区가게이름景区内");
	        addressinfo.setPhone("(0577)88201281");
	        addressinfo.setX(120.653208);
	        addressinfo.setY(28.032606);
	        redisTemplate.boundHashOps("outletsinfo").put("가게이름", addressinfo);
	    }

	    /**
     * 2. 위치 좌표 정보 획득
	     */
	    public void geopos(){
	        //위치 정보를 조회하기 위해 위치 이름을 전달합니다.
	        List<Point> position = redisTemplate.boundGeoOps("outlets").position("南塘五组团");
	        for (Point point : position) {
	            System.out.println(point);
	        }
	    }
	 
	    /**
     * 3. 두 위치 정보 계산
	     */
	    public void geodist(){
	    	  /**
	        거리 거리 = redistemplate.boundgeoops("outlets").distance("Jiangxinyu", "Wenzhou Paradise");
	        //거리
	        이중 값 = distance.getvalue();
	        //단위
	        문자열 단위 = distance.getunit();
	        system.out.println("두 점 사이의 거리: "+값+단위);
	         */
	        //km 단위로 표시
	        /**
	        거리 거리 = redistemplate.boundgeoops("outlets").distance("Jiangxinyu", "Wenzhou Paradise", metrics.kilometers);
	        //거리
	        이중 값 = distance.getvalue();
	        //단위
	        문자열 단위 = distance.getunit();
	        system.out.println("두 점 사이의 거리:"+값+단위);*/
	 
	        //保留两位小数
	        Distance distance = redisTemplate.boundGeoOps("outlets").distance("가게이름", "温州乐园", Metrics.KILOMETERS);
	        //距离
	        double value = distance.getValue();
	        //单位
	        String unit = distance.getUnit();
	        System.out.println("两点相距:"+new BigDecimal(value).setScale(2,BigDecimal.ROUND_HALF_UP) +unit);
	    }
	 
	    /**
     * 4. 주어진 위도와 경도에 따라 지정된 범위의 위치 찾기
	     */
	    public  void georadius(){
	        //위치 지정
	        Point point = new Point(120.754274,  27.983296);
	        //빌드 조건 -10km
	        Distance distance = new Distance(10, Metrics.KILOMETERS);
	        Circle circle = new Circle(point,distance);
	        // redisgeo 명령
	        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
	         // 위치 정보를 포함
	        args.includeDistance();

	        // 조회된 URL 위치 정보를 저장합니다.
	        GeoResults<RedisGeoCommands.GeoLocation<String>> outlets = redisTemplate.boundGeoOps("outlets").radius(circle, args);
	        for (GeoResult<RedisGeoCommands.GeoLocation<String>> outlet : outlets) {
	            //거리 정보 얻기
	            Distance outletdistance = outlet.getDistance();
	            //거리
	            double value = outletdistance.getValue();
	            //단위
	            String unit = outletdistance.getUnit();
	            System.out.println("当前坐标距离:"+outlet.getContent().getName()+value+unit);
	        }
	    }
	 
	    /**
     * 5. 지정된 요소에 따라 지정된 범위 위치 찾기(컬렉션에 있어야 함)
	     */
	    public void georadiusbymember(){
	        //빌드 조건
	    	Distance distance = new Distance(10, Metrics.KILOMETERS);
	        // 빌드 조건 - 위치 정보 포함
	    	RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
	    	args.includeDistance();
	        //파라미터 키 및 기타 조건으로 구성된 조건
	        GeoResults<RedisGeoCommands.GeoLocation<String>> outlets = redisTemplate.boundGeoOps("outlets").radius("가게이름", distance,args);
	        for (GeoResult<RedisGeoCommands.GeoLocation<String>> outlet : outlets) {
	            //거리 정보 얻기
	            Distance outletdistance = outlet.getDistance();
	            //거리
	            double value = outletdistance.getValue();
	            //단위
	            String unit = outletdistance.getUnit();
	            System.out.println("가게이름어쩌구:"+outlet.getContent().getName()+value+unit);
	        }
	    }
	}

