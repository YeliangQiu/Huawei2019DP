package com.huawei.simulator;

import com.huawei.controller.CRController2;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.utl.FormatUtils;
import com.huawei.utl.MapUtils;

import java.util.*;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  18:04
 * @description: 进行模拟
 */
public class Simulator {
    private PriorityQueue<Car> readyQueue; //等待上路的队列
    private Map<Integer,Car> cars;
    private Map<Integer,Road> roads;
    private Map<Integer,Cross> crosses;
    private int[][] crossMatrix;
    public static double a = 0;
    public static double b = 0;
    public static int vipCarNumTh = 0;
    public static Map<Integer,Integer> crossMapingMatrix;

    public Simulator(Map<Integer, Car> cars, Map<Integer, Road> roads, Map<Integer, Cross> crosses, int[][] crossMatrix) {
        this.cars = cars;
        this.roads = roads;
        this.crosses = crosses;
        this.crossMatrix = crossMatrix;
    }

//    public Map<Integer,Car> TheFirstStage(){
//        for(int i=1;i<crossMatrix.length;i++){
//            for(int j=1;j<crossMatrix[i].length;j++) {
//                System.out.print(crossMatrix[i][j]+",");
//            }
//            System.out.println();
//        }
//        for(Map.Entry<Integer,Car> map:cars.entrySet()){
//            double[][] weightMatrix = MapUtils.createWeightMatrix(crossMatrix,crosses,roads,map.getValue());
//            System.out.println("此车的id是:"+map.getValue().getId()+"出发时间是"+map.getValue().getPlantime()+"此车的出发地是:"+map.getValue().getFrom()+"此车的目的地是:"+map.getValue().getTo()+"速度是"+map.getValue().getSpeed());
//            MapUtils.SPFA(crossMatrix,weightMatrix,weightMatrix,map.getValue(),map.getValue().getFrom());
//        }
//        return cars;
//        //MapUtils.SPFA(weightMap,start,end);
//    }

    public int[][] getCrossMatrix() {
        return crossMatrix;
    }

    //按id降序排列
    public static Comparator<Car> readyComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            if(c1.getPlantime() > c2.getPlantime())
                return 1;
            else if(c1.getPlantime() < c2.getPlantime())
                return -1;
            if(c1.getId() > c2.getId())
                return 1;
            else
                return -1;
        }
    };
    public static Comparator<Car> carSpeedComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            if(c1.getSpeed() > c2.getSpeed())
                return -1;
            else if(c1.getSpeed() < c2.getSpeed())
                return 1;
            if(c1.getId() > c2.getId())
                return 1;
            else
                return -1;
        }
    };

    //一辆车一辆车走
//    public void oneByOne(double rate){
//        readyQueue = new PriorityQueue<>(readyComparator);
//        for(Map.Entry<Integer,Car> map:cars.entrySet()){
//            readyQueue.add(map.getValue());
//        }
//        int plusTime = readyQueue.peek().getCompleteTime() + readyQueue.peek().getPlantime();
//        readyQueue.poll();
//        while(!readyQueue.isEmpty()){
//            Car car = readyQueue.poll();
//            //System.out.println("车的ID是"+car.getId()+"出发时间是"+car.getPlantime()+"最短路径时间是"+car.getCompleteTime());
//            //System.out.println(plusTime);
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate * car.getCompleteTime()) + car.getPlantime();
//        }
//    }

