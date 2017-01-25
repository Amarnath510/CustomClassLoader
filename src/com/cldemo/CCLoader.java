package com.cldemo;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CCLoader extends ClassLoader {
	
	public CCLoader(ClassLoader parent) {
		super(parent);
	}
	
	/**
	 * IMPORTANT Method.
	 * Every request for a class passes through this method. If the class is in
     * com.classloaderdemo package, we will use this classloader or else delegate the
     * request to parent classloader.
	 * 
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		
		if (name.startsWith("com.cldemo")) {
			System.out.println("*** Loading Class with CustomClassLoader ***: " + name);
			return getClass(name);
		}
		
		System.out.println("*** Loading by using default class loaders. ***: " + name);
		return super.loadClass(name);
	}
	
	public Class getClass(String name) throws ClassNotFoundException {
		String file = name.replace('.', File.separatorChar) + ".class";
		byte[] buffer = null;
		
		try {
			// This will load the byte code data from file.
			buffer = loadClassFileData(file);
			
			Class c = defineClass(name, buffer, 0, buffer.length);
			resolveClass(c);
			
			return c;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}

	private byte[] loadClassFileData(String name) throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
		int size = stream.available();
		byte[] buffer = new byte[size];
		DataInputStream in = new DataInputStream(stream);
		in.readFully(buffer);
		in.close();
		return buffer;
	}
}