package com.huawei.Service;

import com.huawei.DAO.RoadDAO;
import com.huawei.Factory.DataFactory;
import com.huawei.Factory.ServiceFactory;
import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.Status.RunStatus;
import com.huawei.controller.CRController2;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.CarUtil;
import com.huawei.object.Channel;
import com.huawei.object.Road;
import com.huawei.utl.MapUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoadServiceImp implements RoadService {
	
	RoadDAO roadDao = new RoadDAO();
	CarService carService = null;
	CrossService crossService = null;

	public RoadServiceImp() {
		carService = ServiceFactory.carService;
		crossService = ServiceFactory.crossService;
	}
	
	@Override
	public Road getRoadById(int roadId) {
		return roadDao.getRoad(roadId);
	}
	
	@Override
	public int calculate(int roadId, int vCar) {
		
		int speed = roadDao.getRoad(roadId).getSpeed();
		if(speed > vCar)
			speed = vCar;
		
		return speed;
	}
	
	@Override
	public boolean isOut(int roadId, Location loc, int needLength) {
		Road road = roadDao.getRoad(roadId);
		int parking = loc.getParking();
		int restLength = road.getLength() - 1 - parking;
		if(restLength < needLength)
			return true;
		return false;
	}

	@Override
	public CarStatus getSameChaCarId(Location location, int needLength) throws Exception {
		int roadId = location.getRoadId();
		Location loc = new Location(location);
		List<Channel> channels = roadDao.getChannelByKey(roadId, loc.getFromCrossId(), loc.getToCrossId());
		int length = isOut(roadId, location, needLength) ? roadDao.getRoad(roadId).getLength() - 1 : loc.getParking() + needLength;
		for (int i = loc.getParking() + 1; i <= length; i++) {
			loc.setParking(i);
			CarStatus occupyCarStatus = roadDao.getChannelCarStatus(channels, loc);
			if(occupyCarStatus != null)
				return new CarStatus(occupyCarStatus);
		}
		
		return null;
	}
	
	/**
	 * 和getSameChaCarId差不多, 但是方向不一样, 使用与通过路口找空位信息
	 * @param location parking属性为-1
	 * @param
	 * @return
	 */
//	public CarStatus getSameChanReverCarId(Location location, int needLength) {
//		int roadId = location.getRoadId();
//		Location loc = new Location(location);
//		List<Channel> channels = roadDao.getChannelByKey(roadId, loc.getFromCrossId(), loc.getToCrossId());
//		int length = isOut(roadId, location, needLength) ? roadDao.getRoad(roadId).getLength() - 1 : loc.getParking() + needLength;
//		CarStatus occupyStatus = null;
//		for (int i = length; i >= loc.getParking() + 1; i--) {
//			loc.setParking(i);
//			CarStatus s = roadDao.getChannelCarStatus(channels, loc);
//			if(s == null)
//				return occupyStatus;
//			occupyStatus = s;
//		}
//		
//		return occupyStatus;
//	}

	@Override
	public Car getProChaCaId(Location location, List<Channel> channels) {
		
		return getProChaCaId(channels, location.getChannel(), location.getParking());
	}
	
	private Car getProChaCaId(List<Channel> channels, int channel, int parking) {
		
		int pSize = channels.get(0).getParkings().size();
		int cSize = channels.size();
		for(int i = pSize - 1; i > parking; i--) {
			for(int j = 0; j < cSize; j++) {
				Car car = channels.get(j).getParkings().get(i);
				if(car != null) {
					CarStatus carStatus = car.getCarStatus();
					if(carStatus.getRunStatus() == RunStatus.WAITING)
						return car;
				}
			}
		}
		
		for(int i = 0; i < channel; i++) {
			Car car = channels.get(i).getParkings().get(parking);
			if(car != null) {
				CarStatus carStatus = car.getCarStatus();
				if(carStatus.getRunStatus() == RunStatus.WAITING)
					return car;
			}
		}
		
		for(int i = channel + 1; i < cSize; i++) {
			Car car = channels.get(i).getParkings().get(parking);
			if(car != null && car.getCarStatus().getRunStatus() == RunStatus.WAITING && car.getPriority() == 1)
				return car;
		}
		
		return null;
		
	}

	@Override
	public Car getLastCarIdByCRId(int roadId, int toCrossId) {
		
		Road road = roadDao.getRoad(roadId);
		int fromCrossId = road.getFrom();
		if(road.getFrom() == toCrossId)
			fromCrossId = road.getTo();
		List<Channel> channels = roadDao.getChannelByKey(roadId, fromCrossId, toCrossId);
		
//		Car car = roadDao.getLastCarIdWithChannels(channels, road.getLength());
		Set<CarUtil> list = roadDao.getLastCarIdWithChannels(channels, road.getLength());
		if(list == null)
			return null;
		
		for(CarUtil carutil: list) {
			
			Car car = carutil.getCar();
			CarStatus carStatus = car.getCarStatus();
			boolean isOut = this.isOut(roadId, carStatus.getLocation(), carStatus.getNowSpeed());
			if(isOut)
				return car;
		}
		
		return null;
	}
	
	public Car getCarByChannelIndex(Road road, int toCrossId, int channelIndex) {
		return null;
	}
	
	public int getNumChannlesByRoadId(int roadId) {
		return getRoadById(roadId).getChannel();
	}
	
	@Override
	public CarStatus getCanOccupyStatus(CarStatus carStatus) {
		
		Location loc = carStatus.getLocation();
		CarStatus occupyCarStatus = null;
		//获取在当前车道行驶自己可以占用的空位
		try {
			occupyCarStatus = getSameChaCarId(loc, carStatus.getNowSpeed());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(occupyCarStatus != null)
			occupyCarStatus.getLocation().setParking(occupyCarStatus.getLocation().getParking() - 1);
		else
			return null;
		return new CarStatus(occupyCarStatus);
		
	}
	
	@Override
	public CarStatus getFirstCanOccupyStatus(int distance, CarStatus carStauts) throws Exception {
		
		if(distance == 0)
			return null;
		
		Location loc = carStauts.getLocation();
		Road road = roadDao.getRoad(loc.getRoadId());
		CarStatus occupyStatus = null;
		boolean can = false;
		for(int i = 0; i < road.getChannel(); i++) {
			loc.setChannel(i);
			occupyStatus = getSameChaCarId(loc, distance);
			//获取是否有车位可占
//			if(occupyStatus == null || occupyStatus.getLocation().getParking() != 0) {
			if(occupyStatus == null || occupyStatus.getRunStatus() == RunStatus.WAITING
					|| (occupyStatus.getRunStatus() == RunStatus.FINISHED
							&& occupyStatus.getLocation().getParking() != 0)) {
				can = true;
				break;
			}
		}
		
		//如果下一个车道有车且把车道占满,另外最后一个车位为完成状态
		if(!can)
			return null;
		
		//如果下一个道路上没有车位被占
		if(occupyStatus == null) {
			carStauts.getLocation().setParking(distance - 1);
			return carStauts;
		}
		
		//如果下一个车道有车位被占, 但还可停车, 那么就排在该车后面, 判断是否为完成状态外面会判断
		carStauts.getLocation().setParking(occupyStatus.getLocation().getParking() - 1);
		carStauts.setRunStatus(occupyStatus.getRunStatus());
		
		return carStauts;
		
		
	}
	
	@Override
	public int getToCrossId(int roadId, int fromCrossId) {
		Road road = getRoadById(roadId);
		if(road.getFrom() == fromCrossId)
			return road.getTo();
		
		return road.getFrom();
	}
	
	@Override
	public Car getFLCarIdWithChannel(Channel channel, boolean isFirst) {
		
		if(isFirst)
			return roadDao.getFirstCarIdWithChannel(channel);
		else
			return roadDao.getLastCarIdWithChannel(channel);
	}

	@Override
	public void driveCar(int roadId, Car car){
		
		CarStatus carStatus = car.getCarStatus();
		Location loc = carStatus.getLocation();
		
//		int distance = carStatus.getNowSpeed();
//		loc.setParking(loc.getParking() + distance);
		List<Channel> channels = roadDao.getChannelByKey(roadId, loc.getFromCrossId(), loc.getToCrossId());
		roadDao.updateChanByLoc(channels, car);

	}

	@Override
	public void clearChannelByLoc(Location loc){
		
		List<Channel> channels = roadDao.getChannelByKey(loc.getRoadId(), loc.getFromCrossId(), loc.getToCrossId());
		roadDao.clearChannelByLoc(channels, loc);
		
	}
	
	@Override
	public Collection<Road> getAllRoads() {
		return roadDao.getAllRoads();
	}
	
	private void updateChannelSpeed(List<Channel> channel, Road road, int from, int to) {
		int speed = road.getSpeed();
		int carNumber = 0;
		int i = 0;
		boolean isLastNull = false;
		for(; i < road.getChannel(); i++) {
			if(channel.get(i).getParkings().get(0) == null) {
				isLastNull = true;
				break;
			}
		}
		if(isLastNull) {
			for(int j = 0; j < road.getLength(); j++) {
				Car car = channel.get(i).getParkings().get(j);
				if(car != null && speed > car.getCarStatus().getNowSpeed())
					speed = car.getCarStatus().getNowSpeed();
//				if(car != null) {
//					speed = car.getCarStatus().getNowSpeed();
//					break;
//				}
			}
		}
		else
			speed = 1;
		for(i=0; i < road.getChannel(); i++) {
			for(int j = 0; j < road.getLength(); j++) {
				if(channel.get(i).getParkings().get(j) != null)
					carNumber++;
			}
		}
		road.getNowSpeed().put(from + "-" + to, speed+"-"+carNumber);
	}
	
	@Override
	public void updateRoadNowSpeed() {
		Collection<Road> allRoads = roadDao.getAllRoads();
		for(Road road: allRoads) {
			updateChannelSpeed(road.getFromChannels(), road, road.getFrom(), road.getTo());
			if(road.getIsDuplex() == 1)
				updateChannelSpeed(road.getToChannels(), road, road.getTo(), road.getFrom());
		}
	}
	
	private void updateChannel(List<Channel> channels, int cSize, int pSize) {

		for(int i = 0; i < cSize; i++) {
			for(int j = pSize - 1; j >= 0; j--) {
				Car car = roadDao.getChannelCar(channels.get(i), j);
				if(car == null || car.getCarStatus().getRunStatus() == RunStatus.FINISHED)
					continue;
//				if(car.getId() == 14375) {
//					System.out.println(CRController2.getInstance().getAllTime());
//					System.out.println(car);
//				}
				CarStatus carStatus = car.getCarStatus();
				Location loc = carStatus.getLocation();
				int roadId = loc.getRoadId();
				CarStatus occupyStatus = getCanOccupyStatus(carStatus);
//				boolean isOut = isOut(roadId, loc, carStatus.getNowSpeed());
//				if(isOut) {
//					int nextRoadId = carService.getCarNextRoadId(car, loc.getToCrossId(), roadId);
//					CarDirection carDirection = crossService.getDirection(loc.getToCrossId(), roadId, nextRoadId);
//					car.getCarStatus().setDirection(carDirection);
//				}
				if(occupyStatus != null) {
					RunStatus nextRunStatus = occupyStatus.getRunStatus();
					if(nextRunStatus == RunStatus.WAITING)
						continue;
					occupyStatus.setDirection(carStatus.getDirection());
					occupyStatus.setNowSpeed(carStatus.getNowSpeed());
					CRController2.getInstance().driveCar(car, occupyStatus);
				}else{
					//获取向前走是否出道路
					boolean isOut = isOut(roadId, loc, carStatus.getNowSpeed());
					if(isOut) {
						continue;
					}
					occupyStatus = new CarStatus(carStatus);
					occupyStatus.getLocation().setParking(loc.getParking() + carStatus.getNowSpeed());
					occupyStatus.setRunStatus(RunStatus.FINISHED);
					CRController2.getInstance().driveCar(car, occupyStatus);
				}
			}
		}
		
	}
	
	@Override
	public void updateAllRoad() {
		for(Road road: getAllRoads()) {
			updateChannel(road.getFromChannels(), road.getChannel(), road.getLength());
			if(road.getIsDuplex() == 1) {
				updateChannel(road.getToChannels(), road.getChannel(), road.getLength());
			}
		}
	}
	
	@Override
	public List<Channel> getChannls(int roadId, int fromCrossId, int toCrossId) {
		return roadDao.getChannelByKey(roadId, fromCrossId, toCrossId);
	}
	
	@Override
	public Car getLastCarByChannels(Channel channel, int length) {
		
		for(int i = length - 1; i >= 0; i--) {
			Car car = channel.getParkings().get(i);
			if(car != null)
				return car;
		}
		return null;
	}
	
	@Override
	public void updateChannel(Channel channel) {
		
		List<Car> parkings = channel.getParkings();
		int length = parkings.size();
		for(int i = length - 1; i >= 0; i--) {
			Car car = parkings.get(i);
			if(car == null || car.getCarStatus().getRunStatus() == RunStatus.FINISHED)
				continue;
			CarStatus carStatus = car.getCarStatus();
			CarStatus occupyStatus = getCanOccupyStatus(carStatus);
			if(occupyStatus == null) {
				occupyStatus = new CarStatus(carStatus);
				occupyStatus.setRunStatus(RunStatus.FINISHED);
				occupyStatus.getLocation().setParking(carStatus.getLocation().getParking() + carStatus.getNowSpeed());;
			}else {
				occupyStatus.setDirection(carStatus.getDirection());
				occupyStatus.setNowSpeed(carStatus.getNowSpeed());
			}
			CRController2.getInstance().updateThroughCross(car, occupyStatus);
		}
		
	}

	@Override
	public boolean isJoinCar(int roadId, int toCrossId) {

		Road road = roadDao.getRoad(roadId);
		int fromCrossId = road.getFrom();
		if(toCrossId == road.getFrom()) {
			if(road.getIsDuplex() != 1)
				return false;
			fromCrossId = road.getTo();
		}

		List<Channel> channels = getChannls(roadId, fromCrossId, toCrossId);
		int cSize = road.getChannel(), pSize = road.getLength();
		for(Channel channel: channels) {
			for(int i = 0; i < pSize; i++) {
				Car car = channel.getParkings().get(i);
				if(car != null && car.getCarStatus().getRunStatus() == RunStatus.WAITING)
					return false;
			}
		}

		return true;
	}


}
