package com.huawei.Service;

import com.huawei.object.Car;
import com.huawei.object.CarStatus;

import java.util.Set;

public interface CRService {
	
	/**
	 * 根据车给出车子将要达到的位置状态
	 * @param carStatus
	 * @param nextRoadId
	 * @return
	 * @throws Exception 
	 */
	public CarStatus getThroughCrossNextStatus(int distance, int nextRoadId, Car car) throws Exception;
	
	/**
	 * 获取经过路口后进入下一个车道可以行使的距离
	 * @param carStatus
	 * @return
	 */
	public int getThroughCrossNextDis(CarStatus carStatus, int vCar, int nextRoadId);
	/**
	 * 车子刚开始驶入的时候
	 * @param car
	 * @return
	 * @throws Exception 
	 */
	public CarStatus getBeginDepStatus(Car car) throws Exception;
	/**
	 * 根据执行车子给出依赖车子状态
	 * @param car
	 * @return null为不能走
	 * @throws Exception 
	 */
	public CarStatus getStraDepStatus(Car car) throws Exception;
	/**
	 * 根据左转车子给出依赖车子状态
	 * @param car
	 * @return null为不能转
	 * @throws Exception 
	 */
	public CarStatus getLeftDepStatus(Car car) throws Exception;
	
	/**
	 * 根据右转车子给出依赖车子状态
	 * @param car
	 * @return null为不能转
	 * @throws Exception 
	 */
	public CarStatus getRightDepStatus(Car car) throws Exception;
	
	/**
	 * 开车，清空当前车位信息，更新T+1时间的车位信息，更新车子的状态信息
	 * @param loc
	 */
	public void driveCar(Car car, CarStatus nexStatus);
	/**
	 * 更新到达路口的道路
	 * @param roadId
	 * @param toCrossId
	 * @throws Exception 
	 */
	public void updateRoadByToCrossId(int roadId, int toCrossId) throws Exception;

	/**
	 * 更新每个道路第一辆车子的方向
	 * @param toCrossId
	 * @param roadId_toCross
	 */
	public void updateCrossRoadProCar(int toCrossId, Set<Integer> roadId_toCross);

	
}
