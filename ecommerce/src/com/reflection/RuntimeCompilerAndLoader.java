package com.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class RuntimeCompilerAndLoader {

	// converting a .java file to .class file and return class loader for the
	// compiled files
	public URLClassLoader compileAndGetClassLoader(String sourceLocation, String outputLocation) {
		try {

			List<File> files = new ArrayList<File>();
			Files.walk(Paths.get(sourceLocation)).filter(Files::isRegularFile)
					.filter(f -> f.toString().toLowerCase().endsWith(".java"))
					.forEach(f -> files.add(new File(f.toUri())));

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjects(files.toArray(new File[files.size()]));
			Iterable<String> options = Arrays.asList(new String[] { "-d", outputLocation });
			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null,
					compilationUnits);
			task.call();
			fileManager.close();

			File outputDirectory = new File(outputLocation);
			URL[] urls = new URL[] { outputDirectory.toURL() };
			return new URLClassLoader(urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<String> getClassNames(File outputLocation) throws IOException {
		ArrayList<String> classNames = new ArrayList<String>();
		Files.walk(Paths.get(outputLocation.getPath())).filter(Files::isRegularFile)
				.filter(f -> f.toString().toLowerCase().endsWith(".class")).forEach(f -> {
					String fileName = f.toFile().getPath();
					String outputDir = outputLocation.getPath();
					fileName = fileName.replace(outputDir, "");
					fileName = fileName.replace(".class", "");
					fileName = fileName.replaceAll("/", ".");
					fileName = fileName.substring(1);
					classNames.add(fileName);
				});
		return classNames;
	}

	// get interfaces and superclasses
	public void GetInterfaces(URLClassLoader cl, String temp) {
		try {
			Class<?> cls = cl.loadClass(temp);
			System.out.println("---------------------------");
			System.out.println(cls.getName());
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				System.out.println("\t" + field.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			RuntimeCompilerAndLoader obj = new RuntimeCompilerAndLoader();

			String sourcefiles = "./src/";
			String destinationDirectory = "./generated/";
			
			File outputLocation = new File(destinationDirectory);
			outputLocation.mkdirs();

			// converting a .java file to .class and load into class loader
			URLClassLoader cl = obj.compileAndGetClassLoader(sourcefiles, outputLocation.getAbsolutePath());

			// get class names from the output location
			ArrayList<String> classNames = obj.getClassNames(outputLocation);

			// use reflection to figure out names and methods
			classNames.forEach(c -> obj.GetInterfaces(cl, c));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