//    public void carSpeedClassification(double rate1,double rate2){
//        readyQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> fastQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> slowQueue = new PriorityQueue<>(readyComparator);
//        for(Map.Entry<Integer,Car> map: cars.entrySet())
//            readyQueue.add(map.getValue()); //按照出发时间和id排序
//        while(!readyQueue.isEmpty()){
//            Car car = readyQueue.poll();
//            Car nextCar = readyQueue.peek();
//            int startRoadId = car.getPlanWayList().get(0);
//            int roadSpeed  = roads.get(startRoadId).getSpeed();
//            int carSpeed = car.getSpeed();
//            if(nextCar == null){
//                if(carSpeed > roadSpeed)
//                    fastQueue.add(car);
//                else
//                    slowQueue.add(car);
//                break;
//            }
//            System.out.println("newxtCar="+nextCar.getPlantime());
//            System.out.println("Car="+car.getPlantime());
//            if(carSpeed < roadSpeed && nextCar.getPlantime() - car.getPlantime() < 3)
//                slowQueue.add(car);
//            else
//                fastQueue.add(car);
//        }
//        int plusTime = fastQueue.peek().getCompleteTime() + fastQueue.peek().getPlantime();
//        fastQueue.poll();
//        while(!fastQueue.isEmpty()){
//            Car car = fastQueue.poll();
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate1 * car.getCompleteTime() + car.getPlantime());
//        }
//        while (!slowQueue.isEmpty()){
//            Car car = slowQueue.poll();
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate2 * car.getCompleteTime() + car.getPlantime());
//        }
//    }

    public  List<Car> mapToList(){
        List<Car> list = new ArrayList<>();
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            list.add(map.getValue());
        }
        return list;
    }

    public void update() {
        readyQueue = new PriorityQueue<>(readyComparator); //先拿优先队列排序，出发时间早的在前面，相同出发时间的要看id大小
        Map<Integer,List<Car>> mapList = new HashMap<>();
        Map<Integer,List<Car>> presetCarMap = new HashMap<>();
        int vipCar = 0;
        int preCar = 0;
        int commonCar = 0;
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            if(map.getValue().getPreset() != 1 && map.getValue().getPriority() != 1) { //普通车
                readyQueue.add(map.getValue());
                commonCar ++;
            }
            else if(map.getValue().getPriority() == 1 && map.getValue().getPreset() != 1){ //VIP非预置
                Car car = map.getValue();
                vipCar ++;
                if(mapList.containsKey(car.getPlantime())){
                    mapList.get(car.getPlantime()).add(car);
                }else{
                    List<Car> list = new ArrayList<>();
                    list.add(car);
                    mapList.put(car.getPlantime(),list);
                }
            }else{//预置车
                Car car = map.getValue();
                preCar ++;
                if(presetCarMap.containsKey(car.getPlantime())){
                    presetCarMap.get(car.getPlantime()).add(car);
                }else{
                    List<Car> list = new ArrayList<>();
                    list.add(car);
                    presetCarMap.put(car.getPlantime(),list);
                }
            }
        }
