package com.huawei.Service;

import com.huawei.DAO.CrossDAO;
import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.Cross;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossServiceImp implements CrossService {
	
	CrossDAO crossDao = null;
	
	public CrossServiceImp() {
		crossDao = new CrossDAO();
	}
	
	@Override
	public Cross getCrossById(int crossId) {
		return crossDao.getCrossById(crossId);
	}
	
	@Override
	public Collection<Cross> getAllCross() {
		return crossDao.getAllCross();
	}
	
	@Override
	public int getDepRoadId(Car car, int nextLevel) {
		
		CarStatus carStatus = car.getCarStatus();
		Location loc = carStatus.getLocation();
		CarDirection direction = carStatus.getDirection();
		int toCrossId = loc.getToCrossId();
		int roadId_toCross = loc.getRoadId();
		int depRoadId = -1;
		if(direction == CarDirection.LEFT) {
			depRoadId = crossDao.getLeftDepStra(toCrossId, roadId_toCross);
		}else if(direction == CarDirection.RIGHT) {
			if(nextLevel == 0){
				depRoadId = crossDao.getRightDepLeft(toCrossId, roadId_toCross);
			}else if(nextLevel == 1) {
				depRoadId = crossDao.getRightDepStra(toCrossId, roadId_toCross);
			}
		}
		
		return depRoadId;
	}

	@Override
	public CarDirection getDirection(int toCrossId, int nowRoadId, int nextRoadId) {
		if(nowRoadId == nextRoadId)
			return CarDirection.STRAIGHT;
		return crossDao.getDirection(toCrossId, nowRoadId, nextRoadId);
	}

	@Override
	public int getNextRoadID(Car car) {
		CarStatus carStatus = car.getCarStatus();
		Location loc = carStatus.getLocation();
		
		if(car.getTo() == loc.getToCrossId())
			return loc.getRoadId();
		
		int nextRoadId = 0;
		if(carStatus.getDirection() == CarDirection.STRAIGHT) {
			nextRoadId = crossDao.getStraDepNext(loc.getToCrossId(), loc.getRoadId());
		}else if(carStatus.getDirection() == CarDirection.LEFT) {
			nextRoadId = crossDao.getLeftDepNext(loc.getToCrossId(), loc.getRoadId());
		}else if(carStatus.getDirection() == CarDirection.RIGHT) {
			nextRoadId = crossDao.getRightDepNext(loc.getToCrossId(), loc.getRoadId());
		}else {
			List<Integer> planWayList = car.getPlanWayList();
			int nowRoadId = loc.getRoadId();
			int nextRoadIndex = planWayList.indexOf(nowRoadId) + 1;
			if(nextRoadIndex >= planWayList.size())
				return nowRoadId;
			nextRoadId = planWayList.get(nextRoadIndex);
		}
		
		if(nextRoadId == -1) {
			Cross cross = crossDao.getCrossById(loc.getToCrossId());
			//System.out.println(cross);
		}
		
		return nextRoadId;
	}
	
	@Override
	public List<Integer> getOtherRoadId(int roadI, int toCrossId) {
		
		List<Integer> roadId_toCross = crossDao.getOtherRoadId(toCrossId);
		
		int nowRoadIndex = roadId_toCross.indexOf(roadI);
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
			if(i != nowRoadIndex) {
				Integer roadId = roadId_toCross.get(i);
				if(roadId != -1)
					list.add(roadId);
			}
		}
		
		return list;
	}
	
	@Override
	public void updateCrossReadyCars(List<Car> readyCars) {
		
//		Map<Integer, List<Car>> crossReadyCars = new HashMap<Integer, List<Car>>();
		for(Car car: readyCars)
			if(car.getPriority() == 1) {
				crossDao.getCrossById(car.getFrom()).getReadyCars().add(car);
			}
		
	}

	@Override
	public int getStraNextRoadId(int toCrossId, int roadId_toCross) {
		return crossDao.getStraDepNext(toCrossId,roadId_toCross);
	}

}
