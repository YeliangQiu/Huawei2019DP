package com.huawei.Service;


import com.huawei.DAO.CarDAO;
import com.huawei.Factory.DataFactory;
import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.Status.RunStatus;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.utl.MapUtils;

import java.util.List;

public class CarServiceImp implements CarService {
	
	CarDAO carDao = new CarDAO();
	
	@Override
	public Car getCarById(int carId) {
		return carDao.getCar(carId);
	}
	
	@Override
	public void updateCar(Car car, CarStatus carStatus) {
		if(car.getCarStatus().getLocation().getRoadId() != carStatus.getLocation().getRoadId()) {
			car.getRealWayList().add(carStatus.getLocation().getRoadId());
//			if(car.getPreset() == 1)
			car.setIndexWay(car.getIndexWay() + 1);
		}
//		if(car.getId() == 19409)
//			System.out.println(car);
		car.setCarStatus(carStatus);
//		int nowRoadId = carStatus.getLocation().getRoadId();
//		int length = car.getRealWayList().size();
//		if(length == 0 || car.getRealWayList().get(length - 1) != nowRoadId)
//			car.getRealWayList().add(nowRoadId);
	}
	
	@Override
	public int getNext2RoadId(Car car, int toCrossId2, int nextRoadId) {

//		List<Integer> planWayList = car.getPlanWayList();
////		int nowIndex = planWayList.indexOf(car.getCarStatus().getLocation().getRoadId());
//		int nowIndex = car.getIndexWay();
//		nowIndex += 2;
//		if (nowIndex >= planWayList.size())
//			return planWayList.get(planWayList.size() - 1);
//
//		return planWayList.get(nowIndex);

		if(toCrossId2 == car.getTo())
			return nextRoadId;

		MapUtils.changeCarPlanWay(car, toCrossId2, DataFactory.crossMatrix,DataFactory.roadsMap, nextRoadId,DataFactory.crossesMap);

		return car.getPlanWayList().get(0);
	}

	@Override
	public int getCarNextRoadId(Car car, int toCrossId, int nowRoadId) {
		if(toCrossId == car.getTo())
			return nowRoadId;
		
		if(car.getPreset() == 1) {
			int nowIndex = car.getIndexWay();
			nowIndex++;
			return car.getPlanWayList().get(nowIndex);
		}
		
		//改过
		if(car.isUpdateWay() && car.getIsUpdateWayCount() == -1)
			MapUtils.changeCarPlanWay(car, toCrossId, DataFactory.crossMatrix,DataFactory.roadsMap, nowRoadId,DataFactory.crossesMap);

//		return car.getPlanWayList().get(0);
		return car.getPlanWayList().get(car.getIsUpdateWayCount() + 1);
	}

	@Override
	public int getAllCarSize() {
		return carDao.getAllSize();
	}

	@Override
	public void updateCarStatusBegin(Car car) {
		car.getCarStatus().setDirection(CarDirection.NONE);
		car.getCarStatus().setRunStatus(RunStatus.WAITING);
		car.getCarStatus().getLocation().setRoadId(-1);
	}


}
