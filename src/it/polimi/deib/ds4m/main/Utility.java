/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */

package it.polimi.deib.ds4m.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utility {
	
	
	/**
	 * read a file form a specified path
	 * 
	 * @param path the path of the file to read
	 * @param encoding the encoding of the file to read
	 * @return the content of the file
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	/**
	 * source https://www.baeldung.com/java-list-directory-files returns a list of
	 * files
	 * 
	 * @param dir
	 *            the path of the directory to visit
	 * @param depth
	 *            the level of folder to inspect, to inspect a folder it must be set
	 *            to 1
	 * @return
	 * @throws IOException
	 */
	public static Set<String> listFilesUsingFileWalk(String dir, int depth) throws IOException {
		try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
			return stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString)
					.collect(Collectors.toSet());
		}
	}
	
	
	/**
	 * removes all files in a directory (not recursive)
	 * 
	 * @param pathToDir
	 */
	public static boolean removeAllFiles(String pathToDir)
	{
		File dir = new java.io.File(pathToDir);
		
		if (!dir.exists())
		{
			System.err.println("The path "+pathToDir+" is not a directory" );
			return false;
		}
		
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory()) 
		    {
		    	System.out.println("Removed: "+file.getAbsolutePath());
		        file.delete();
		    }
		
		return true;
	}

}
