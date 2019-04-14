package com.huawei.utl;

import com.huawei.object.Car;
import com.huawei.object.Channel;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.simulator.Simulator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  20:25
 * @description: 格式化
 */
public class FormatUtils {
    public static List<String> load(String path){
        List<String> res = null;
        try{
            res = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            res = res.stream()
                    .filter(v->!v.contains("#")||v.length()<2)  //替换#号
                    .map(v->v.replaceAll("\\(|\\)|","")) //替换括号
                    .map(v->v.replaceAll(" ",""))   //替换空格
                    .collect(Collectors.toList());  //转为List
//            for(String a:res)
//                System.out.println(a);
        }catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }

    public static void createPreSetWay(List<String> lines,Map<Integer,Car> carMap){
        for(String line:lines){
            int[] carInformation = strToint(line.split(","));
            Car car = carMap.get(carInformation[0]);
            car.setPlantime(carInformation[1]);
            List<Integer> list = new ArrayList<>();
            car.setRealtime(carInformation[1]);
            for(int i=2;i<carInformation.length;i++){
                list.add(carInformation[i]);
            }
            car.setPlanWayList(list);
        }
    }
    //将结果写入文件
    private final static String ansHead = "#(carId,startTime,RoadId...)";
    public static void writeAnswer(String path,Map<Integer,Car> cars) {
        try {
            File f = Paths.get(path).toFile();
            if (!f.exists()) f.createNewFile();
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
                bw.write(ansHead+"\n");
                for(Map.Entry<Integer,Car> map:cars.entrySet()){
                    Car car = map.getValue();
                    if(car.getPreset() == 1)
                        continue;
                    List list = car.getRealWayList();
                    String listWay = "";
                    String stringAns = "("+car.getId()+","+car.getRealtime()+",";
                    for(int i=0;i<list.size();i++){
                        if(i != list.size()-1)
                            listWay = listWay + list.get(i) + ",";
                        else
                            listWay = listWay + list.get(i) + ")";
                    }
                    stringAns += listWay;
                    bw.write(stringAns + "\n");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("写入已完成");
    }
//    public static void writeTest(String path, Set<Car> cars) {
//        try {
//            File f = Paths.get(path).toFile();
//            if (!f.exists()) f.createNewFile();
//            try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
//                //bw.write(ansHead+"\n");
//                for(Car car:cars){
////                    List list = car.getRealWayList();
////                    String listWay = "";
////                    String stringAns = "("+car.getId()+","+car.getPlantime()+",";
////                    for(int i=0;i<list.size();i++){
////                        if(i != list.size()-1)
////                            listWay = listWay + list.get(i) + ",";
////                        else
////                            listWay = listWay + list.get(i) + ")";
////                    }
////                    stringAns += listWay;
//                    bw.write(car.toString()+ "\n");
//                }
//                bw.flush();
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//        System.out.println("写入已完成");
//    }
    //构建Car对象
public static Map<Integer,Car> createCar (List<String> lines){
    Map<Integer,Car> cars = new HashMap<>();
    Set<Integer> allCarStartDistri = new HashSet<>();
    Set<Integer> priCarStartDistri = new HashSet<>();
    Set<Integer> allCarEndDistri = new HashSet<>();
    Set<Integer> priCarEndDistri = new HashSet<>();
    int priCar = 0;
    int carMaxSpeed = Integer.MIN_VALUE; //最大车速
    int carMinSpeed = Integer.MAX_VALUE; //最小车速
    int carMaxPlanTime = Integer.MIN_VALUE; //最大出发时间
    int carMinPlanTime = Integer.MAX_VALUE; //最小出发时间
    int pricarMaxSpeed = Integer.MIN_VALUE;
    int pricarMinSpeed = Integer.MAX_VALUE;
    int priCarMaxPlanTime = Integer.MIN_VALUE; //VIP最大出发时间
    int priCarMinPlanTime = Integer.MAX_VALUE; //VIP最小出发时间
    for(String line:lines){
        int[] car = strToint(line.split(","));
        cars.put(car[0],new Car(car[0],car[1],car[2],car[3],car[4],car[5],car[6])); //(id,from,to,speed,plantime,prioritymoreset)
        if(carMaxSpeed < car[3])
            carMaxSpeed = car[3];
        if(carMinSpeed > car[3])
            carMinSpeed = car[3];
        if(car[5] == 1) { //VIP车辆
            priCar++;
            if(priCarMaxPlanTime < car[4])
                priCarMaxPlanTime = car[4];
            if(priCarMinPlanTime > car[4])
                priCarMinPlanTime = car[4];
            if(pricarMaxSpeed < car[3])
                pricarMaxSpeed = car[3];
            if(pricarMinSpeed > car[3])
                pricarMinSpeed = car[3];
            priCarStartDistri.add(car[1]);
            priCarEndDistri.add(car[2]);
        }
        if(carMaxPlanTime < car[4])
            carMaxPlanTime = car[4];
        if(carMinPlanTime > car[4])
            carMinPlanTime = car[4];
        allCarStartDistri.add(car[1]);
        allCarEndDistri.add(car[2]);
    }
    Simulator.a = (cars.size() * 1.0/priCar) * 0.05 + (carMaxSpeed * 1.0/carMinSpeed)/(pricarMaxSpeed * 1.0/pricarMinSpeed) * 0.2375
            + (carMaxPlanTime * 1.0/carMinPlanTime)/(priCarMaxPlanTime * 1.0/priCarMinPlanTime) * 0.2375
            + (allCarStartDistri.size() * 1.0/priCarStartDistri.size()) * 0.2375 + (allCarEndDistri.size()*1.0/ priCarEndDistri.size()) * 0.2375;

    return cars;
}

    //构建Road对象
    public static Map<Integer, Road> createRoad (List<String> lines){
        Map<Integer,Road> roads = new HashMap<>();
        for(String line:lines){
            int[] road = strToint(line.split(","));
            for(int i=0;i<road[6];i++) {
                ArrayList<ArrayList<Car>> chennllist = new ArrayList<>();//channel[0]代表从from到to的通道，channel[1]代表从to到from的道路
                for (int j = 0; j < road[3]; j++) {
                    ArrayList<Car> chennelCarList = new ArrayList<>();
                    chennllist.add(chennelCarList);
                }
            }
            roads.put(road[0],new Road(road[0],road[1],road[2],road[3],road[4],road[5],road[6]));
        }
        return roads;
    }

    //构建Cross对象,这里需要检查这个节点是否能从这条道路出发,以及与这条路相连的crossId
    public static Map<Integer,Cross> createCross(List<String> lines,Map<Integer,Road> roadMap){
        Map<Integer,Cross> crosses = new HashMap<>();
        for(String line:lines){
            List<Integer> roadId_fromCross = new ArrayList<>();
            List<Integer> roadId_toCross = new ArrayList<>();
            int[] roads = new int[4];
            int[] cross = strToint(line.split(","));
            for(int i=1 ;i<5;i++){
                if(cross[i] == -1) {
                    roadId_fromCross.add(-1);
                    roadId_toCross.add(-1);
                    continue;
                }
                if(roadMap.containsKey(cross[i])){
                    Road road = roadMap.get(cross[i]);
                    if(road.getDuplex() == 1) {
                        roads[i - 1] = road.getId();
                        roadId_fromCross.add(road.getId());
                        roadId_toCross.add(road.getId());
                    }
                    else if(road.getFrom() == cross[0]) {
                        roads[i - 1] = road.getId();
                        roadId_fromCross.add(road.getId());
                        roadId_toCross.add(-1);
                    }else{
                        roadId_fromCross.add(-1);
                        roadId_toCross.add(road.getId());
                    }
                }
            }
            crosses.put(cross[0],new Cross(cross[0],roads,roadId_fromCross,roadId_toCross));
        }
        return crosses;
    }

    private static int[] strToint(String[] str){
        int[] res = new int[str.length];
        for(int i=0;i<res.length;i++)
            res[i] = Integer.parseInt(str[i]);
        return res;
    }
}
