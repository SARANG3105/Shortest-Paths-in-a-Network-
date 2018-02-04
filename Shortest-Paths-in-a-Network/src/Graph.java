
//Graph.java
//Graph code, modified from code by Mark A Weiss.
//Computes Unweighted shortest paths.


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;



//Used to signal violations of preconditions for
//various shortest path algorithms.
class GraphException extends RuntimeException
{
	public GraphException( String name )
	{
		super( name );
	}
}

//Edge Class containing linked list of destination vertices and the transmission time for there edges.
class Edge{
	public Vertex destName;
	public Float trans_time;
	public String edgeStatus;
	public Edge(Vertex destName,Float trans_time){
		this.destName=destName;
		this.trans_time=trans_time;
		this.edgeStatus="up";
	}
} 
//Represents a vertex in the graph.
class Vertex
{
	boolean visited;
	public String     name;   // Vertex name
	public List<Edge> adj;    // Adjacent vertices
	public Float trans_time;
	public Vertex prev;   // Previous vertex on shortest path
	public String vertexStatus;

	public Vertex( String nm )
	{ name = nm; adj = new LinkedList<Edge>(); reset( );vertexStatus="up";visited=false;}

	public void reset( )
	{ trans_time =Graph.INFINITY; prev = null; }    

}

//Graph class: evaluate shortest paths.
//
//CONSTRUCTION: with no parameters.
//
//******************PUBLIC OPERATIONS**********************
//void addEdge( String v, String w,Float trans_time ) --> Add additional edge
//void deleteEdge(String v,String w)--> Delete edges from Graph
//void edgeDown(String v,String w)-->Sets the edge to down state
//void printPath( String w )   --> Print path after alg is run
//void ShortestPath( String s )  --> Dijkstra's Algorithm
//******************ERRORS*********************************
//Some error checking is performed to make sure graph is ok,
//and to make sure graph satisfies properties needed by each
//algorithm.  Exceptions are thrown if errors are detected.

public class Graph
{
	public static final float INFINITY = Integer.MAX_VALUE;

	private Map<String,Vertex> vertexMap = new TreeMap<String,Vertex>( );//Contains Vertices.
	private LinkedList<Edge> downedges=new LinkedList<Edge>();//Contains Down Edges.


	/**
	 * Add a new edge to the graph.
	 */
	public void addEdge(String sourceName, String destName,Float trans_time){
		Vertex v = getVertex( sourceName );
		Vertex w = getVertex(destName);

		if(v.adj.contains(w)){
			v.adj.get(0).trans_time=trans_time;
		}else if(!v.adj.contains(w)){
			v.adj.add(new Edge(w,trans_time));
		}
	}	


	//Deletes the edge from a provided sourceVertex to destinationVertex if it exists.
	public void deleteEdge(String sourceName,String destName){

		Vertex v = getVertex(sourceName);
		Vertex w = getVertex(destName);
		Edge e=getEdge(sourceName, destName);

		if(w.name==e.destName.name){
			v.adj.remove(e);
		
			System.out.println();
		}else if(w.name!=e.destName.name){
			System.out.println("no such edge");
		}
	}	
//this method turn the edge status down.
	public void edgeDown(String sourceName,String destName){

		Vertex w=getVertex(destName);
		Edge edge=getEdge(sourceName, destName);

		if(w.name.equals(edge.destName.name)){
			edge.edgeStatus="down";
			downedges.add(edge);
		
			System.out.println();
		}else{
			System.out.println("No Such Edge");
		}
	}
// this method turns the edge status up if it is down
	public void edgeUp(String sourceName,String destName){
		Vertex v=getVertex(sourceName);
		Vertex w=getVertex(destName);
		Edge edge=getEdge(sourceName, destName);
		if(downedges.contains(edge)){
			downedges.remove(edge);
		
			System.out.println();
		}else {

			System.out.println("No Such Edge");
		}
	}
//this method sets the vertex status to up if it is down.
	public void vertexUp(String vertex){
		if(vertexMap.containsKey(vertex)){
			Vertex v=getVertex(vertex);
			v.vertexStatus="up";
		}else{
			System.out.println("No Such Vertex");
		}
	}
	//this method sets the vertex state to down.
	public void vertexDown(String vertex){
		if(vertexMap.containsKey(vertex)){
			Vertex v=getVertex(vertex);
			v.vertexStatus="down";
		}else{
			System.out.println("No Such Vertex");
		}
	}

	//this method gets the edge for by taking the sourcename and destination name as input
	private Edge getEdge(String sourceName,String destName){
		Vertex v= getVertex(sourceName);
		Vertex w=getVertex(destName);
		if(v==null||w==null){
			return null;
		}
		for(Edge edge:v.adj){
			if(edge.destName.name.equals(w.name)){
				return edge;
			}
		}
		return null;
	}
	private Vertex getVertex( String vertexName )
	{
		Vertex v = vertexMap.get( vertexName );
		if( v == null )
		{
			v = new Vertex( vertexName );
			vertexMap.put( vertexName, v );
		}
		return v;
	}


