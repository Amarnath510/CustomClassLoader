package com.cldemo;

import java.lang.reflect.Method;

public class CustomClassLoader {
	public static void main(String[] args) {
		try {
			String progClass = args[0];
			String[] proArgs = new String[args.length - 1];
			System.arraycopy(args, 1, proArgs, 0, proArgs.length);
			
			CCLoader ccl = new CCLoader(CustomClassLoader.class.getClassLoader());
			Class clas = ccl.loadClass(progClass);
			Class[] mainArgsType = { (new String[0]).getClass() };
			Method mainMethod = clas.getMethod("main", mainArgsType);
			
			Object[] argsArray = {proArgs};
			mainMethod.invoke(null, argsArray);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}














