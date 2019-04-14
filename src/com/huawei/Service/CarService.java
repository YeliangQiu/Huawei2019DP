package com.huawei.Service;

import com.huawei.object.Car;
import com.huawei.object.CarStatus;

public interface CarService {
	
	public Car getCarById(int carId);
	public int getAllCarSize();
	public void updateCar(Car car, CarStatus carStatus);
	public int getNext2RoadId(Car car, int toCrossId2, int nextRoadId);
	public void updateCarStatusBegin(Car car);
	public int  getCarNextRoadId(Car car, int toCrossId2, int nowRoadId);
	
}
