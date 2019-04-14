package com.huawei.object;

import com.huawei.Status.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  13:10
 * @description: 道路信息
 */
public class Road {
    private int id;
    private int length;
    private int speed;
    private int channel;
    private int from;
    private int to;
    private int isDuplex;
    private Channel[] Arraychannel;
    private Map<String, List<Channel>> channelMap = new HashMap<String, List<Channel>>();
    List<Channel> fromChannels = null;
    List<Channel> toChannels = null;
    private Map<String, String> nowSpeed = new HashMap<String, String>();
//    public Map<String, Integer> repeatTime = new HashMap<>();
//    public int preCar = -1;
//    public Location loc = null;

    public Road(int id, int length, int speed, int channel, int from, int to, int isDuplex) {
        this.id = id;
        this.length = length;
        this.speed = speed;
        this.channel = channel;
        this.from = from;
        this.to = to;
        this.isDuplex = isDuplex;
//        this.preCar = -1;
//        this.loc = null;
    }

    public Map<String, List<Channel>> getChannelMap() {
        if(channelMap.isEmpty()) {
            channelMap.put(from + "-" + to, this.getFromChannels());
            channelMap.put(to + "-" + from, this.getToChannels());
        }
        return channelMap;
    }

    //懒加载实现from->to的车道数量
    public List<Channel> getFromChannels() {

        if(fromChannels == null) {
            fromChannels = new ArrayList<>();
            for(int i = 0; i < channel; i++) {
                fromChannels.add(new Channel(length));
            }
        }

        return fromChannels;
    }

    //懒加载实现to->from的车道数量
    public List<Channel> getToChannels() {

        if(this.isDuplex == 1 && toChannels == null) {
            toChannels = new ArrayList<>();
            for(int i = 0; i < channel; i++) {
                toChannels.add(new Channel(length));
            }
        }

        return toChannels;
    }

    public void setIsDuplex(int isDuplex) {
        this.isDuplex = isDuplex;
    }

    public void setArraychannel(Channel[] arraychannel) {
        Arraychannel = arraychannel;
    }

    public int getIsDuplex() {
        return isDuplex;
    }

    public Channel[] getArraychannel() {
        return Arraychannel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setDuplex(int duplex) {
        isDuplex = duplex;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getSpeed() {
        return speed;
    }

    public Map<String, String> getNowSpeed() {
    	if(nowSpeed.isEmpty()) {
    		this.nowSpeed.put(from + "-" + to, this.speed+"-"+0);
            if(this.isDuplex == 1)
                this.nowSpeed.put(to + "-" + from, this.speed+"-"+0);
    	}
		return nowSpeed;
	}

	public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getDuplex() {
        return isDuplex;
    }
}
