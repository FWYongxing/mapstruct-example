package org.example;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AppService {
    public static void main(String[] args) {
        new AppService().queryApp();
    }

    public void queryApp() {
        AppEntity appEntity = new AppEntity();
        appEntity.setInstallCount(Long.MAX_VALUE);
        appEntity.setUninstallCount("880");
        appEntity.setTodayIncome(200);
        appEntity.setYesterdayIncome(100);
        appEntity.setCreateTime(LocalDateTime.now());
        appEntity.setAddress(new AppEntity.Address("中国", "南山"));
        appEntity.setTags(Lists.newArrayList("看书", "美食").stream());

        AppResponse appResponse = AppMapper.INSTANCE.toAppResponse(appEntity);
        System.out.println(JSON.toJSONString(appEntity, true));
        System.out.println(JSON.toJSONString(appResponse, true));

        ArrayList<LocalDateTime> times = Lists.newArrayList(LocalDateTime.now(), LocalDateTime.now());
        List<String> stringTimes = AppMapper.INSTANCE.toStringTimes(times);
        System.out.println(JSON.toJSONString(times, true));
        System.out.println(JSON.toJSONString(stringTimes, true));
    }


    @Mapper
    public interface AppMapper {
        AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);

        @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH点")
        @Mapping(target = "income", expression = "java(appEntity.getTodayIncome() + appEntity.getYesterdayIncome())")
        @Mapping(source = "address.street", target = "street")
        AppResponse toAppResponse(AppEntity appEntity);

        @IterableMapping(dateFormat = "yyyy-MM-dd HH点")
        List<String> toStringTimes(List<LocalDateTime> times);
    }

    @Data
    public static class AppEntity {
        // "微信"
        public final String name = "微信";
        // Long.MAX_VALUE
        private Long installCount;
        // "880"
        private String uninstallCount;
        // 100
        private int yesterdayIncome;
        // 200
        private int todayIncome;
        // 2020-12-30T21:07:41.037
        private LocalDateTime createTime;
        // {"country":"中国", "street":"南山"}
        private Address address;
        // ["看书","美食"]
        private Stream<String> tags;

        @Data
        @AllArgsConstructor
        public static class Address {
            // "中国"
            private String country;
            // "南山"
            private String street;
        }
    }

    @Data
    public static class AppResponse {
        private String name;
        private int installCount;
        private long uninstallCount;
        private int income;
        private String createTime;
        private String country;
        private String street;
        private List<String> tags;
    }

}
