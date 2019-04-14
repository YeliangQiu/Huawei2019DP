package com.huawei.utl;

import com.huawei.Factory.DataFactory;
import com.huawei.object.Car;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.simulator.Simulator;

import java.util.*;

/**
 * @author : qiuyeliang
 * create at:  2019/3/10  15:27
 * @description:
 */
public class MapUtils {
    static double [][] weightMatrix;
    static  double [][] carNumMatrix;
    public static int [][] supMatrix;

    public static void init (int len){
        weightMatrix = new double [len][len];
        carNumMatrix = new double[len][len];
        supMatrix = new int[len][len];
    }
    public static int[][]createCrossMap(Map<Integer,Cross> crossMap, Map<Integer, Road>roadMap){
        int maxCrossNumber = 0;
        Simulator.crossMapingMatrix = new HashMap<>();
        int i = 0;
        for(Map.Entry<Integer,Cross> map:crossMap.entrySet()){
            Simulator.crossMapingMatrix.put(map.getKey(),i);
            i++;
        }
        for(Map.Entry<Integer,Integer> map:Simulator.crossMapingMatrix.entrySet()){
            System.out.println("路口号"+map.getKey());
            System.out.println("数组号"+map.getValue());
        }
        System.out.println("初始化成功");
        System.out.println(maxCrossNumber);
        int[][] crossMatrix = new int[Simulator.crossMapingMatrix.size()][Simulator.crossMapingMatrix.size()]; //路口的id没有0
        for(Map.Entry<Integer,Road> map:roadMap.entrySet()){
            Road road = map.getValue();  //路对象
            int start = road.getFrom(); //开始的路口
            int end = road.getTo();  //结束的路口
            int id = road.getId(); //道路的ID
            int duplex = road.getDuplex();//是否双向
            int matrixRow = Simulator.crossMapingMatrix.get(start);
            int matrixColumn = Simulator.crossMapingMatrix.get(end);
            if(duplex == 1) { //如果双向
                crossMatrix[matrixRow][matrixColumn] = id;
                crossMatrix[matrixColumn][matrixRow] = id;
            }else//如果单向
                crossMatrix[matrixRow][matrixColumn] = id;
        }
        return crossMatrix;
    }