	/**
	 * Driver routine to print total distance.
	 * It calls recursive routine to print shortest path to
	 * destNode after a shortest path algorithm has run.
	 */
	public void printPath( String destName )
	{
		Vertex w = vertexMap.get( destName );
		if( w == null )
			throw new NoSuchElementException( "Destination vertex not found" );
		else if( w.trans_time == INFINITY )
			System.out.println( destName + " is unreachable" +w );
		else
		{

			printPath( w );
			
			DecimalFormat format = new DecimalFormat("0.##");
			
			System.out.println("  "+format.format(w.trans_time));
		}
	}

	/**
	 * Recursive routine to print shortest path to dest
	 * after running shortest path algorithm. The path
	 * is known to exist.
	 */

	private void printPath( Vertex dest )
	{
		if( dest.prev != null )
		{
			printPath( dest.prev );
			System.out.print("  " );
		}
		System.out.print( dest.name );
	
	}


	/**
	 * Initializes the vertex output info prior to running
	 * any shortest path algorithm.
	 */
	private void clearAll()
	{
		for( Vertex v : vertexMap.values( ) )
			v.reset( );
	}


	public void print(){
		clearAll();
		Map<String,Vertex> vertices=new TreeMap<String,Vertex>(vertexMap);

		for(Vertex v:vertices.values()){
			if(v.vertexStatus.equals("up")){
				System.out.println(v.name);
			}else if(v.vertexStatus.equals("down")){
				System.out.println(v.name+" DOWN");

			}
			TreeMap<String,Edge> ne=new TreeMap<String,Edge>();
			for(Edge e:v.adj){
				ne.put(e.destName.name,e);
			}
			for (Edge adjVertex : ne.values()) {
				Edge edge = getEdge(v.name,adjVertex.destName.name);
				if (downedges.contains(edge)){
					System.out.println("  " +adjVertex.destName.name + " " + adjVertex.trans_time+"  DOWN");
					
				}
				else {
					System.out.println("  " +adjVertex.destName.name + " " + adjVertex.trans_time);
			
					continue;
				}
			}
		}

	}
	//Finds the shortest path using Dijkstras Algorithm.
	public void ShortestPath(String source){
		clearAll();
		BinaryMinHeap pq=new BinaryMinHeap(vertexMap.size());
		Vertex startVertex=vertexMap.get(source);
		startVertex.trans_time=0.0f;
		if(startVertex.equals(null)){
			System.out.println("no such vertex");
		}else if(startVertex.vertexStatus.equals("down")){
			System.out.println("Vertex is down");
		}
		pq.insert(new Edge(startVertex,startVertex.trans_time));
		while(pq.size()!=0){
			Vertex v=getVertex(pq.remove().destName.name);
			TreeMap<Float,Edge> e=new TreeMap<Float,Edge>();
			for (Edge e1 : v.adj) {
				e.put(e1.trans_time,e1);
			}
			for(Edge edge:e.values()){
				Vertex u=getVertex(edge.destName.name);
				if(u.vertexStatus.equals("down")||downedges.contains(edge)){
					continue;
				}
				if(u.trans_time>v.trans_time+edge.trans_time){
					u.trans_time=v.trans_time+edge.trans_time;
					u.prev=v;
					pq.insert(new Edge(u,u.trans_time));
				}else if(edge.trans_time<0){
					System.out.println("Negative Edge");
				}
			}
		}

	}
/*	
for every vertex in list this algorithm sets the transmission time to infinity except the staring vertex
the discovered vertices are added to the queue and removed on by one from the queue when the queue is empty 
it gets the vertex from vertex list to start again. So for every vertex it searches the adjacent vertices
and if it is infinity adds it to the queue. the running time for this algorithm is O(V+E).
*/

	public void reachable(){

		for(Vertex start:vertexMap.values()){
			if(start.vertexStatus.equals("up")){
				System.out.println(start.name);
				clearAll( ); 


				if( start == null )
					throw new NoSuchElementException( "Start vertex not found" );

				Queue<Vertex> q = new LinkedList<Vertex>( );
				q.add(start); start.trans_time = 0.0f;

				while( !q.isEmpty( ) )
				{
					Vertex v = q.remove( );

					for( Edge w : v.adj ){

						if(w.destName.vertexStatus.equals("up")){
							if(!downedges.contains(w)){

								if( w.destName.trans_time == INFINITY )
								{
									w.destName.trans_time = v.trans_time + 1;
									w.destName.prev = v;
									q.add( w.destName );
									System.out.println("  "+w.destName.name);
									
								}
							}
						}
					}
				}
			}
		}
	}

