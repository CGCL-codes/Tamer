    public IGraph loadNetwork() {
        long t1 = System.currentTimeMillis();
        int numArcs;
        int numEdges;
        int numNodes;
        short sentidoDigit;
        RandomAccessFile file;
        try {
            file = new RandomAccessFile(netFile.getPath(), "r");
            FileChannel channel = file.getChannel();
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buf.order(ByteOrder.LITTLE_ENDIAN);
            numArcs = buf.getInt();
            numEdges = buf.getInt();
            numNodes = buf.getInt();
            GvGraph g = new GvGraph(numArcs, numEdges, numNodes);
            buf.position(36 * numEdges + 12);
            for (int i = 0; i < numNodes; i++) {
                GvNode node = readNode(buf);
                g.addNode(node);
            }
            buf.position(12);
            for (int i = 0; i < numEdges; i++) {
                GvEdge edge = readEdge(buf);
                edge.setIdEdge(i);
                g.addEdge(edge);
                GvNode nodeOrig = g.getNodeByID(edge.getIdNodeOrig());
                nodeOrig.addOutputLink(edge);
                GvNode nodeEnd = g.getNodeByID(edge.getIdNodeEnd());
                nodeEnd.addInputLink(edge);
                EdgePair edgePair = g.getEdgesByIdArc(edge.getIdArc());
                if (edgePair == null) {
                    edgePair = new EdgePair();
                    g.addEdgePair(edge.getIdArc(), edgePair);
                }
                if (edge.getDirec() == 1) edgePair.setIdEdge(i); else edgePair.setIdInverseEdge(i);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("Tiempo de carga: " + (t2 - t1) + " msecs");
            System.out.println("NumEdges = " + g.numEdges());
            return g;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
