package com.ecommerce.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFrequencyCounter {

	private String filePath;
	
	public WordFrequencyCounter(String file) {
		this.filePath = file;
	}
	
	// https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
	// http://javarevisited.blogspot.com/2016/07/10-examples-to-read-text-file-in-java.html
	/**
	 *  File(Input|Output)Stream - read these bytes as a stream of byte.
		File(Reader|Writer) - read from a stream of bytes as a stream of char.
		BufferedReader - Reads text from a character-input stream, buffering characters so as to provide for 
						the efficient reading of characters, arrays, and lines.T
		Scanner - read from a stream of char and tokenise it.
		RandomAccessFile - read these bytes as a searchable byte[].
		FileChannel - read these bytes in a safe multithreaded way.
		byte[] Files.readAllBytes(Path path)
		List<String> Files.readAllLines(Path path, Charset cs)
		final Stream<String> words = Files.lines(Paths.get("myFile.txt")).
        flatMap((in) -> Arrays.stream(in.split("\\b")));
        
	 */
	public void printFile()
	{
		/**
		File file = new File(filePath);
		assert(file.exists());
		try {
			 BufferedReader br = new BufferedReader(new FileReader(file));
			  String st;
			  while ((st = br.readLine()) != null)
			    System.out.println(st);
		} catch (Exception e) {
			e.printStackTrace();
		} */
		
		try {
			Files.lines(new File(filePath).toPath())
			.map(s -> s.trim()) 
			.filter(s -> !s.isEmpty())
			.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Long> extract() 
	{
		try {

			Stream<String> lines = Files.lines(new File(filePath).toPath()).map(s -> s.trim())
					.filter(s -> !s.isEmpty());
			

			Stream<HashMap<String, Integer>> wordsCount = lines.map((l) -> {
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				String[] tokens = l.toLowerCase().split(" ");
				for (String token : tokens) {
					Integer count = map.getOrDefault(token, 0);
					map.put(token, count + 1);
				}
				return map;
			});

			BiConsumer<HashMap<String, Integer>, HashMap<String, Integer>> accumulator = (map1, orgmap) -> map1
					.putAll(orgmap);
			

			BiConsumer<HashMap<String, Integer>, HashMap<String, Integer>> combiner = (map1, map2) -> {
				for (Map.Entry<String, Integer> entry : map2.entrySet()) {
					if (map1.containsKey(entry.getKey())) {
						map1.put(entry.getKey(), map1.get(entry.getKey()) + entry.getValue());
					} else {
						map1.put(entry.getKey(), entry.getValue());
					}
				}

			};

			HashMap<String, Integer> result = wordsCount.collect(HashMap::new, accumulator, combiner);
			result.forEach((k, v) -> System.out.println(k + ":" + v));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * With FlatMaps !!
	 * 
	 * @return
	 */
	public Map<String, Long> extract2() {

		try {
			Stream<String> lines = Files.lines(new File(filePath).toPath()).map(s -> s.trim())
					.filter(s -> !s.isEmpty());

			Stream<String[]> wordsArr = lines.map((l) -> {
				String[] tokens = l.trim().toLowerCase().split(" ");
				return tokens;
			});

			Stream<String> words = wordsArr.flatMap(strArr -> Arrays.stream(strArr));
			//Map<String, Long> result = words.collect(Collectors.groupingBy((c) -> c, Collectors.counting()));
			//result.forEach((k, v) -> System.out.println(k + ":" + v));
			
			 // sort by key
			Map<String, Long> result = words.collect(Collectors.groupingBy((c) -> c, TreeMap::new, Collectors.counting()));
			result.forEach((k, v) -> System.out.println(k + ":" + v));
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String filePath = "./resources/sampleACAD.txt";
		WordFrequencyCounter counter = new WordFrequencyCounter(filePath);
		//counter.printFile();
		counter.extract2();
	}
}
