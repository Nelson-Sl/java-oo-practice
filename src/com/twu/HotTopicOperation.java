package com.twu;

import java.util.*;

public class HotTopicOperation {
    private static Map<String,Integer> hotTopic = new TreeMap<String,Integer>();
    private static Map<Integer,String> sellTopic = new HashMap<Integer,String>(); //购买热搜的竞价信息

    public static void UserFace(){
        Scanner userTypeCheck = new Scanner(System.in);
        System.out.println("欢迎来到热搜排行榜，你是？");
        System.out.println("1. 用户");
        System.out.println("2. 管理员");
        System.out.println("3. 退出");

        int userChoice = userTypeCheck.nextInt();
        if(userChoice == 1) {
            String username = UserOperation.getUserName();
            UserOperation.userInterface(username,hotTopic,sellTopic);
        } else if (userChoice == 2){
            String checkInfo = AdminOperation.adminCheck();
            if(checkInfo.contains("true")){ //登录成功
                String adminName = checkInfo.split(" ")[1];
                AdminOperation.userInterface(adminName,hotTopic,sellTopic);
            }else{
                System.out.println("登录失败");
                UserFace();
            }
        } else {
            System.exit(1);
        }
    }
}
