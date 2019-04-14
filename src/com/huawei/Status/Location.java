package com.huawei.Status;

public class Location {
	
	private int roadId; //默认为-1
	private int channel;
	private int parking;
	private int fromCrossId;
	private int toCrossId;
	public Location() {
		this.roadId = -1;
		this.channel = -1;
		this.parking = -1;
		this.fromCrossId = -1;
		this.toCrossId = -1;
	}
	public Location(Location loc) {
		super();
		this.roadId = loc.roadId;
		this.channel = loc.channel;
		this.parking = loc.parking;
		this.fromCrossId = loc.fromCrossId;
		this.toCrossId = loc.toCrossId;
	}
	public Location(int roadId, int channel, int parking, int fromCrossId, int toCrossId) {
		super();
		this.roadId = roadId;
		this.channel = channel;
		this.parking = parking;
		this.fromCrossId = fromCrossId;
		this.toCrossId = toCrossId;
	}
	public int getToCrossId() {
		return toCrossId;
	}
	public void setToCrossId(int toCrossId) {
		this.toCrossId = toCrossId;
	}
	public int getRoadId() {
		return roadId;
	}
	public void setRoadId(int roadId) {
		this.roadId = roadId;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public int getParking() {
		return parking;
	}
	public void setParking(int parking) {
		this.parking = parking;
	}
	public int getFromCrossId() {
		return fromCrossId;
	}
	public void setFromCrossId(int fromCrossId) {
		this.fromCrossId = fromCrossId;
	}
	@Override
	public String toString() {
		return "Location [roadId=" + roadId + ", channel=" + channel + ", parking=" + parking + ", fromCrossId="
				+ fromCrossId + ", toCrossId=" + toCrossId + "]";
	}
	
	
}