    //利用SPFA算法找到最短路径，并且把最短路径存在对象car
    public static void SPFA(int[][] crossMatrix, double[][] weightMatrix, double [][] carNumMatrix, Car car, int startCross,Map<Integer,Cross> crossMap){
        int crossNum = weightMatrix.length;
        double[] dist = new double[crossNum]; //源点到顶点i的最短距离
        int[] path = new int[crossNum]; //恢复路径用
        int start = Simulator.crossMapingMatrix.get(startCross);
        int end = Simulator.crossMapingMatrix.get(car.getTo());
        List<Integer> planWay = new ArrayList<>();
        List<Integer> planCross = new ArrayList<>();
        Queue<Integer> queue = new ArrayDeque<>();
        Stack<Integer> stack = new Stack<>();
        queue.add(start);
        while(!queue.isEmpty()){
            int cross = queue.poll();
            for(Map.Entry<Integer,Cross> map:crossMap.entrySet()){ //遍历所有结点,100多次
                int i = Simulator.crossMapingMatrix.get(map.getKey());
                if(weightMatrix[cross][i]!=0 && (weightMatrix[cross][i] + dist[cross] < dist[i] || dist[i] == 0)){
                    dist[i] = weightMatrix[cross][i] + dist[cross];
                    path[i] = cross;
                    queue.add(i);
                }
            }
        }
        //System.out.println("最短路径(混合权重)"+dist[end]);
        car.setWeight(dist[end]);
        //car.setCompleteTime((int)dist[end]);
        int node =  end; //用于迭代路径
        stack.push(node);
        while(start != node){
            stack.push(path[node]);
            node = path[node];
        }
        while(!stack.empty()){
            planCross.add(stack.pop()); //此时planCross存的是数组的id,不是cross id
        }
        // System.out.println("经过的道路");
        int weight = 0;//拥堵权重
        List<Double> weightList = new ArrayList<>();
        for(int i=1;i<planCross.size();i++){
            int plannode = planCross.get(i-1);
            int nextplannode = planCross.get(i);
            int roadId = crossMatrix[plannode][nextplannode];
            planWay.add(roadId);
            weight += carNumMatrix[plannode][nextplannode];
            if(weightList.size() <= 3)
                weightList.add(carNumMatrix[plannode][nextplannode]);
        }
        car.setPlanWayList(planWay);
        car.setPlanCrossList(planCross);
        car.setWeight(weight);
        car.setWeightList(weightList);
    }

public static void changeCarPlanWay(Car car,int start,int[][] crossMatrix,Map<Integer,Road> roadMap,int nextRoadId,Map<Integer,Cross> crossMap){
    int carSpeed = car.getSpeed();
    for(Map.Entry<Integer,Road> map:roadMap.entrySet()){ //遍历roadMap中的所有道路
        Road road = map.getValue();
        Map<String,String> nowSpeed = road.getNowSpeed();
        for(Map.Entry<String,String> speedMap:nowSpeed.entrySet()){ //遍历这条道路的两个结点
            String str = speedMap.getKey();
            int[] fromTo = strToint(str.split("-"));//分割
            int matrixRow = Simulator.crossMapingMatrix.get(fromTo[0]);
            int matrixColumn = Simulator.crossMapingMatrix.get(fromTo[1]);
            int[] carSpeedAndNum = strToint(speedMap.getValue().split("-"));
            double roadSpeed = carSpeedAndNum[0]; //当前道路最后一辆车的速度
            double roadLenth = roadMap.get(crossMatrix[matrixRow][matrixColumn]).getLength();//路的长度
            int chennl = roadMap.get(crossMatrix[matrixRow][matrixColumn]).getChannel(); //有多少车道
            carNumMatrix[matrixRow][matrixColumn] = carSpeedAndNum[1] / (roadLenth * chennl); //车子数量的占道比率
            if(roadSpeed > carSpeed) //如果路的速度大于车的速度
                weightMatrix[matrixRow][matrixColumn] = roadLenth / carSpeed + ((carSpeedAndNum[1])/(roadLenth * chennl)) * 10;
            else
                weightMatrix[matrixRow][matrixColumn] = roadLenth / roadSpeed + ((carSpeedAndNum[1])/(roadLenth * chennl)) * 10;
        }
    }
    if(nextRoadId != -1) { //初始化的时候不知道下一条路是什么,不走回头路
        Road road = roadMap.get(nextRoadId);
        int matrixRow = Simulator.crossMapingMatrix.get(road.getFrom());
        int matrixColumn = Simulator.crossMapingMatrix.get(road.getTo());
        weightMatrix[matrixRow][matrixColumn] = 0;
        weightMatrix[matrixColumn][matrixRow] = 0;
    }
    SPFA(crossMatrix,weightMatrix,carNumMatrix,car,start, crossMap);
}


    
    public static int[] strToint(String[] str){
        int[] res = new int[str.length];
        for(int i=0;i<res.length;i++)
            res[i] = Integer.parseInt(str[i]);
        return res;
    }

    public static void updateCarWeight(int[][] crossMatrix,Map<Integer,Road> roadMap,Car car){
        if(car.getPlanCrossList() == null)
            return;
        List<Integer> plancrossId = car.getPlanCrossList();
        List<Double> weightList = new ArrayList<>();
        int fromCrossId = car.getFrom();
        for(int i=1;i<plancrossId.size();i++){
            int plannode = plancrossId.get(i-1);
            int nextplannode = plancrossId.get(i);
            int roadId = crossMatrix[plannode][nextplannode];
            int roadlen = roadMap.get(roadId).getLength();
            int chennl = roadMap.get(roadId).getChannel();

            Road road = roadMap.get(roadId);
            int toCrossId = road.getFrom();
            if(fromCrossId == road.getFrom())
                toCrossId = road.getTo();
            String str = road.getNowSpeed().get(fromCrossId+"-"+toCrossId);
            int[] carSpeedAndNum = strToint(str.split("-"));
            if(weightList.size() <= 3)
                weightList.add(carSpeedAndNum[1] * 1.0 / (roadlen + chennl));
            fromCrossId = toCrossId;
        }
        car.setWeightList(weightList);
    }
}
