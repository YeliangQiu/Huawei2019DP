package com.huawei.DAO;

import com.huawei.Factory.DataFactory;
import com.huawei.object.Car;

import java.util.Map;

public class CarDAO {
	
	private Map<Integer, Car> carMap = DataFactory.carsMap;
	
	public Car getCar(int carId) {
		return carMap.get(carId);
	}

	public int getAllSize() {
		return carMap.values().size();
	}
	
}
