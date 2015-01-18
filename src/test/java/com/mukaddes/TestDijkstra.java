package com.mukaddes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * 
 * @author Mukaddes Büyükkavut Ertaş
 *
 */
public class TestDijkstra {

	private static Logger logger = LogManager.getLogger(TestDijkstra.class.getSimpleName());

	@Test
	public void testExcute() {
		
		GraphFromFileService graphService = new GraphFromFileService("input.txt");
		Graph graph = graphService.getGraph();
		logger.log(Level.DEBUG, graph.toString());
		
		GraphTaskFromFileService taskService = new GraphTaskFromFileService("input.txt");
		LinkedList<GraphTask> taskList = taskService.getTasks();
		
		logger.log(Level.DEBUG, "\nTasks:\n");
		
		for (Iterator<GraphTask> iterator = taskList.iterator(); iterator.hasNext();) {
			GraphTask graphTask = (GraphTask) iterator.next();
			logger.log(Level.DEBUG, graphTask.toString());
		}
		
		executeTasks(taskList, graph);
	}
	
	public Boolean executeTasks(LinkedList<GraphTask> taskList, Graph graph){

		Dijkstra dijkstra = new Dijkstra(graph);
		
		while (taskList.size() > 0) {
			
			GraphTask task = taskList.remove(0);
			GraphTaskType taskType = task.getTaskType();
			
			if (taskType == GraphTaskType.FindShortestDistanceIntermsOfStepCount ||
					taskType == GraphTaskType.FindShortestDistanceIntermsOfTravelLength) {
				
				findDistance(task, dijkstra);
			}
			else if (taskType == GraphTaskType.FindPathsExact) {
				LinkedList<LinkedList<Vertex>> paths = findPathsInExactSteps(task, dijkstra);
				
				for (LinkedList<Vertex> path : paths) {
					printPath(path);
				}
			}
		}
		
		return true;
	}
	
