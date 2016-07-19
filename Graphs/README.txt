Name: Aninditha Madishetty
ID#: 800936606
Email: amadishe@uncc.edu

which compiler you used:
     Java compiler
Description of program design:
     1. This program reads the file given as an input in the command line.
     2. It then Builds the initial graph and waits for the user to input a query.
     3. This program then responds according to the query.
     4. it makes a vertex reachable or unreachable, Edge reachable or unreachable, prints the graph and 
        finds all the reachable set of vertices for every Vertex.
     5. This program also calculates the shortest path based on the transmitting time of the edgs.
What works well and fails:
     1. All the queries are giving the expected output.
Data structure design:
     1. There are 3 classes in the program. Edge Vertex Graph classes.
     2. Vertex class maintains all the vertex related information like vertex status, vertex name, list of adjacent 
         vertices and edges.
     3. Edge class maintains all the Edge related information like edge status, edge name, edge cost.
     4. Graph class contains all the methods to handle user queries
     5. Dijkstra's algorithm uses implementation of HEAPS in a priority Queue to compute the shortest path from
         a given source to destination in O((|V| + |E|)log|V|) time complexity.
     6. Reachability uses BFS to find the reachable vertices in (|V| + |E|) time complexity.
        But, this algorithm runs for every vertex. So the final time complexity of reachability is O((|V|+|E|)*|V|)