package com.huawei.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  13:10
 * @description: 车辆信息以及车辆状态
 */
public class Car implements Comparable<Car>{
    private int id;
    private int from;
    private int to;
    private int speed;
    private int plantime;
    private int realtime;
    private int priority;
    private int preset;
    private List<Integer> planWayList;
    private List<Integer> planCrossList;
    private List<Integer> realWayList;
    private CarStatus carStatus;
    private List<Double> weightList;
    private int indexWay = -1;
    private double weight = 0;
    private boolean isUpdateWay = true;
    private int isUpdateWayCount = -1;
    public final static int updateCount = 3;
    public int count = 0;
    
    private Car depCar;

    public Car(int id, int from, int to, int speed, int plantime,int priority,int preset) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.plantime = plantime;
        this.realtime = plantime;
        this.priority = priority;
        this.preset = preset;
    }

    public int getPriority() {
        return priority;
    }

    public int getPreset() {
        return preset;
    }

    public List<Double> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<Double> weightList) {
        this.weightList = weightList;
    }

    public void setIndexWay(int indexWay) {
        this.indexWay = indexWay;
    }

    public int getIndexWay() {
        return indexWay;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDepCar(Car depCar) {
		this.depCar = depCar;
	}
    
    public Car getDepCar() {
		return depCar;
	}
    


    public void setPlanCrossList(List<Integer> planCrossList) {
        this.planCrossList = planCrossList;
    }

    public List<Integer> getPlanCrossList() {
        return planCrossList;
    }


    public List<Integer> getPlanWayList() {
        return planWayList;
    }

    public void setPlanWayList(List<Integer> planWayList) {
        this.planWayList = planWayList;
    }

    public List<Integer> getRealWayList() {
    	if(realWayList == null)
    		realWayList = new ArrayList<Integer>();
		return realWayList;
	}

	public void setRealWayList(List<Integer> realWayList) {
		this.realWayList = realWayList;
	}

	public void setId(int id) {
        this.id = id;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPlantime(int plantime) {
        this.plantime = plantime;
    }

    public int getRealtime() {
		return realtime;
	}

	public void setRealtime(int realtime) {
		this.realtime = realtime;
	}

	public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public Integer getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getSpeed() {
        return speed;
    }

    public Integer getPlantime() {
        return plantime;
    }

    public boolean isUpdateWay() {
		return isUpdateWay;
	}

	public void setUpdateWay(boolean isUpdateWay) {
		this.isUpdateWay = isUpdateWay;
	}

    public int getIsUpdateWayCount() {
        return isUpdateWayCount;
    }

    public void setIsUpdateWayCount(int isUpdateWayCount) {
        this.isUpdateWayCount = isUpdateWayCount;
//        if(isUpdateWayCount >= 2) {
        if(isUpdateWayCount >= updateCount - 1) {
            this.isUpdateWayCount = -1;
            this.isUpdateWay = true;
        }
    }

    public CarStatus getCarStatus() {
        if(this.carStatus == null)
            this.carStatus = new CarStatus();
        return carStatus;
    }

//    @Override
//    public String toString() {
//        return "Car [id=" + id + ",carStatus=" + carStatus + ", count=" + isUpdateWayCount + ", from=" + from + ", to=" + to + ", speed=" + speed + ", plantime=" + plantime
//                + ", planWayList=" + planWayList + "]";
//    }
    
//    @Override
//    public String toString() {
//    	// TODO Auto-generated method stub
//    	return this.carStatus.getDirection() + "-" + this.id
//    			+ ", roadId: " + this.carStatus.getLocation().getRoadId()
//    			+ ", channel: " + this.carStatus.getLocation().getChannel()
//    			+ ", parking: " + this.carStatus.getLocation().getParking();
//    }
    
//    @Override
//    public String toString() {
//    	// TODO Auto-generated method stub
//    	return this.carStatus.getDirection() + "-" + this.id + "-" + this.carStatus.getNowSpeed() + "-" + this.getPriority();
//    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "\n" + this.carStatus.getDirection() + "-" + this.id + "-" + this.carStatus.getNowSpeed() + "-" + this.getPriority() + "-" + this.carStatus.getLocation();
    }

    /**
     * 当前值 < 给的值 返回-1,升序
     */
    @Override
    public int compareTo(Car o) {
    	if(this.priority > o.getPriority())
    		return -1;
    	else if(this.priority < o.getPriority())
    		return 1;
    	
//    	if(this.plantime < o.getPlantime())
//    		return -1;
//    	else if(this.plantime > o.getPlantime())
//    		return 1;

//    	if (this.preset > o.getPreset())
//    	    return -1;
//    	else if(this.preset < o.getPreset())
//    	    return 1;
    	
    	if(this.getId() > o.getId())
    		return 1;
    	else if(this.getId() == o.getId())
    		return 0;
    	else
    		return -1;
    }




}