	private boolean findDistance(GraphTask task, Dijkstra dijkstra ) {
		StringTokenizer st = new StringTokenizer(task.getTaskData(), "-");
		String sourceVertexName = st.nextToken();
		LinkedList<Vertex> shortestPath = new LinkedList<Vertex>();
		List<Vertex> vertexList = dijkstra.getVertexList();
		Vertex globalSource = null;
		Vertex destination = null;
		int weight = 0;
		
		for (Vertex vertex : vertexList) {
			
			// Check if there is a Vertex named with same name in task?
			if (vertex.getName().equals(sourceVertexName)) {
				
				// There can be mid towns to visit so mark this as global source
				if (globalSource == null) {
					globalSource = vertex;
					//logger.log(Level.DEBUG, "Global source vertex: " + globalSource);
				}
				
				dijkstra.execute(vertex);
				
				while (st.hasMoreTokens()) {
					
					String destinationVertexName = st.nextToken();
					
					for (Vertex vertex2 : vertexList) {
						
						// Check if there is a Vertex named with same name in task?
						if (vertex2.getName().equals(destinationVertexName)) {
							//logger.log(Level.DEBUG, "Next vertex: " + vertex2);
							destination =  vertex2;
							LinkedList<Vertex> midPath = dijkstra.getPath(vertex2);
							
							weight += dijkstra.getShortestDistance(vertex2);
							
							// Eliminate double add mid towns
							if (!shortestPath.isEmpty() && shortestPath.getLast().getName().equals(midPath.getFirst().getName())) {
								midPath.remove();
							}
							
							shortestPath.addAll(midPath);
							dijkstra.execute(vertex2);
							break;
						}
					}
				}//end of while
			}//end of if
		}// end of for
		
		if (shortestPath.size() > 0) {
			logger.log(Level.DEBUG, "There is a path between " + globalSource + " and " + destination + 
					" with step count " + (shortestPath.size() - 1) + " and weight " + weight);
			
			printPath(shortestPath);
		}
		else {
			logger.log(Level.DEBUG, "There is no path between " + globalSource + " and " + destination);
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method applies depth limited breadth first search to find paths from source to destination in exact step.
	 * @param task The task will be applied on graph.
	 * @param dijkstra To get neighbours list of a vertex.
	 * @return
	 */
	public LinkedList<LinkedList<Vertex>> findPathsInExactSteps(GraphTask task, Dijkstra dijkstra){
		
		StringTokenizer st = new StringTokenizer(task.getTaskData(), "-");
		
		String sourceVertexName = st.nextToken();
		Vertex sourceVertex = dijkstra.getVertexWithName(sourceVertexName);
		dijkstra.execute(sourceVertex);
		
		String destinationVertexName = st.nextToken();
		Vertex destinationVertex = dijkstra.getVertexWithName(destinationVertexName);
		
		int stepCount = Integer.parseInt(st.nextToken());
		
		LinkedList<LinkedList<Vertex>> paths = new LinkedList<LinkedList<Vertex>>();
		LinkedList<Vertex> currentPath = new LinkedList<Vertex>();
		
		int currentStepCount = 0;
		int desiredStepCount = stepCount;
		
		//logger.debug(sourceVertexName);
		//logger.debug(destinationVertexName);
		//logger.debug(sourceVertex);
		//logger.debug(destinationVertex);
		
		findPathsInExactSteps(dijkstra, sourceVertex, destinationVertex, currentStepCount, desiredStepCount, paths, currentPath);
		
		return paths;
	}
	
	public void findPathsInExactSteps (
			Dijkstra dijkstra, 
			Vertex source, 
			Vertex destination, 
			int currentStepCount, 
			int desiredStepCount, 
			LinkedList<LinkedList<Vertex>> paths,
			LinkedList<Vertex> currentPath) {
		
		//logger.debug("");
		//logger.debug("findPathsInExactSteps recursive");
		
		if (source == null || destination == null || dijkstra == null || paths == null || desiredStepCount < 1) {
			//logger.debug("Some of the data is null!");
			return;
		}
		
		if (currentStepCount > desiredStepCount) {
			return;
		}
		
		List<Vertex> neigbours = getNeighbors(source, dijkstra.getEdgeList());
		++currentStepCount;
		currentPath.add(source);
		
		for (Vertex vertex : neigbours) {
			
			//logger.debug(source + " visiting " + vertex + " in step " + currentStepCount);
			
			// found the destination vertex
			if (vertex.getName().equals(destination.getName())) {
				
				// is step count is equal to desired step count?
				if (currentStepCount == desiredStepCount) {
					
					//logger.debug("");
					//logger.debug("Found the destination on desired step!");
					currentPath.add(vertex);
					paths.add(currentPath);
					return;
				}
				else{
					
					//logger.debug("");
					//logger.debug("Found the destination on differen step than desired step: " + currentStepCount);
					findPathsInExactSteps(dijkstra, vertex, destination, new Integer(currentStepCount), desiredStepCount, paths, (LinkedList<Vertex>)currentPath.clone());
				}
			}
			else {
				findPathsInExactSteps(dijkstra, vertex, destination, new Integer(currentStepCount), desiredStepCount, paths, (LinkedList<Vertex>)currentPath.clone());
			}
		}
		
		return;
	}
	
	private void printPath(List<Vertex> path){
		StringBuilder sb = new StringBuilder();
		
		for (Vertex vertex : path) {
			
			if (sb.length() > 0) {
				sb.append(" --> ");
			}
			
			sb.append(vertex.toString());
		}
		
		logger.log(Level.DEBUG, sb.toString());
	}
	
	public List<Vertex> getNeighbors(Vertex node, List<Edge> edgeList) {
		
		List<Vertex> neighbors = new ArrayList<Vertex>();
		
		for (Edge edge : edgeList) {
			
			if (edge.getSource().equals(node)) {
				neighbors.add(edge.getDestination());
			}
		}
		
		return neighbors;
	}
}