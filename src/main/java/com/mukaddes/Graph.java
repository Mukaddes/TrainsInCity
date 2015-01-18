package com.mukaddes;

import java.util.Iterator;
import java.util.List;

public class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("\nGraph:\n");
		sb.append("\nVertexes:\n");
		
		Iterator<Vertex> vertexInterator = vertexes.iterator();
		
		while (vertexInterator.hasNext()) {
			Vertex vertex = (Vertex) vertexInterator.next();
			sb.append(vertex.toString()+"\n");
		}
		
		sb.append("\nEdges:\n");
		
		Iterator<Edge> egdeIterator = edges.iterator();
		
		while (egdeIterator.hasNext()) {
			Edge edge = (Edge) egdeIterator.next();
			sb.append(edge.toString()+"\n");
		}
		
		return sb.toString();
	}
}
