package com.huawei.Factory;

import com.huawei.object.Car;
import com.huawei.object.Cross;
import com.huawei.object.Road;

import java.util.Map;

public class DataFactory {

	public static Map<Integer, Car> carsMap = null;
	public static Map<Integer, Road> roadsMap = null;
	public static Map<Integer, Cross> crossesMap = null;
	public static int[][] crossMatrix = null;
	public static void loadData(Map<Integer, Car> carMap, Map<Integer, Road> roadMap, Map<Integer, Cross> crossMap,int[][] Matrix) {
		carsMap = carMap;
		roadsMap = roadMap;
		crossesMap = crossMap;
		crossMatrix = Matrix;
	}
}
