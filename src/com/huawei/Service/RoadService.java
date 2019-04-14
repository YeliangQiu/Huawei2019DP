package com.huawei.Service;

import java.util.Collection;
import java.util.List;

import com.huawei.Status.Location;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.Channel;
import com.huawei.object.Road;

public interface RoadService {
	
	/**
	 * 根据roadId获取road对象
	 * @param roadId
	 * @return
	 */
	public Road getRoadById(int roadId);
	/**
	 * 计算car在道路roadId下的车速
	 * @param roadId
	 * @param
	 * @return
	 */
	public int calculate(int roadId, int vCar);
	/**
	 * car按照当前速度行驶是否超出当前道路
	 * @param roadId
	 * @param
	 * @return true为超出, false为未超出
	 */
	public boolean isOut(int roadId, Location loc, int needLength);
	/**
	 * 获取car在道路roadId下向前行走一个单位时间碰到的车的id
	 * @param
	 * @param
	 * @return -1为无车
	 * @throws Exception 
	 */
	public CarStatus getSameChaCarId(Location location, int needLength) throws Exception;
	/**
	 * 获取与car相同方向比自己车道号小的车道上车的id
	 * @param
	 * @param
	 * @return -1位无车
	 * @throws Exception 
	 */
	public Car getProChaCaId(Location location, List<Channel> channels);
	/**
	 * 获取道路上第一辆要进入路口toCrossId且为等待状态的车id
	 * @param roadId
	 * @param roadId 车子从路口id进入道路
	 * @param toCrossId 车子进入道路终点路口id
	 * @return -1位无车可开
	 * @throws Exception 
	 */
	public Car getLastCarIdByCRId(int roadId, int toCrossId);
	/**
	 * 获取车道上的第一辆车
	 * @param road
	 * @param toCrossId
	 * @param channelIndex
	 * @return
	 */
	public Car getCarByChannelIndex(Road road, int toCrossId, int channelIndex);
	/**
	 * 获取道路上fromCrossId->roadId方向,能放下状态为carStatus的车的位置信息, 只能得到同一个道路上直行的车
	 * @param carStatus
	 * @return
	 */
	public CarStatus getCanOccupyStatus(CarStatus carStatus);
	/**
	 * 获取道路上roadId->toCrossId方向, 能放下状态为carStatus的车的位置信息, 适用于经过路口的车到达下一个道路
	 * @param distance
	 * @param carStatus parking为-1
	 * @return  null为下个道路没车可停
	 * @throws Exception 
	 */
	public CarStatus getFirstCanOccupyStatus(int distance, CarStatus carStaus) throws Exception;
	/**
	 * 获取抵达道路进入的入口id
	 * @param roadId
	 * @param fromCrossId
	 * @return
	 */
	public int getToCrossId(int roadId, int fromCrossId);
	/**
	 * 获取某个车道的第一或最后一辆车
	 * @param channel
	 * @param isFirst true为第一辆车, false为最后一辆车
	 * @return
	 */
	public Car getFLCarIdWithChannel(Channel channel, boolean isFirst);
	/**
	 * car在道路roadId上行驶，也就是更新道路roadId上某个车道的某个具体位置信息
	 * @param roadId
	 * @param car
	 * @throws Exception 
	 */
	public void driveCar(int roadId, Car car);
	
	/**
	 * 清空道路在loc位置的信息
	 * @param
	 * @param loc
	 * @throws Exception 
	 */
	public void clearChannelByLoc(Location loc);
	
	/**
	 * 获取所有道路
	 * @return
	 */
	public Collection<Road> getAllRoads();
	
	/**
	 * 更新所有道路的车子
	 */
	public void updateRoadNowSpeed();
	
	/**
	 * 更新所有道路的车子
	 */
	public void updateAllRoad();
	/**
	 * 获取道路
	 * @param fromCrossId
	 * @param toCrossId
	 * @return
	 */
	public List<Channel> getChannls(int roadId, int fromCrossId, int toCrossId);
	/**
	 * 获取车
	 * @param channel
	 * @return null为无车
	 */
	public Car getLastCarByChannels(Channel channel, int length);
	/**
	 * 更新某个车道，已经确定可以全部更新，
	 */
	public void updateChannel(Channel channel);
	/**
	 * 判断当前的道路上的车是否全为完成状态
	 * @param roadId
	 * @param toCrossId
	 * @return
	 */
	public boolean isJoinCar(int roadId, int toCrossId);
}
