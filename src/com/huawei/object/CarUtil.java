package com.huawei.object;

import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;

public class CarUtil implements Comparable<CarUtil> {
	
	private Car car;
	
	public CarUtil(Car car) {
		this.car = car;
	}
	
	public Car getCar() {
		return car;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.car.toString();
	}
	
	@Override
	public int compareTo(CarUtil o) {
		
		if(this.car.getPriority() > o.getCar().getPriority())
			return -1;
		else if(this.car.getPriority() < o.getCar().getPriority())
			return 1;
		
		Location location = car.getCarStatus().getLocation();
		Location locationO = o.getCar().getCarStatus().getLocation();
		
		if(location.getRoadId() != locationO.getRoadId()) {
			CarDirection direction = car.getCarStatus().getDirection();
			CarDirection directionO = o.getCar().getCarStatus().getDirection();
			if(direction.getDirection() < directionO.getDirection()) {
				return -1;
			}else if(direction.getDirection() > directionO.getDirection()) {
				return 1;
			}else
				return 0;
		}
		
		int parking = location.getParking();
		int parkingO = locationO.getParking();
		
		int channel = location.getChannel();
		int channelO = locationO.getChannel();
		
		if(parking < parkingO)
			return 1;
		else if(parking > parkingO)
			return -1;
		
		if(channel < channelO)
			return -1;
		else if(channel == channelO)
			return 0;
		
		return 1;
		
	}

}
