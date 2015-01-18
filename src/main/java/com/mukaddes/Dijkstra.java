

package com.mukaddes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Mukaddes Büyükkavut Ertaş
 *
 */
public class Dijkstra {
	
	@SuppressWarnings("unused")
	private ApplicationContext ctx;

	@SuppressWarnings("unused")
	private final List<Vertex> nodes;
	private final List<Edge> edges;
	
	private Set<Vertex> discoveredNodes;
	private Set<Vertex> visitedNodes;
	
	// This map contains the information for which node is the predecessor of which node.
	private Map<Vertex, Vertex> predecessorsMap;
	
	// This map contains the information for shortest distance of a node to source node.
	private Map<Vertex, Integer> shortestDistancesMap;

	/**
	 * Constructor of the Dijkstra.
	 * @param graph The graph that Dijkstra algorithm will be applied.
	 */
	public Dijkstra(GraphService graphService) {
		//this.ctx = new ClassPathXmlApplicationContext("beans.xml");
		Graph graph = graphService.getGraph();
		this.nodes = new ArrayList<Vertex>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * This methods runs the Dijkstra's algorithm on given graph and finds the
	 * shortest paths from source vertex to other vertexes.
	 * @param source Source vertex.
	 */
	public void execute(Vertex source) {
		
		discoveredNodes = new HashSet<Vertex>();
		visitedNodes = new HashSet<Vertex>();
		shortestDistancesMap = new HashMap<Vertex, Integer>();
		predecessorsMap = new HashMap<Vertex, Vertex>();
		
		shortestDistancesMap.put(source, 0);
		
		visitedNodes.add(source);
		
		while (visitedNodes.size() > 0) {
			Vertex node = getMinimum(visitedNodes);
			discoveredNodes.add(node);
			visitedNodes.remove(node);
			findMinimalDistances(node);
		}
	}
	
	/**
	 * Returns the vertex from unsettled vertexes that has the minimum distance to source vertex. 
	 * @param vertexes Set of the unsettled vertexes;
	 * @return Returns the vertex from unsettled vertexes that has the minimum distance to source vertex.
	 */
	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		
		for (Vertex vertex : vertexes) {
			
			if (minimum == null) {
				minimum = vertex;
			}
			else {
				
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		
		return minimum;
	}
	
	/**
	 * This methods returns the weight of the shortest distance from source to destination node if
	 * there is a path to source to destination.
	 * @param destination Destination node.
	 * @return Returns the weight of the shortest distance from source to destination node if there is a path to
	 * source to destination. Or it returns Integer.MAX_VALUE.
	 */
	private int getShortestDistance(Vertex destination) {
		Integer d = shortestDistancesMap.get(destination);
		
		if (d == null) {
			return Integer.MAX_VALUE;
		}
		else {
			return d;
		}
	}

	/**
	 * This methods finds the minimum distances 
	 * @param node
	 */
	private void findMinimalDistances(Vertex node) {
		
		List<Vertex> adjacentNodes = getNeighbors(node);
		
		for (Vertex target : adjacentNodes) {
			
			if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
				shortestDistancesMap.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessorsMap.put(target, node);
				visitedNodes.add(target);
			}
		}
	}
	
	/**
	 * This method returns the list of vertexes that neighbor to given node and not sattled.
	 * @param node The vertex which its neighbors requested.
	 * @return Returns the list of vertexes that neighbor to given node.
	 */
	private List<Vertex> getNeighbors(Vertex node) {
		
		List<Vertex> neighbors = new ArrayList<Vertex>();
		
		for (Edge edge : edges) {
			
			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		
		return neighbors;
	}

	/**
	 * This method returns the weight of the edge from destination to target node if it is exist.
	 * @param source Source node.
	 * @param destination Destination node.
	 * @return Returns the weight of the edge from destination to target node if it is exist. 
	 */
	private int getDistance(Vertex source, Vertex destination) {
		
		for (Edge edge : edges) {
			
			if (edge.getSource().equals(source) && edge.getDestination().equals(destination)) {
				return edge.getWeight();
			}
		}
		
		throw new RuntimeException("Should not happen");
	}
	
	/**
	 * This methods tells if given vertex is settled or not.
	 * @param vertex The vertex that queried.
	 * @return Returns true if the given vertex is settled or returns false.
	 */
	private boolean isSettled(Vertex vertex) {
		
		return discoveredNodes.contains(vertex);
	}
	
	/**
	 * This method returns the path from the source vertex to the destination vertex.
	 * @param destination Destination vertex.
	 * @return Returns the path from the source vertex to the destination vertex.
	 */
	public LinkedList<Vertex> getPath(Vertex destination) {
		
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = destination;
		
		// check if a path exists
		if (predecessorsMap.get(step) == null) {
			return null;
		}
		
		path.add(step);
		
		while (predecessorsMap.get(step) != null) {
			step = predecessorsMap.get(step);
			path.add(step);
		}
		
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}
}