//        Map<Integer,List<Car>> mapList = carSpeedClassification();
        Map<Integer,List<Car>> SerializationCar = new HashMap<>(); //将car按出发时间序列化
        while(!readyQueue.isEmpty()){ //处理普通车
            Car car = readyQueue.poll();
            if(SerializationCar.containsKey(car.getPlantime())){
                List<Car> list = SerializationCar.get(car.getPlantime());
                list.add(car);
            }else{
                List<Car> carList = new ArrayList<>();
                carList.add(car);
                SerializationCar.put(car.getPlantime(),carList);
            }
        }
        CRController2 controller = CRController2.getInstance();
        List<Car> initcarList = null; //初始化车
        if(SerializationCar.containsKey(1)) { //普通车出发时间为1的车
            initcarList = SerializationCar.get(1);
            SerializationCar.remove(1);
        }else
            initcarList = new ArrayList<>();
        if(mapList.containsKey(1)) { //VIP出发时间为1的车
            initcarList.addAll(mapList.get(1));
            mapList.remove(1);
        }
        if(presetCarMap.containsKey(1)){//预置车出发时间为1的车
            initcarList.addAll(presetCarMap.get(1));
            presetCarMap.remove(1);
        }
        controller.init(initcarList); //普通+VIP+预置
        int count = 1;
        int maxMapCarSize = 0;
        int tim = 0;
        int CRCcarnum = controller.maxCarNum;
        while(!controller.finished()) {
            System.out.println("SerializationCar="+SerializationCar.size());
            System.out.println("mapList="+mapList.size());
            System.out.println("preCarList="+presetCarMap.size());
            System.out.println("道路上的VIP车辆="+controller.getMapVipSize());
            System.out.println("当前时间片="+controller.getAllTime());
            if(maxMapCarSize < controller.getMapCarSize())
                maxMapCarSize = controller.getMapCarSize();
//            long now = System.currentTimeMillis();
            try {
                controller.update();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("count=" + count + "Mapsize" + controller.getMapCarSize());
                break;
            }
            List<Car> joinCars = null;
            if(controller.getMapCarSize() <= CRCcarnum && controller.getAcceptCarSize() <= CRCcarnum * 2) { //如果本时刻路上的车小于*;
                joinCars = SerializationCar.get(controller.getAllTime()); //这一时刻的值
                SerializationCar.remove(controller.getAllTime()); // 移出
                tim = 0;
            }else{
                tim++;
                Map<Integer,List<Car>> tmpMap = new HashMap<>();
                for(Map.Entry<Integer,List<Car>> map:SerializationCar.entrySet()){ //还未上路的所有车的出发时间都要+1
                    List <Car> carList = map.getValue();
                    for(Car car:carList){
                    	car.setRealtime(car.getRealtime() + 1);
                    }
                    tmpMap.put(map.getKey()+1,carList);
                }
                SerializationCar = tmpMap;
            }
            if(mapList.containsKey(controller.getAllTime())) {
                if (controller.getMapVipSize() > vipCarNumTh) { //如果路上的VIP车大于500，剩下的VIP车延迟出发
                    Map<Integer, List<Car>> tmpMap = new HashMap<>();
                    for (Map.Entry<Integer, List<Car>> map : mapList.entrySet()) {
                        List<Car> carList = map.getValue();
                        for (Car car : carList) {
                            car.setRealtime(car.getRealtime() + 1);
                        }
                        tmpMap.put(map.getKey() + 1, carList);
                    }
                    mapList = tmpMap;
                } else {
                    if (joinCars == null || joinCars.size() == 0)
                        joinCars = mapList.get(controller.getAllTime());
                    else
                        joinCars.addAll(mapList.get(controller.getAllTime()));
                    mapList.remove(controller.getAllTime());
                }
            }
            if(presetCarMap.containsKey(controller.getAllTime())){
                if(joinCars == null || joinCars.size() == 0)
                    joinCars = presetCarMap.get(controller.getAllTime());
                else{
                    joinCars.addAll(presetCarMap.get(controller.getAllTime()));
                }
                presetCarMap.remove(controller.getAllTime());
            }
            if(joinCars != null && joinCars.size() != 0) {
                controller.addMapListCar(joinCars);
            }
            System.out.println(SerializationCar.size());
//    		controller.addMapCar(null); //加入要跑图的车子序列
        }
        System.out.println("限制车辆数量"+controller.maxCarNum);
        System.out.println("限制车辆权重"+controller.weight);
        System.out.println("预置车辆的Size="+presetCarMap);
        System.out.println("maxMapCarSize=" + maxMapCarSize);
        System.out.println("maxWeight"+controller.maxWeight);
        System.out.println("time" + controller.getAllTime());
        System.out.println("优先级车辆的最后到达时间" + controller.priorityTim);
        System.out.println("系统调度时间" + (controller.getAllTime()+controller.priorityTim * a) );
    }
