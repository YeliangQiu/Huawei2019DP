package com.huawei.DAO;

import com.huawei.Factory.DataFactory;
import com.huawei.Status.CarDirection;
import com.huawei.object.Cross;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossDAO {
	
	private Map<Integer, Cross> crossMap = DataFactory.crossesMap;
	
	//各项车道标志不是roadId而是索引
	//当前车道直行到经过路口进入下一个车道、当前车道右拐进入路口依赖左拐车道
	private Map<Integer, Integer> straDepNext = new HashMap<Integer, Integer>();
	//当前车道左拐进入路口依赖直行、当前车道右拐经过路口进入下个车道
	private Map<Integer, Integer> leftDepStra = new HashMap<Integer, Integer>();
	//当前车道左拐经过路口进入下一个车道、当前车道右拐进入路口依赖直行车道
	private Map<Integer, Integer> leftDepNext = new HashMap<Integer, Integer>();
	
	public CrossDAO() {
		straDepNext.put(0, 2);
		straDepNext.put(1, 3);
		straDepNext.put(2, 0);
		straDepNext.put(3, 1);

		leftDepStra.put(0, 3);
		leftDepStra.put(1, 0);
		leftDepStra.put(2, 1);
		leftDepStra.put(3, 2);

		leftDepNext.put(0, 1);
		leftDepNext.put(1, 2);
		leftDepNext.put(2, 3);
		leftDepNext.put(3, 0);
	}
	
	public Cross getCrossById(int crossId) {
		return crossMap.get(crossId);
	}
	
	public Collection<Cross> getAllCross() {
		return crossMap.values();
	}
	
	//获取方向
	public CarDirection getDirection(int toCrossId, int nowRoadId, int nextRoadId) {
		Cross cross = crossMap.get(toCrossId);
		
		int nowRoadIndex = cross.getRoadId_toCross().indexOf(nowRoadId);
		int nextRoadIndex = cross.getRoadId_fromCross().indexOf(nextRoadId);

		if (straDepNext.get(nowRoadIndex) == nextRoadIndex) {
			return CarDirection.STRAIGHT;
		} else if (leftDepNext.get(nowRoadIndex) == nextRoadIndex) {
			return CarDirection.LEFT;
		} else
			return CarDirection.RIGHT;
	}
	
	//当前道路经过路口直接进入下一个车道
	public int getDepNextRoadId(int toCrossId, int roadId_toCross, Map<Integer, Integer> dep){
		Cross cross = getCrossById(toCrossId);
		
		int nowIndex = cross.getRoadId_toCross().indexOf(roadId_toCross);
		int nextIndex = 0;
		try{
			nextIndex = dep.get(nowIndex);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
		
		int roadId = cross.getRoadId_fromCross().get(nextIndex);
		
//		if(roadId == -1)
//			throw new Exception("road" + roadId_toCross + " can not through the cross" + toCrossId);
		
		return roadId;
	}
	
	//当前车道经过路口依赖优先级更高的车道
	public int getDepOtherRoadId(int toCrossId, int roadId_toCross, Map<Integer, Integer> dep){
		Cross cross = getCrossById(toCrossId);
		
		int nowIndex = cross.getRoadId_toCross().indexOf(roadId_toCross);
		int nextIndex = 0;
		try {
			nextIndex = dep.get(nowIndex);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
		
		int roadId = cross.getRoadId_toCross().get(nextIndex);
		
//		if(roadId == -1) {
//			throw new Exception("road" + roadId + " do not have dependence road");
//		}
		
		return roadId;
	}
	
	//获取直行通过路口的下一个道路
	public int getStraDepNext(int toCrossId, int roadId_toCross){
		return getDepNextRoadId(toCrossId, roadId_toCross, straDepNext);
	}
	
	//获取左转通过路口的直行依赖
	public int getLeftDepStra(int toCrossId, int roadId_toCross){
		return getDepOtherRoadId(toCrossId, roadId_toCross, leftDepStra);
	}
	
	//获取左转通过路口的下一个道路
	public int getLeftDepNext(int toCrossId, int roadId_toCross){
		return getDepNextRoadId(toCrossId, roadId_toCross, leftDepNext);
	}
	
	//获取右转通过路口的左转依赖
	public int getRightDepLeft(int toCrossId, int roadId_toCross){
		return getDepOtherRoadId(toCrossId, roadId_toCross, straDepNext);
	}
	
	//获取右转通过路口的直行依赖
	public int getRightDepStra(int toCrossId, int roadId_toCross) {
		return getDepOtherRoadId(toCrossId, roadId_toCross, leftDepNext);
	}
	
	//获取右转通过路口的下一个道路
	public int getRightDepNext(int toCrossId, int roadId_toCross) {
		return getDepNextRoadId(toCrossId, roadId_toCross, leftDepStra);
	}
	
	public List<Integer> getOtherRoadId(int toCrossId) {
		return getCrossById(toCrossId).getRoadId_toCross();
	}
	
}
