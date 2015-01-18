package com.mukaddes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Mukaddes Büyükkavut Ertaş
 *
 */
public class GraphTaskFromFileService implements GraphTaskService {

	private static Logger logger = LogManager.getLogger(GraphTaskFromFileService.class.getSimpleName());
	private static final String TASK = "Task";
	private LinkedList<GraphTask> taskList;
	private String fileName;
	
	public GraphTaskFromFileService() {
		super();
	}
	
	public GraphTaskFromFileService(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<GraphTask> getTasks() {
		taskList = new LinkedList<GraphTask>();
		File file = new File(fileName);

		if (!file.exists()) {
			logger.error("Task definition file not exist!");
			return taskList;
		}
		
		try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) ) ) {
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				if (line.startsWith(TASK)) {
					
					StringTokenizer st = new StringTokenizer(line);
					
					// Skip Task tag
					st.nextToken();
					
					// get task type
					String taskType = st.nextToken();
					
					// get towns to visit
					String townsToVisit = st.nextToken();
					
					// Task: Find distance of path in term of steps
					if (taskType.startsWith("#DS")) {
						taskList.add(new GraphTask(GraphTaskType.FindShortestDistanceIntermsOfStepCount, townsToVisit));
					}
					// Task: Find distance of path in term of travel length
					else if (taskType.startsWith("#DL")) {
						taskList.add(new GraphTask(GraphTaskType.FindShortestDistanceIntermsOfTravelLength, townsToVisit));
					}
					// Task: Find number of paths less than given travel length
					else if (taskType.startsWith("#PLT")) {
						taskList.add(new GraphTask(GraphTaskType.FindPathsLessThan, townsToVisit));
					}
					// Task: Find number of paths less than given travel length
					else if (taskType.startsWith("#PM")) {
						taskList.add(new GraphTask(GraphTaskType.FindPathsMaximum, townsToVisit));
					}
					// Task: Find number of paths less than given travel length
					else if (taskType.startsWith("#PE")) {
						taskList.add(new GraphTask(GraphTaskType.FindPathsExact, townsToVisit));
					}
				}
			}
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return taskList;
	}
}
