package com.huawei.object;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  13:12
 * @description: 路口信息，路口可以有四个方向的道路，0，上，1右，2下，3左
 */
public class Cross implements Comparable<Cross> {
    private Integer id;
    private int[] roadId; //指的是从这个路口能出去的roadId,不连则为0
    //从该路口出发的roadId，按照顺时针排序，[上,右,下,左],形式为crossId->roadId_fromCross
    private List<Integer> roadId_fromCross;//*
    //到达该路口的roadId,按照顺时针排序，[上,右,下,左],形式为roadId_toCross->crossId
    private List<Integer> roadId_toCross;//*
    private Set<Car> readyCars;
    public Cross(int id, int[] roadId, List<Integer> roadId_fromCross, List<Integer> roadId_toCross) {
        this.id = id;
        this.roadId = roadId;
        this.roadId_fromCross = roadId_fromCross;
        this.roadId_toCross = roadId_toCross;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoadId(int[] roadId) {
        this.roadId = roadId;
    }

    public int[] getRoads() {
        return roadId;
    }

    public List<Integer> getRoadId_fromCross() {
        return roadId_fromCross;
    }

    public List<Integer> getRoadId_toCross() {
        return roadId_toCross;
    }

	public Set<Car> getReadyCars() {
		if(readyCars == null) {
			readyCars = new TreeSet<Car>();
		}
		return readyCars;
	}

	@Override
	public int compareTo(Cross o) {
		return this.id.compareTo(o.getId());
	}
}
