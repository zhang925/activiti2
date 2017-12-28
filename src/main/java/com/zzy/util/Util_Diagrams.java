package com.zzy.util;

import java.io.File;

public class Util_Diagrams {
	
	public static String[] list(){
		String basePath= Util_Diagrams.class.getResource("/").getPath();
		basePath=basePath.substring(1,basePath.length());
		return new File(basePath+File.separator+"diagrams").list();
	}

}
