package com.zeus.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class ClassScanner {
	
	private static Logger logger = Logger.getLogger(ClassScanner.class);
	
	/**
	 * 查询packageName包名下的所有带有注解classes的类，Class上注解
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findByAnnotationOnClass(String packageName,Class<?>... classes){
		return find(packageName,true,ClassType.ClassAnnotation,classes);
	}
	
	/**
	 * 查询packageName包名下的所有带有注解classes的类，Method上注解
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findByAnnotationOnMethod(String packageName,Class<?>... classes){
		return find(packageName,true,ClassType.MethodAnnotation,classes);
	}
	
	/**
	 * 查询packageName包名下的所有带有注解classes的类，Class或Method上注解
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findByAnnotation(String packageName,Class<?>... classes){
		return find(packageName,true,ClassType.Annotation,classes);
	}
	
	/**
	 * 查询packageName包名下的所有implements Interface classes的类
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findByInterface(String packageName,Class<?>... classes){
		return find(packageName,true,ClassType.Interface,classes);
	}
	
	
	/**
	 * 查询packageName包名下的所有implements Interface classes的类
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findByParent(String packageName,Class<?>... classes){
		return find(packageName,true,ClassType.SuperClass,classes);
	}
	
	/**
	 * 查询某个包名下的所有类
	 * @param packageName
	 * @return
	 */
	public static List<Class<?>> find(String packageName){
		return find(packageName,true,null,new Class<?>[]{});
	}

	/**
	 * 查询某个包名下的所有带有注解classes的类,
	 * @param packageName  目标包名称
	 * @param isRecursive  增加递归参数
	 * @param classType classes的类型
	 * @param classes
	 * @return 符合条件的class
	 */
	public static List<Class<?>> find(String packageName,boolean isRecursive,ClassType classType, Class<?>... classes) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			String strFile = packageName.replaceAll("\\.", "/");
			Enumeration<URL> urls = loader.getResources(strFile);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					String pkgPath = url.getPath();
					if ("file".equals(protocol)) {
						findClassName(classList, packageName, pkgPath, classType, isRecursive, classes);
					} else if ("jar".equals(protocol)) {
						findClassName(classList, packageName, url, classType, isRecursive, classes);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classList;
	}

	private static void findClassName(List<Class<?>> clazzList, String pkgName, String pkgPath, ClassType classType, boolean isRecursive,
			Class<?>... classes) {
		logger.info("scanning package " + pkgName);
		File[] files = new File(pkgPath).listFiles(file->{
			return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
		});
		if (files != null) {
			for (File f : files) {
				String fileName = f.getName();
				if (f.isFile()) {
					String clazzName = getClassName(pkgName, fileName);
					addClassName(clazzList, clazzName, classType , classes);
				} else {
					if (isRecursive) {
						String subPkgName = pkgName + "." + fileName;
						String subPkgPath = pkgPath + "/" + fileName;
						findClassName(clazzList, subPkgName, subPkgPath, classType, true, classes);
					}
				}
			}
		}
	}

	private static void findClassName(List<Class<?>> clazzList, String pkgName, URL url, ClassType classType, boolean isRecursive,
			Class<?>... clazz) throws IOException {
		JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = jarURLConnection.getJarFile();
		logger.info("scanning jar " + jarFile.getName());
		logger.info("classpath: " + System.getProperty("CLASSPATH"));
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName(); // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
			String clazzName = jarEntryName.replace("/", ".");
			int endIndex = clazzName.lastIndexOf(".");
			String prefix = null;
			if (endIndex > 0) {
				String prefix_name = clazzName.substring(0, endIndex);
				endIndex = prefix_name.lastIndexOf(".");
				if (endIndex > 0) {
					prefix = prefix_name.substring(0, endIndex);
				}
			}
			if (prefix != null && jarEntryName.endsWith(".class")) {
				if (prefix.equals(pkgName)) {
					addClassName(clazzList, clazzName,classType, clazz);
				} else if (isRecursive && prefix.startsWith(pkgName)) {
					addClassName(clazzList, clazzName,classType, clazz);
				}
			}
		}
	}

	private static String getClassName(String pkgName, String fileName) {
		int endIndex = fileName.lastIndexOf(".");
		String clazz = null;
		if (endIndex >= 0) {
			clazz = fileName.substring(0, endIndex);
		}
		String clazzName = null;
		if (clazz != null) {
			clazzName = pkgName + "." + clazz;
		}
		return clazzName;
	}

	@SuppressWarnings("unchecked")
	private static void addClassName(List<Class<?>> clazzList, String clazzName,ClassType classType,Class<?>... classes) {
		if (clazzList != null && clazzName != null) {
			try {
				Class<?> clazz = Class.forName(clazzName);
				Method[] methods = clazz.getMethods();
				Boolean add = Boolean.TRUE;
				if (clazz != null) {
					if (classes != null) {
						switch (classType) {
							case ClassAnnotation:
								for(Class<?> cl : classes)
									if(!clazz.isAnnotationPresent((Class<Annotation>)cl)) {
										add = Boolean.FALSE;
										break;
									}
								break;
							case MethodAnnotation:
								for(Method method:methods){
									for(Class<?> cl : classes){
										if(!method.isAnnotationPresent((Class<Annotation>)cl)){
											add = Boolean.FALSE;
											break;
										}
									}
									if(add){
										break;
									}
								}
								break;
							case Annotation:
								for(Class<?> cl : classes){
									if(!clazz.isAnnotationPresent((Class<Annotation>)cl)) {
										add = Boolean.FALSE;
										break;
									}
								}
								if(add){
									break;
								}
								add = Boolean.TRUE;
								for(Method method:methods){
									for(Class<?> cl : classes){
										if(!method.isAnnotationPresent((Class<Annotation>)cl)){
											add = Boolean.FALSE;
											break;
										}
									}
									if(add){
										break;
									}
								}
								break;
							case SuperClass:
								for(Class<?> cl : classes)
									if(!cl.isAssignableFrom(clazz)) {
										add = Boolean.FALSE;
										break;
									}
								break;
							case Interface:
								for(Class<?> cl : classes)
									if(!cl.isAssignableFrom(clazz)) {
										add = Boolean.FALSE;
										break;
									}
								break;
							default:
								break;
						}
					}
					if(add){
						logger.debug("add Class" + clazz.getName());
						clazzList.add(clazz);
					}
				}
			} catch (ClassNotFoundException e) {
				logger.error("ClassNotFoundException for " + clazzName,e);
			}
		}
	}
	
	public enum ClassType{
		MethodAnnotation,ClassAnnotation,Annotation,SuperClass,Interface
	}
}
