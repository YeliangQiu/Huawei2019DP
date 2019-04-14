package com.huawei.object;

import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.Status.RunStatus;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  15:44
 * @description:
 */
public class CarStatus {
    private CarDirection direction;
    private Location location;
    private RunStatus runStatus;
    private int nowSpeed;
    private boolean finished;
    public int getNowSpeed() {
        return nowSpeed;
    }
    public void setNowSpeed(int nowSpeed) {
        this.nowSpeed = nowSpeed;
    }
    public boolean getisFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public CarStatus() {
        direction = CarDirection.NONE;
        location = new Location();
        runStatus = RunStatus.WAITING;
        nowSpeed = 0;
        finished = false;
    }
    public CarStatus(boolean finish) {
        this.finished = finish;
        this.runStatus = RunStatus.FINISHED;
    }
    public CarStatus(CarStatus carStatus) {
        this.direction = carStatus.direction;
        this.location = new Location(carStatus.location);
        this.runStatus = carStatus.runStatus;
        this.nowSpeed = carStatus.nowSpeed;
        this.finished = carStatus.finished;
    }
    public CarStatus(CarDirection direction, Location location, RunStatus runStatus, int nowSpeed) {
        super();
        this.direction = direction;
        this.location = location;
        this.runStatus = runStatus;
        this.nowSpeed = nowSpeed;
        this.finished = false;
    }
    public CarDirection getDirection() {
        return direction;
    }
    public void setDirection(CarDirection direction) {
        this.direction = direction;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public RunStatus getRunStatus() {
        return runStatus;
    }
    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }
    @Override
    public String toString() {
        return "CarStatus [direction=" + direction + ", location=" + location + ", runStatus=" + runStatus
                + ", nowSpeed=" + nowSpeed + ", finished=" + finished + "]";
    }
}