	public static boolean processRequest( Scanner queries, Graph g ) throws FileNotFoundException{
		try {
			while(queries.hasNextLine()){
			String qry = queries.nextLine();
	
			String[] arr = qry.split(" ");
	
			switch (arr[0]) {
	
			case "print":
				System.out.println();
				g.print();				
				break;	
			case "quit":
				return false;	
			case "reachable":
				System.out.println();
				g.reachable();
				break;	
			case "path":
				if (arr.length == 3) {
					System.out.println();
					g.ShortestPath(arr[1]);
					g.printPath(arr[2]);
				} 
				break;	
			case "edgeup":
				if (arr.length == 3) {
					g.edgeUp(arr[1], arr[2]);
				}
				break;	
			case "edgedown":
				if (arr.length == 3) {
					g.edgeDown(arr[1], arr[2]);
				}
				break;	
			case "vertexup":
				if (arr.length == 2) {
					g.vertexUp(arr[1]);
				}
				break;
	
			case "vertexdown":
				if (arr.length == 2) {
					g.vertexDown(arr[1]);
				}
				break;
	
			case "deleteedge":
				if (arr.length == 3) {
					g.deleteEdge(arr[1], arr[2]);
				}
				break;
	
			case "addedge":
				if (arr.length == 4) {
					g.addEdge(arr[1], arr[2], Float.parseFloat(arr[3]));
				}
				break;
	
			default:
				System.out.println("Query not valid");
				break;
			}}
		} catch (NoSuchElementException e) {
			System.out.println("Invalid Vertex Name");
		} catch (GraphException e) {
			System.err.println(e);
		}
	
		return false;
	}
		
	/**
	 * A main routine that:
	 * 1. Reads a file containing edges (supplied as a command-line parameter);
	 * 2. Forms the graph;
	 * 3. Repeatedly prompts for two vertices and
	 *    runs the shortest path algorithm.
	 * The data file is a sequence of lines of the format
	 *    source destination 
	 */
	public static void main( String args[] )
	{
		Graph g = new Graph( );
		try
		{
			FileReader fin = new FileReader( args[0] );
			FileReader fin2=new FileReader(args[1]);		
			Scanner graphFile = new Scanner( fin );
			Scanner queries = new Scanner(fin2 );
			// Read the edges and insert
			String line;
			while( graphFile.hasNextLine( ) )
			{
				line = graphFile.nextLine( );
				StringTokenizer st = new StringTokenizer( line );
		
				
				try
				{
					if( st.countTokens( ) != 3 )
					{
					
						continue;
					}
					String source  = st.nextToken( );
					String dest    = st.nextToken( );
					Float trans_time=Float.parseFloat(st.nextToken());
					g.addEdge( source, dest,trans_time );
					g.addEdge(dest, source, trans_time);
					PrintStream out = new PrintStream(new File(args[2]));
					 System.setOut(out);
					PrintStream Console =System.out;
					System.setOut(Console);
				}
				catch( NumberFormatException e ){
			
					System.out.println( "File read..." );
					System.out.println( g.vertexMap.size() + " vertices");

				}
				PrintStream out = new PrintStream(new File(args[2]));
				System.setOut(out);
				PrintStream Console =System.out;
				System.setOut(Console);
			}while( processRequest( queries, g ) );
			
		}
		catch( IOException e )
		{ System.err.println( e ); }

		
		
	}
}


//binary min heap implementation as the priority queue.
class BinaryMinHeap{
	private static final int maxChild=2;
	private Edge[] heap;
	int heapSize;
	private static final int root=1;
	int n;
	public BinaryMinHeap(int n){
		this.heapSize=0;
		this.n=n;
		Vertex v=new Vertex("null");
		heap=new Edge[this.n+1];
		heap[0]=new Edge(v,(float) Integer.MIN_VALUE);
	}

	public int size(){
		return heapSize;
	}

	public boolean isEmpty(){
		return heapSize==0;
	}
	public Edge remove(){
		Edge p=heap[root];
		heap[root]=heap[heapSize--];
		minHeapify(root);
		return p;
	}
	private void minHeapify(int index){
		if(!isLeaf(index)&&this.heapSize>0){
			if(heap[index].trans_time>heap[leftChild(index)].trans_time||
					heap[index].trans_time<heap[rightChild(index)].trans_time){
				if(heap[leftChild(index)].trans_time<heap[rightChild(index)].trans_time){
					swap(index,leftChild(index));
					minHeapify(leftChild(index));
				}else{
					swap(index,rightChild(index));
					minHeapify(rightChild(index));
					System.out.println();
				}
			}

		}
	}
	public void minHeap() {
		for (int index = (heapSize / maxChild); index >= 1; index--) {
			minHeapify(index);
		}
	}

	private void swap(int pin,int cin){
		Edge temp;
		temp=heap[pin];
		heap[pin]=heap[cin];
		heap[cin]=temp;

	}
	public void insert(Edge e) throws IllegalArgumentException{
		this.heapSize++;
		this.heap[heapSize] = e;
		int c = this.heapSize;

		if(this.heapSize > 1){

			while(this.heap[c].trans_time < heap[parent(c)].trans_time) {
				swap(c, parent(c));
				c= parent(c);
			}
		}
	}

	private int parent(int index){
		return index/maxChild;
	}
	private int leftChild(int index){
		return maxChild*index;
	}
	private int rightChild(int index){
		return (maxChild*index)+1;
	}
	private boolean isLeaf(int index){
		return (index>=(heapSize/maxChild) && index<=heapSize);
	}
}

