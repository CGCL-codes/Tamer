    public Graph loadJungNetwork() {
        SparseGraph g = new SparseGraph();
        long t1 = System.currentTimeMillis();
        RandomAccessFile file;
        try {
            file = new RandomAccessFile(netFile.getPath(), "r");
            FileChannel channel = file.getChannel();
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buf.order(ByteOrder.LITTLE_ENDIAN);
            int numArcs = buf.getInt();
            int numEdges = buf.getInt();
            int numNodes = buf.getInt();
            buf.position(24 * numEdges + 12);
            for (int i = 0; i < numNodes; i++) {
                GvNode node = readNode(buf);
                Vertex v = new DirectedSparseVertex();
                g.addVertex(v);
            }
            Indexer indexer = Indexer.getIndexer(g);
            buf.position(12);
            for (int i = 0; i < numEdges; i++) {
                GvEdge edge = readEdge(buf);
                int nodeOrig = edge.getIdNodeOrig();
                int nodeEnd = edge.getIdNodeEnd();
                Vertex vFrom = (Vertex) indexer.getVertex(nodeOrig);
                Vertex vTo = (Vertex) indexer.getVertex(nodeEnd);
                DirectedSparseEdge edgeJ = new DirectedSparseEdge(vFrom, vTo);
                g.addEdge(edgeJ);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("Tiempo de carga: " + (t2 - t1) + " msecs");
            return g;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