//
    public void randStartTime(int repeat){
        Map<Integer,Integer> startTimeCount = new HashMap<>();
        Set<Integer> repeatSet = new HashSet<>();
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            Car car = map.getValue();
            if(startTimeCount.containsKey(car.getPlantime()))
                startTimeCount.put(car.getPlantime(),startTimeCount.get(car.getPlantime())+1);
            else
                startTimeCount.put(car.getPlantime(),1);
        }
        for(Map.Entry<Integer,Integer> map:startTimeCount.entrySet()){
            if(map.getValue() >= repeat)
                repeatSet.add(map.getKey());
        }
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            Car car = map.getValue();
            if(repeatSet.contains(car.getPlantime())) {
                car.setPlantime(car.getPlantime() + (int) (Math.random() * (50 - 1 + 1)));
            }
        }
    }

    public Map<Integer,List<Car>>  carSpeedClassification(){
        readyQueue = new PriorityQueue<>(readyComparator);
        Map<Integer,List<Car>> mapList = new HashMap<>();
        Map<Integer,PriorityQueue<Car>> carMap = new HashMap<>();
        Queue<Car> carSpeedQueue = new PriorityQueue<>(carSpeedComparator);//先按速度排一遍
        int maxPlantime = 0;
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            if(map.getValue().getPreset() == 1 || map.getValue().getPriority() == 1){
                Car car = map.getValue();
                if(mapList.containsKey(car.getPlantime())){
                    mapList.get(car.getPlantime()).add(car);
                }else{
                    List<Car> list = new ArrayList<>();
                    list.add(car);
                    mapList.put(car.getPlantime(),list);
                }
                continue;
            }
            if(maxPlantime < map.getValue().getPlantime())
                maxPlantime = map.getValue().getPlantime();
            carSpeedQueue.add(map.getValue());
        }
        for(int i=1;i<=maxPlantime+1;i++) //初始化carMap
            carMap.put(i,new PriorityQueue<Car>(carSpeedComparator));
        int carNum = 0,timeSlice = 1,unitTimePieceCarNum = (int)(cars.size() / maxPlantime);//车的数量，时间片，单位时间片的车数
        List<Car> specialCarlist = new ArrayList<>();
        while (!carSpeedQueue.isEmpty()){
            Car car = carSpeedQueue.poll();
            if(car.getPriority() == 1 || car.getPreset() == 1) {
                specialCarlist.add(car);
                continue;
            }
            if(car.getPlantime() > timeSlice){//如果这个车的出发时间大于当前时间片
                carMap.get(maxPlantime + 1).add(car); //去最后一个优先队列等待排序
            }else if(car.getPlantime() < timeSlice) {//如果小于当前时间片，则延迟出发时间
                car.setPlantime(timeSlice);
                carMap.get(timeSlice).add(car);//进入当前时间片的队列
            }else
                carMap.get(timeSlice).add(car);//进入当前时间片队列
            carNum ++; //遍历到第几台车
            if(carNum > unitTimePieceCarNum){ //大于当前时间片了
                timeSlice ++; //时间片+1
            unitTimePieceCarNum *= timeSlice; //再加上一个当前时间片的值
        }
    }
        PriorityQueue<Car> specialQueue = carMap.get(maxPlantime + 1);
        carMap.remove(maxPlantime + 1);
        for(Map.Entry<Integer,PriorityQueue<Car>> carmap:carMap.entrySet()){
            Queue<Car> carQueue = carmap.getValue();
            if(carQueue.size() == 0)
                continue;
        int thisTimeSliceCarMaxSpeed = carQueue.peek().getSpeed();
        while(!carQueue.isEmpty()){
            Car car = carQueue.poll();
            if(thisTimeSliceCarMaxSpeed == car.getSpeed())
                readyQueue.add(car);
            else
                specialQueue.add(car);
        }
    }
    //PriorityQueue<Car> specialQueue = carMap.get(maxPlantime + 1);
        if(specialQueue != null && specialQueue.size() != 0) {
            int delay = maxPlantime + 1; //延迟多少时间呢
            int speedFlag = specialQueue.peek().getSpeed(); //
            while (!specialQueue.isEmpty()) {
                Car car = specialQueue.poll();
                if (speedFlag == car.getSpeed()){
                    car.setPlantime(delay);
                    readyQueue.add(car);
                } else {
                    delay++;
                    speedFlag = car.getSpeed();
                    car.setPlantime(delay);
                    readyQueue.add(car);
                }
            }
        }
        for(Car car:specialCarlist)
            readyQueue.add(car);
       System.out.println(readyQueue.size());
       return mapList;
    }
}
