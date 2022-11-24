The following specifications are copied from professor Kevin Lin's CSE373 course website.
# Image processing
Seam-carving is a content-aware image resizing technique where the size of an image is reduced by one pixel in height (by removing a horizontal seam) or width (by removing a vertical seam) at a time. Unlike cropping pixels from the edges or scaling the entire image, seam carving attempts to identify and preserve the most important content in an image.

#### Horizontal seam
A path of adjacent or diagonally-adjacent pixels from the left edge of an image to its right edge.

#### Vertical seam
The same as a horizontal seam but from the top edge of an image to its bottom edge.
## Design
### Interface
All codes in the following classes are provided by professor Lin.

#### Picture
A representation of a fixed-size digital image where the color of each pixel is an int. In image processing, pixel (x, y) refers to the pixel in column x and row y, with pixel (0, 0) at the upper-left corner and pixel (W − 1, H − 1) at the lower-right corner. Note that this is the opposite of the standard linear algebra notation where (i, j) refers to row i and column j and (0, 0) is at the lower-left corner.
Seam carving is a 3-step algorithm, each represented as a Java class or interface.

#### EnergyFunction
The energy function defines the importance of a given pixel in a picture. The higher the energy, the greater its importance. The video at 1:45 depicts bright, high-energy pixels around object boundaries.

#### SeamFinder
The seam finding algorithm will avoid high-energy pixels by searching for a horizontal seam with the minimum energy path. The horizontal seam is returned as a list of integers representing the y-coordinates. Continuing from 1:45, the video identifies a vertical seam that can be removed from the picture without noticeably affecting the content.

#### SeamCarver
The seam carving algorithm combines an energy function with the seam finder to identify and remove the most unnoticeable seams from an picture. This class handles vertical seams by transposing the picture before calling SeamFinder.

The focus of this project is on comparing implementations for SeamFinder. In order to solve this problem, the SeamFinder algorithm must find a shortest path from the left edge of a Picture to its right edge. To bootstrap the design, we’ve provided common graph interfaces and algorithms in the graphs package based on code presented in the lesson.

#### graphs.Graph<V>
Represents a directed, edge-weighted graph with a single method that returns a list of the neighbors of a given vertex. The directed Edge class provides 3 methods: access to the origin vertex from, the destination vertex to, and the edge weight.

#### graphs.ShortestPathSolver<V>
Computes a shortest paths tree in a directed, edge-weighted Graph. Implementations of the ShortestPathSolver interface must provide a public constructor that accepts two parameters: a graph and a start vertex. The solution method returns the list of vertices representing a shortest path from the start vertex to the given goal vertex.

#### graphs.ExtrinsicMinPQ<T>
Priority queue interface for DijkstraSolver.

The generic type V is used throughout the graphs package to represent the vertex data type. All vertices will be of the interface type Node, which provides a single method called neighbors that accepts a Picture and an EnergyFunction. Rather than defining all of the graph logic for every type of node in the Graph.neighbors method, this approach allows us to define specialized behavior on a per-node basis by implementing Node.neighbors.

#### Node
An adapter for the Graph interface that delegates responsibility for neighbors to each individual Node rather than the entire Graph.

### Reference implementation
All codes in the following classes are provided by professor Lin.

#### AdjacencyListSeamFinder(DijkstraSolver::new)
An adjacency list-like Graph representation combined with Dijkstra’s shortest paths algorithm. The AdjacencyListSeamFinder class contains a PixelGraph class that implements Graph<Node>. Most of the logic for initializing the adjacency list representation is in the PixelGraph constructor. The PixelGraph class defines 3 Node implementations, each with their own algorithms for determining how to compute neighbors.
##### source
The source node contains 0 incoming edges and picture.height() outgoing edges to each Pixel in the first column of the picture. The weight for each outgoing edge represents the energy of the corresponding pixel in the first column.
##### Pixel
Represents an (x, y) coordinate pixel in the underlying picture with directed edges to its right-up, right-middle, and right-down neighbors. Most pixels have 3 adjacent neighbors except for pixels at the boundary of the picture that only have 2 adjacent neighbors. The weight of an edge from—to represents the energy of the to pixel.
##### sink
The sink node contains picture.height() incoming edges and 0 outgoing edges: no neighbors.

The AdjacencyListSeamFinder.findSeam method constructs a PixelGraph, runs the shortest paths solver on the graph from the source node, and uses the path to the sink node to find a horizontal seam with the minimum energy. Since the shortest paths solver returns the entire path, the source and sink nodes need to be ignored in the final result with seam.subList(1, seam.size() - 1).

The reference implementation decomposes the problem along the following abstraction levels.

- Picture represents the real colors of each pixel in the picture.
- DualGradientEnergyFunction computes the gradient energy for any pixel.
- PixelGraph is a graph representation of the picture where gradient energies are placed on the outgoing edges of each corresponding node.
- ShortestPathSolver computes the shortest path in the PixelGraph.

### Implementation task
Design and implement an alternative graph representation and graph algorithm for solving shortest paths in a directed acyclic graph.

Codes in the following classes are written by my partner Ruyi Chen and me.

#### GenerativeSeamFinder
Rather than precomputing and storing the neighbors for every single Pixel in a 2-D array, this graph representation computes the neighbors for each pixel on demand to reduce memory usage. Identify the relevant portions of the AdjacencyListSeamFinder.PixelGraph class and modify it so that the PixelGraph.pixels and Pixel.neighbors fields are no longer needed.
  
#### graphs.ToposortDAGSolver
Computes a shortest paths tree in a directed acyclic graph using the reduction to topological sorting presented in the lesson.

#### DynamicProgrammingSeamFinder
A dynamic programming seam finding algorithm. This algorithm solves the problem by considering pixels from left to right similar to the ToposortDAGSolver except that it operates directly on the Picture rather than through an abstract Graph representation.
