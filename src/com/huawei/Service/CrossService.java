package com.huawei.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.huawei.Status.CarDirection;
import com.huawei.object.Car;
import com.huawei.object.Cross;

public interface CrossService {
	/**
	 * 根据ID获取路口
	 * @param crossId
	 * @return
	 */
	public Cross getCrossById(int crossId);
	/**
	 * 获取所有的路口
	 * @return
	 */
	public Collection<Cross> getAllCross();
	/**
	 * 获取车经过路口的依赖
	 * @param car
	 * @param nextLevel 只对右转有用, 也就是说, 右转要依赖左转, 如果没有左转依赖, 那么右转要依赖直行
	 * @return
	 */
	public int getDepRoadId(Car car, int nextLevel);
	/**
	 * 获取下一个道路的id
	 * @param car
	 * @return
	 */
	public int getNextRoadID(Car car);
	/**
	 * 判断car经过下一个路口的方向
	 * @param car
	 * @return
	 */
	public CarDirection getDirection(int toCrossId, int nowRoadId, int nextRoadId);
	/**
	 * 获取当前路口除当前车道其他的车道id
	 * @param roadId
	 * @return
	 */
	public List<Integer> getOtherRoadId(int roadI, int toCrossId);
	/**
	 * 更新cross的readyCross
	 * @param readyCross
	 */
	public void updateCrossReadyCars(List<Car> readyCars);

	public int getStraNextRoadId(int toCrossId, int roadId_toCross);

}
