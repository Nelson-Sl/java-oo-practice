package com.twu;

import java.util.*;

public class HotTopicSort {
    //普通热搜根据value值排序
    public static List<Map.Entry<String,Integer>> sortByValue(Map<String,Integer> hotTopicMap){
        List<Map.Entry<String,Integer>> hotTopicList = new ArrayList<Map.Entry<String, Integer>>(hotTopicMap.entrySet());
        Collections.sort(hotTopicList, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String,Integer> topic1, Map.Entry<String,Integer> topic2) {
                return topic2.getValue().compareTo(topic1.getValue());
            }
        });
        return hotTopicList;
    }
}
