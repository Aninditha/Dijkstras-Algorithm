package project2;
//Name: Aninditha Madishetty
//ID#: 800936606
//Email: amadishe@uncc.edu

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

//Represents the edges in the graph.
class Edge {

	public Vertex source, destination;
	public boolean status;
	public double cost;

	public Edge(Vertex source, Vertex destination, double cost, boolean status) {
		this.source = source;
		this.destination = destination;
		this.status = status;
		this.cost = cost;
	}

	public Edge() {
	}

	public String getDestination() {
		return destination.getName();
	}

	public Vertex getDestVertex() {
		return destination;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}

// Represents a vertex in the graph.
class Vertex {

	private String name; // Vertex name
	public boolean status;
	public List<Edge> adjacent;
	public double dist;
	public Vertex prev;
	public int handle;
	private String color;

	public Vertex(String nm, boolean status) {
		name = nm;
		this.status = status;
		adjacent = new LinkedList<Edge>();
	}

	public Vertex() {
	}

	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
	}

	public String getName() {
		return name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getHandle() {
		return handle;
	}

	public void setHandle(int handle) {
		this.handle = handle;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}

// All the graph methods are in this graph
public class Graph {

	public static final int INFINITY = Integer.MAX_VALUE;
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}

	public Vertex getVertex(String name, boolean status) {
		Vertex v = vertexMap.get(name);

		if (v == null) {
			v = new Vertex(name, status);
			vertexMap.put(name, v);
		}
		return v;
	}

	// Adds edge to adjacent list in a vertex
	public void addEdge(String source, String destination, double cost, boolean status) {

		boolean flag = false;
		Vertex v = getVertex(source, status);
		Vertex w = getVertex(destination, status);
		Edge edge = new Edge(v, w, cost, status);
		if (v.adjacent.size() == 0)
			v.adjacent.add(edge);
		else {
			for (Iterator i = v.adjacent.iterator(); i.hasNext();) {
				Edge adj = (Edge) i.next();
				if (adj.getDestination().equals(destination)) {
					adj.setCost(cost);
					flag = true;
					break;
				} else
					continue;
			}
			if (!flag)
				v.adjacent.add(edge);
		}
	}

	// Deletes a specified edge
	private void deleteEdge(String source, String dest) {
		Vertex v = vertexMap.get(source);
		for (Iterator i = v.adjacent.iterator(); i.hasNext();) {
			Edge edge = (Edge) i.next();
			if (edge.getDestination().equals(dest)) {
				v.adjacent.remove(edge);
				break;
			}
		}
	}

	// Makes an Edge status false and make the edge unreachable
	private void edgeDown(String source, String dest) {
		Vertex v = vertexMap.get(source);
		for (Iterator i = v.adjacent.iterator(); i.hasNext();) {
			Edge edge = (Edge) i.next();
			if (edge.getDestination().equals(dest))
				edge.setStatus(false);
		}
	}

	// Makes an Edge status true and make the edge reachable
	private void edgeUp(String source, String dest) {
		Vertex v = vertexMap.get(source);
		for (Iterator i = v.adjacent.iterator(); i.hasNext();) {
			Edge edge = (Edge) i.next();
			if (edge.getDestination().equals(dest))
				edge.setStatus(true);
		}
	}

	// Makes a Vertex status false and make the Vertex unreachable
	private void vertexDown(String vertex) {
		Iterator it = vertexMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (pair.getKey().equals(vertex)) {
				Vertex v = (Vertex) pair.getValue();
				v.setStatus(false);
				break;
			}
		}
	}

