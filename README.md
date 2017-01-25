# Custom Class Loader Tutorial

## Project Structure
	
![Project Structure](https://github.com/Amarnath510/CustomClassLoader/blob/master/customclassloader.png)

## Why do we need to write a Custom Class Loader?
  - Default ClassLoaders in Java (Bootstrap, Extension, Application) can load files from local file system that is good enough for most of the cases.
  - But if you are expecting a class at the runtime or from FTP server or via third party web service at the time of loading the class then you have to extend the existing class loader.

## Custom Class Loader Implementation: <br />
  We need to override two methods <br />
  
  - **getClass(String)**: Loads the class which we needed for our application by calling loadClassFileData(..) method.

    ```Java
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
    ```

  - **loadClass(String)**: Most important method. This class is responsible for loading the classes that start with "com.cldemo" else it will invoke parent class loaders.

   ```Java
   	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		
		if (name.startsWith("com.cldemo")) {
			System.out.println("*** Loading Class with CustomClassLoader ***: " + name);
			return getClass(name);
		}
		
		System.out.println("*** Loading by using default class loaders. ***: " + name);
		return super.loadClass(name);
	}
   ```

## Create two simple classes to test the Custom class loader.
  	
  ```Java
	public class Foo {
	    static public void main(String args[]) throws Exception {
		System.out.println("Foo Constructor >>> " + args[0] + " " + args[1]);
		Bar bar = new Bar(args[0], args[1]);
		bar.printCL();
	    }

	    public static void printCL() {
		System.out.println("Foo ClassLoader: "+Foo.class.getClassLoader());
	    }
	}
  ```	

  ```Java
	public class Bar {
	    public Bar(String a, String b) {
		System.out.println("Bar Constructor >>> " + a + " " + b);
	    }

	    public void printCL() {
		System.out.println("Bar ClassLoader: "+Bar.class.getClassLoader());
	    }
	}
  ```

## CustomClassLoader (Application start class)
  - This class invokes the main method of the Foo class.
  
  ```Java
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
  ```

## Running the program <br />
   Open Command Prompt under project root folder adn run the following commands,

  ```  
    > javac -cp . com/cldemo/Foo.java
    > javac -cp . com/cldemo/Bar.java
    > javac -cp . com/cldemo/CCLoader.java
    > javac -cp . com/cldemo/CustomClassLoader.java
    > java com/cldemo/CustomClassLoader com.cldemo.Foo 1212 1313
  ```

## OUTPUT
  - Only classes in com.cldemo are loaded using our Custom class loader and remaining are loaded using default class loaders.

  ```
	*** Loading Class with CustomClassLoader ***: com.cldemo.Foo

	*** Loading by using default class loaders ***: java.lang.Object
	*** Loading by using default class loaders ***: java.lang.String
	*** Loading by using default class loaders ***: java.lang.Exception
	*** Loading by using default class loaders ***: java.lang.System
	*** Loading by using default class loaders ***: java.lang.StringBuilder
	*** Loading by using default class loaders ***: java.io.PrintStream

	Foo Constructor >>> 1212 1313
	*** Loading Class with CustomClassLoader ***: com.cldemo.Bar
	Bar Constructor >>> 1212 1313
	*** Loading from default class loaders. ***: java.lang.Class
	Bar ClassLoader: com.cldemo.CCLoader@7852e922
  ```

## Credits
  - [Java ClassLoader](http://www.journaldev.com/349/java-classloader)
