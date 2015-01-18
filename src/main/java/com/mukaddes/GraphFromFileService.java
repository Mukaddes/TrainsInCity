
package com.mukaddes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;

/**
 * 
 * Reads graph data from file and builds a Graph with the data and returns it.
 * @author Mukaddes Büyükkavut Ertaş
 * 
 */
public class GraphFromFileService implements GraphService {

	private static Logger logger = LogManager.getLogger(GraphFromFileService.class.getSimpleName());
	private static final String GRAPH = "Graph";
	
	private String fileName;
	
	public GraphFromFileService() {
		this.fileName = null;
	}
	
	public GraphFromFileService(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Reads the graph data from file. Builds a new Graph with this data and returns it.
	 * @returns Returns a new Graph object with the data provided from input file. If any problems occurs during
	 * data parsing it returns an empty Graph.
	 */
	public Graph getGraph() {
		
		Map<String, Vertex> vertexesMap = new HashMap<String, Vertex>();
		Map<String, Edge> edgeMap = new HashMap<String, Edge>();
		
		File file = new File(fileName);

		if (!file.exists()) {
			logger.error("Graph definition file not exist!");
			// Return empty graph
			LinkedList<Vertex> vertexList = new LinkedList<Vertex>();	
			LinkedList<Edge> edgeList = new LinkedList<Edge>();
			return new Graph(vertexList, edgeList);
		}
		
		try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) ) ) {
		
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				if (line.startsWith(GRAPH)) {
					StringTokenizer st = new StringTokenizer(line);
					
					// Skip Graph tag
					st.nextToken();
					
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						
						if (token.length() != 3) {
							logger.warn("Invlid graph token: Length is different from 3!");
							continue;
						}
						
						char source = token.charAt(0);
						char destination = token.charAt(1);
						int length = Integer.parseInt(token.substring(2));
						
						Vertex vertexSource = new Vertex(String.format("%c", source), String.format("%c", source));
						Vertex vertexDestination = new Vertex(String.format("%c", destination), String.format("%c", destination));
						
						if (!vertexesMap.containsKey(vertexSource.getId())) {
							vertexesMap.put(vertexSource.getId(), vertexSource);
						}
						
						if (!vertexesMap.containsKey(vertexDestination.getId())) {
							vertexesMap.put(vertexDestination.getId(), vertexDestination);
						}
						
						String id = "Edge from " + vertexSource.getName() + " to " + vertexDestination.getName();
						
						Edge edge = new Edge(id, vertexSource, vertexDestination, length);
						
						if (!edgeMap.containsKey(id)) {
							edgeMap.put(id, edge);
						}
					}//end of inner while
					
					// Only one graph definition needed, so break
					break;
				}//end of if
			}// end of while
		}// end of try
		catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Iterator<Vertex> it = vertexesMap.values().iterator();
		
		while (it.hasNext()) {
			Vertex v = (Vertex) it.next();
			vertexList.add(v);
		}
		
		LinkedList<Edge> edgeList = new LinkedList<Edge>();
		Iterator<Edge> edgeIterator = edgeMap.values().iterator();
		
		while (edgeIterator.hasNext()) {
			Edge edge = (Edge) edgeIterator.next();
			edgeList.add(edge);
		}
		
		return new Graph(vertexList, edgeList);
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
