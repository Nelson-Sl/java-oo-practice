package com.twu;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminOperation {
    private static String admin;

    protected static void userInterface(String adminName, Map<String, Integer> hotTopic, Map<Integer, String> sellTopic) {
        admin = adminName;

        //显示用户页面
        System.out.println("你好，" + admin + ", 你可以：");
        System.out.println("1. 查看热搜排行榜");
        System.out.println("2. 添加热搜");
        System.out.println("3. 添加超级热搜");
        System.out.println("4. 退出");

        Scanner adminBehaveCheck = new Scanner(System.in);
        int adminChoice = adminBehaveCheck.nextInt();
        switch(adminChoice){
            case 1:
                checkHotTopic(hotTopic,sellTopic);
            case 2:
                addHotTopic(hotTopic,sellTopic);
            case 3:
                addSuperbTopic(hotTopic, sellTopic);
            case 4:
                HotTopicOperation.UserFace();
            default:
                System.out.println("请根据菜单选择需要的服务");
        }
    }

    //检查管理员登录
    public static String adminCheck() {
        Scanner adminCheck = new Scanner(System.in);
        System.out.println("请输入您的昵称：");
        String adminName = adminCheck.next();
        System.out.println("请输入您的密码：");
        String adminPwd = adminCheck.next();
        if(adminName.equalsIgnoreCase("admin") && adminPwd.equals("admin123")){
            return "true " + adminName;
        }
        return "failed";
    }

    //查看热搜排行榜
    private static void checkHotTopic(Map<String, Integer> hotTopic, Map<Integer, String> sellTopic) {
        int count = 1; //rank 排名
        int topicItem = 0; //List排名
        //排序过的名单
        List<Map.Entry<String,Integer>> sortedTopics = HotTopicSort.sortByValue(hotTopic);
        //先看看对应的排名是否有竞价，如果有就加入购买的热搜，没有就加入排序好的普通热搜
        for(int i = 0; i < hotTopic.size() + sellTopic.size(); i++){
            if(sellTopic.containsKey(count)){
                String[] topicInfo = sellTopic.get(count).split(" ");
                System.out.println(count + " " + topicInfo[0] + " " + topicInfo[1]);
            }else{ //若是超级热搜将标签隐藏再上热搜
                if(sortedTopics.get(topicItem).getKey().contains("superb")){
                    String title = sortedTopics.get(topicItem).getKey().split(" ")[0];
                    System.out.println(count + " " + title + " " + sortedTopics.get(topicItem).getValue());
                }else{
                    System.out.println(count + " " + sortedTopics.get(topicItem).getKey() + " " +
                            sortedTopics.get(topicItem).getValue());
                }
                topicItem++;
            }
            count++;
        }

        userInterface(admin,hotTopic,sellTopic);
    }

    //添加热搜
    private static void addHotTopic(Map<String, Integer> hotTopic, Map<Integer, String> sellTopic) {
        Scanner topicAdder = new Scanner(System.in);
        System.out.println("请输入你要添加的热搜的名字：");
        String newTopic = topicAdder.next();
        if(hotTopic.containsKey(newTopic)){
            System.out.println("添加失败，请重新添加热搜");
        }else{
            hotTopic.put(newTopic,0);
            System.out.println("添加成功");
        }
        userInterface(admin,hotTopic,sellTopic);
    }

    //添加超级热搜
    private static void addSuperbTopic(Map<String, Integer> hotTopic, Map<Integer, String> sellTopic) {
        Scanner superbTopicAdder = new Scanner(System.in);
        System.out.println("请输入你要添加的超级热搜的名字：");
        String newSuperbTopic = superbTopicAdder.next();
        if(hotTopic.containsKey(newSuperbTopic)){
            System.out.println("添加失败，请重新添加热搜");
        }else{
            hotTopic.put(newSuperbTopic + " superb",0);
            System.out.println("添加成功");
        }
        userInterface(admin,hotTopic,sellTopic);
    }
}
