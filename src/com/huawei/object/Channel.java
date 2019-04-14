package com.huawei.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  16:32
 * @description:
 */
public class Channel {
    private int curCarNumber=0; //此时刻车道里面的车

    //初始化车道长度
    public Channel(int length) {
        for(int i = 0; i < length; i++){
            parkings.add(null);
        }
    }

    List<Car> parkings = new ArrayList<>();

    public int getCurCarNumber() {
        return curCarNumber;
    }

    public void setParking(int length) {
        for(int i = 0; i < length; i++){
            parkings.add(null);
        }
    }

    public List<Car> getParkings() {
        return parkings;
    }

}
