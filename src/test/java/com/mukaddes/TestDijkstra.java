package com.mukaddes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mukaddes.Edge;
import com.mukaddes.Graph;
import com.mukaddes.GraphFromFileService;
import com.mukaddes.GraphTask;
import com.mukaddes.GraphTaskFromFileService;
import com.mukaddes.Vertex;

/**
 * 
 * @author Mukaddes Büyükkavut Ertaş
 *
 */
public class TestDijkstra {

	private static Logger logger = LogManager.getLogger(TestDijkstra.class.getSimpleName());
	
	private List<Vertex> nodes;
	private List<Edge> edges;
	
	private ApplicationContext ctx;

	@Test
	public void testExcute() {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		
		//ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		GraphFromFileService graphService = new GraphFromFileService();
		graphService.setFileName("input.txt");
		
		Graph graph = graphService.getGraph();
		logger.log(Level.DEBUG, graph.toString());
		
		GraphTaskFromFileService taskService = new GraphTaskFromFileService("input.txt");
		List<GraphTask> taskList = taskService.getTasks();
		
		logger.log(Level.DEBUG, "\nTasks:\n");
		
		for (Iterator iterator = taskList.iterator(); iterator.hasNext();) {
			GraphTask graphTask = (GraphTask) iterator.next();
			logger.log(Level.DEBUG, graphTask.toString());
		}
		
//		Dijkstra dijkstra = (Dijkstra) ctx.getBean("Dijkstra");
//		
//		dijkstra.execute(nodes.get(0));
//		
//		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));
//
//		assertNotNull(path);
//		assertTrue(path.size() > 0);
//
//		for (Vertex vertex : path) {
//			System.out.println(vertex);
//		}
	}
}