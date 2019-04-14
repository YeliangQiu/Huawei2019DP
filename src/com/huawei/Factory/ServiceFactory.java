package com.huawei.Factory;

import com.huawei.Service.*;

public class ServiceFactory {
	
//	public static ServiceFactory instance = null;
	public static CarService carService = null;
	public static CrossService crossService = null;
	public static RoadService roadService = null;
	public static CRService crService = null;
	
	static {
//		instance = new ServiceFactory();
		carService = new CarServiceImp();
		crossService = new CrossServiceImp();
		roadService = new RoadServiceImp();
		crService = new CRServiceImp();
	}
	
	private ServiceFactory(){
		
		carService = new CarServiceImp();
		crossService = new CrossServiceImp();
		roadService = new RoadServiceImp();
		crService = new CRServiceImp();
		
	}
	
//	private static ServiceFactory getInstance() {
//		System.out.println("1 " + instance);
//		if(instance == null) {
//			synchronized (ServiceFactory.class) {
//				if(instance == null) {
//					instance = new ServiceFactory();
//				}
//			}
//		}
//		
//		System.out.println("2 " + instance);
//		return instance;
//	}
	
}
