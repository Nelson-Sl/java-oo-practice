package com.twu;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserOperation {
    private static String userName;
    private static int userVote = 10; //用户初始票数为10票

    protected static void userInterface(String username, Map<String,Integer> hotTopicList, Map<Integer,String>
            sellTopicList){
        userName = username;

        //显示用户页面
        System.out.println("你好，" + userName + ", 你可以：");
        System.out.println("1. 查看热搜排行榜");
        System.out.println("2. 给热搜事件投票");
        System.out.println("3. 购买热搜");
        System.out.println("4. 添加热搜");
        System.out.println("5. 退出");

        Scanner userBehaveCheck = new Scanner(System.in);
        int userChoice = userBehaveCheck.nextInt();
        if(userChoice == 1) {
            listHotTopic(hotTopicList,sellTopicList);
        } else if (userChoice == 2){
            voteForHotTopic(hotTopicList,sellTopicList);
        } else if (userChoice == 3){
            buyTopic(hotTopicList,sellTopicList);
        } else if (userChoice == 4){
            addTopic(hotTopicList,sellTopicList);
        } else{
            HotTopicOperation.UserFace();
        }
    }

    //用户输入用户名
    protected static String getUserName(){
        //询问用户名字
        Scanner userNameCollector = new Scanner(System.in);
        System.out.println("请输入您的昵称: ");
        String userName = userNameCollector.next();
        return userName;
    }

    //查看热搜榜
    private static void listHotTopic(Map<String,Integer> hotTopicList, Map<Integer,String>
            sellTopicList) {
        int count = 1; //rank 排名
        int topicItem = 0; //List排名
        //排序过的名单
        List<Map.Entry<String,Integer>> sortedTopics = HotTopicSort.sortByValue(hotTopicList);
        //先看看对应的排名是否有竞价，如果有就加入购买的热搜，没有就加入排序好的普通热搜
        for(int i = 0; i < hotTopicList.size() + sellTopicList.size(); i++){
            if(sellTopicList.containsKey(count)){
                String[] topicInfo = sellTopicList.get(count).split(" ");
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

        userInterface(userName,hotTopicList,sellTopicList);
    }

    // 给热搜投票
    private static void voteForHotTopic(Map<String,Integer> hotTopicList, Map<Integer,String>
            sellTopicList) {
        // 获取要投票的热搜标题
        Scanner hotTopicViewer = new Scanner(System.in);
        System.out.println("请输入你要投票的热搜名称：");
        String newTopic = hotTopicViewer.next();
        if (!hotTopicList.containsKey(newTopic) && !hotTopicList.containsKey(newTopic + " superb")){
            System.out.println("热搜不存在，投票失败");
        }else{
            // 进入投票页面，用户进行投票
            Scanner voteViewer = new Scanner(System.in);
            System.out.println("请输入你要投票的热搜票数：(你目前还有" + userVote + "票)");
            int vote = voteViewer.nextInt();
            if (vote > userVote){
                System.out.println("投票失败");
            }else{ //热搜一票等于两票，普通搜索一票就等于一票
                if (hotTopicList.containsKey(newTopic + " superb")) {
                    hotTopicList.put(newTopic + " superb", hotTopicList.get(newTopic + " superb") + (vote*2));
                }else{
                    hotTopicList.put(newTopic, hotTopicList.get(newTopic) + vote);
                }
                userVote -= vote;
                System.out.println("投票成功");
            }
        }
        //返回用户界面
        userInterface(userName,hotTopicList,sellTopicList);
    }

    // 购买热搜
    private static void buyTopic(Map<String, Integer> hotTopicList, Map<Integer, String>
            sellTopicList) {
        //获取热搜信息
        Scanner sellTopicInfo = new Scanner(System.in);
        System.out.println("请输入你要购买的热搜名称：");
        String sellTopicName = sellTopicInfo.next();
        System.out.println("请输入你要购买的热搜排名：");
        int sellTopicRank = sellTopicInfo.nextInt();
        System.out.println("请输入你要购买的热搜金额：");
        int sellTopicPrice = sellTopicInfo.nextInt();

        //榜单里面没有就新建榜单并录入信息
        if (sellTopicRank < 1 || sellTopicRank > hotTopicList.size()){
            System.out.println("购买失败");
        }else if(!sellTopicList.containsKey(sellTopicRank)){
            if(hotTopicList.containsKey(sellTopicName)){
                int currentHotNum = hotTopicList.get(sellTopicName);
                String rankTopicInfo =  sellTopicName + " " + currentHotNum + " " + sellTopicPrice;
                sellTopicList.put(sellTopicRank,rankTopicInfo);
                hotTopicList.remove(sellTopicName);
            }else{
                String rankTopicInfo =  sellTopicName + " " + 0 + " " + sellTopicPrice;
                sellTopicList.put(sellTopicRank,rankTopicInfo);
            }
            System.out.println("购买成功！");
        }else{
            // 获取榜单标价并比较，高于当前出价更新热搜，不高于当前出价不变
            String[] currentRankInfo = sellTopicList.get(sellTopicRank).split(" ");
            int currentRankPrice = Integer.parseInt(currentRankInfo[2]);
            if(sellTopicPrice > currentRankPrice && !hotTopicList.containsKey(sellTopicName)){
                String rankTopicInfo =  sellTopicName + " " + 0 + " " + sellTopicPrice;
                sellTopicList.put(sellTopicRank,rankTopicInfo);
                System.out.println("购买成功！");
            }else if(sellTopicPrice > currentRankPrice && hotTopicList.containsKey(sellTopicName)){
                int currentHotNum = hotTopicList.get(sellTopicName);
                String rankTopicInfo =  sellTopicName + " " + currentHotNum + " " + sellTopicPrice;
                sellTopicList.put(sellTopicRank,rankTopicInfo);
                hotTopicList.remove(sellTopicName);
                System.out.println("购买成功！");
            } else{
                System.out.println("购买失败");
            }
        }
        //返回用户界面
        userInterface(userName,hotTopicList,sellTopicList);
    }
    //添加热搜
    private static void addTopic(Map<String,Integer> hotTopicList, Map<Integer,String>
            sellTopicList) {
        Scanner topicViewer = new Scanner(System.in);
        System.out.println("请输入你要添加的热搜的名字：");
        String newTopic = topicViewer.next();
        if(hotTopicList.containsKey(newTopic)){
            System.out.println("添加失败，请重新添加热搜");
        }else{
            hotTopicList.put(newTopic,0);
            System.out.println("添加成功");
        }
        userInterface(userName,hotTopicList,sellTopicList);
    }
}