	// Makes an Vertex status true and make the Vertex reachable
	private void vertexUp(String vertex) {
		Iterator it = vertexMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (pair.getKey().equals(vertex)) {
				Vertex v = (Vertex) pair.getValue();
				v.setStatus(true);
				break;
			}
		}
	}

	private void sortEdges(List<Edge> edgeList) {
		Comparator<Edge> COMPARATOR = new Comparator<Edge>() {
			public int compare(Edge edge1, Edge edge2) {
				String destination1 = edge1.getDestination().toUpperCase();
				String destination2 = edge2.getDestination().toUpperCase();
				return destination1.compareTo(destination2);
			}
		};
		Collections.sort(edgeList, COMPARATOR);
	}

	/*
	 * Running Time is O(|V|*(|V|+|E|)) BFS runs with O(|V|+|E|) time complexity Since
	 * we need to find reachable vertices for all the vertices the time
	 * complexity is O(|V|) times BFS time complexity.
	 * 
	 * reachability refers to the ability to get from one vertex to another within a graph.
	 * We say that a vertex s can reach a vertex t (or that t is reachable from s) if there exists
	 * a sequence of adjacent vertices (i.e. a path) which starts with s and ends with t.
	 * Breadth-first search is a way to find all the vertices reachable from the a given source vertex, s.
	 * Like depth first search, BFS traverse a connected component of a given graph and defines a spanning tree.
	 * 
	 * We send a wave out from source s. The wave hits all vertices 1 edge from s.
	 * From there, the wave hits all vertices 2 edges from s. Etc.
	 * We use FIFO queue Q to maintain the wavefront: v is in Q if and only if wave has hit v but has not
	 * come out of v yet.
	 */
	private void reachable() {

		SortedSet<String> keys = new TreeSet<String>(vertexMap.keySet());
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Vertex v = vertexMap.get(it.next());
			if (v.isStatus()) {
				System.out.println(v.getName());
				bfs(v);
			}
		}
	}

	private void bfs(Vertex s) {
		SortedSet<String> adjVertex = new TreeSet<>();
		clearAll();

		for (Vertex v : vertexMap.values())
			v.setColor("white");

		s.setColor("gray");
		s.dist = 0;
		s.prev = null;

		Queue<String> vertices = new PriorityQueue<String>();
		vertices.add(s.getName());

		while (!vertices.isEmpty()) {
			String name = vertices.remove();
			Vertex u = vertexMap.get(name);
			for (Iterator i = u.adjacent.iterator(); i.hasNext();) {
				Edge edge = (Edge) i.next();
				Vertex adj = edge.getDestVertex();
				if (adj.getColor().equals("white") && adj.isStatus()) {
					adj.setColor("gray");
					adj.dist = u.dist + 1;
					adj.prev = u;
					vertices.add(adj.getName());
					adjVertex.add(adj.getName());
				}
			}
			u.setColor("black");
		}
		Iterator it = adjVertex.iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			System.out.println("  " + name);
		}
	}

	// Prints the graph in the required format
	private void printGraph() {
		SortedSet<String> keys = new TreeSet<String>(vertexMap.keySet());
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Vertex v = vertexMap.get(it.next());
			if (v.isStatus())
				System.out.println(v.getName());
			else
				System.out.println(v.getName() + " DOWN");
			sortEdges(v.adjacent);
			for (Iterator i = v.adjacent.iterator(); i.hasNext();) {
				// v.adjacent.sort();
				Edge edge = (Edge) i.next();
				if (edge.isStatus())
					System.out.println("  " + edge.getDestination() + " " + edge.getCost());
				else
					System.out.println("  " + edge.getDestination() + " " + edge.getCost() + " DOWN");
			}
		}
	}

	// Extracts the Vertex with minimum distance in the heap.
	public Vertex extractMin(Vertex[] queue, int size) {
		Vertex min = queue[0];
		queue[0] = queue[size - 1];
		queue[0].setHandle(0);
		minHeapify(queue, 0, size - 1);
		return min;
	}

	// Builds a heap
	private void buildMinHeap(Vertex[] queue, int size) {
		int length = size / 2;
		for (int index = length - 1; index >= 0; index--) {
			minHeapify(queue, index, size);
		}
	}

	// Restores the heap order
	private void minHeapify(Vertex[] queue, int index, int size) {
		int l = left(index);
		int r = right(index);
		int smallest;
		if (l < size && queue[l].dist < queue[index].dist)
			smallest = l;
		else
			smallest = index;
		if (r < size && queue[r].dist < queue[smallest].dist)
			smallest = r;
		if (smallest != index) {
			Vertex temp = queue[index];
			queue[index] = queue[smallest];
			queue[index].setHandle(index);
			temp.setHandle(smallest);
			queue[smallest] = temp;
			minHeapify(queue, smallest, size);
		}
	}

	// Retrieves the Right child
	private int right(int index) {
		return 2 * index + 2;
	}

	//Retrieves the Left child 
	private int left(int index) {
		return 2 * index + 1;
	}

	// Decreases the priority of the newly updated vertex.
	public void HeapdecreaseKey(Vertex[] queue, int i, Vertex key) {
		queue[i] = key;
		while (i > 0 && queue[(i - 1) / 2].dist > queue[i].dist) {
			Vertex temp = queue[(i - 1) / 2];
			Vertex temp2 = queue[i];
			temp.setHandle(i);
			queue[i] = temp;
			temp2.setHandle((i - 1) / 2);
			queue[(i - 1) / 2] = temp2;
			i = (i - 1) / 2;
		}
	}

	// Updates all the vertices to maintain shortest distance from a given source.
	// Updates the previous Vertex as well
	private double dijkstrasAlgo(String source, String dest) {

		clearAll(); // running time: |V|
		int count = 0; // running time: Constant
		for (Vertex v : vertexMap.values()) // running time: |V|
			if (v.isStatus())
				count++;

		Vertex[] queue = new Vertex[count]; // running time: Constant
		Vertex start = vertexMap.get(source); // running time: Constant
		start.dist = 0; // running time: Constant
		start.prev = null; // running time: Constant

		int index = 0; // running time: Constant
		for (Vertex v : vertexMap.values()) { // running time: |V|
			if (v.isStatus()) {
				queue[index] = v;
				v.setHandle(index);
				index++;
			}
		}

		buildMinHeap(queue, count); // running time: |V|

		while (count != 0) { // running time: |V|
			Vertex min = extractMin(queue, count); // running time: log |V|
			count--; // running time: Constant

			for (Iterator i = min.adjacent.iterator(); i.hasNext();) { // running time: |E|
				Edge edge = (Edge) i.next();
				if (edge.isStatus()) {
					Vertex adjVertex = vertexMap.get(edge.getDestination());
					if (adjVertex.dist > (min.dist + edge.getCost()) && adjVertex.isStatus()) {
						adjVertex.dist = (min.dist + edge.getCost());
						adjVertex.prev = min;
						int pos = adjVertex.getHandle();
						HeapdecreaseKey(queue, pos, adjVertex); // running time: log |V|
					}
				}
			}
		}
		return vertexMap.get(dest).dist; // running time: Constant
	}

	private void printPath(String vertex) {
		Vertex dest = vertexMap.get(vertex);
		if (dest.prev != null)
			printPath(dest.prev.getName());
		System.out.print(dest.getName() + " ");
	}

	public static void main(String[] args) {

		Graph g = new Graph();
		boolean status = true;
		String source, dest, type;
		double cost;

		try {
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);

			// Reads the edges and insert them into its corresponding vertices.
			String line;
			System.out.println("Reading file...");
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					source = st.nextToken();
					dest = st.nextToken();
					cost = Double.parseDouble(st.nextToken());

					g.addEdge(source, dest, cost, status);
					g.addEdge(dest, source, cost, status);
				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line " + line);
				}
			}
			graphFile.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		System.out.println("File read...");
		System.out.println(g.vertexMap.size() + " vertices\n");

		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.print("Enter your query: ");
			String query = in.nextLine();
			StringTokenizer st = new StringTokenizer(query);

			if (query.startsWith("print")) {
				if (st.countTokens() != 1) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				g.printGraph();
			} else if (query.startsWith("path")) {
				if (st.countTokens() != 3) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				dest = st.nextToken();
				double Distance = g.dijkstrasAlgo(source, dest);
				BigDecimal a = new BigDecimal(Distance);
				BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				g.printPath(dest);
				System.out.print(" " + roundOff);
			} else if (query.startsWith("edgedown")) {
				if (st.countTokens() != 3) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				dest = st.nextToken();
				g.edgeDown(source, dest);
			} else if (query.startsWith("edgeup")) {
				if (st.countTokens() != 3) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				dest = st.nextToken();
				g.edgeUp(source, dest);
			} else if (query.startsWith("vertexdown")) {
				if (st.countTokens() != 2) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				g.vertexDown(source);
			} else if (query.startsWith("vertexup")) {
				if (st.countTokens() != 2) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				g.vertexUp(source);
			} else if (query.startsWith("reachable")) {
				if (st.countTokens() != 1) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				g.reachable();
			} else if (query.startsWith("addedge")) {
				if (st.countTokens() != 4) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				dest = st.nextToken();
				cost = Double.parseDouble(st.nextToken());
				g.addEdge(source, dest, cost, status);
			} else if (query.startsWith("deleteedge")) {
				if (st.countTokens() != 3) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				type = st.nextToken();
				source = st.nextToken();
				dest = st.nextToken();
				g.deleteEdge(source, dest);
			} else if (query.startsWith("quit")) {
				if (st.countTokens() != 1) {
					System.err.println("Skipping ill-formatted line " + query);
					continue;
				}
				System.exit(0);
			} else
				System.out.println("Invalid query");
			System.out.println();
		}
	}
}