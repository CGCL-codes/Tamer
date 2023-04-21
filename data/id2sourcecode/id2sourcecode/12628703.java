    public static Graph stressTest(int numFields, int numNodes, int numEdges) {
        final MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        final Random random = new Random(System.currentTimeMillis());
        final Graph graph = new Graph();
        final Attributes attr = new Attributes("TestNodeType");
        final EdgeType et = new EdgeType("TestEdgeType", "TestNodeType", "TestNodeType");
        long timer;
        long[] mem, mem2;
        cleanMem(mbean);
        mem = getMemory(mbean);
        timer = System.currentTimeMillis();
        attr.add(new AttributeKey("Name"));
        for (int f = 0; f < numFields; f++) attr.add(new AttributeContinuous("Field" + f));
        graph.addAttributes(attr);
        graph.addEdgeType(et);
        mem2 = getMemory(mbean);
        log.info("Constructed Attributes container: " + attr + lineSeparator + "Attribute setup took: " + (System.currentTimeMillis() - timer) / 1000.0 + " secs" + lineSeparator + "Memory change heap: " + printableBytes(mem2[0] - mem[0]) + " nonheap: " + printableBytes(mem2[1] - mem[1]));
        cleanMem(mbean);
        mem = getMemory(mbean);
        timer = System.currentTimeMillis();
        final double[] results = new double[attr.attributeCount()];
        for (int n = 0; n < numNodes; n++) {
            final Node node = graph.addNode("Node" + n, attr);
            for (int r = 0; r < results.length; r++) results[r] = random.nextDouble();
            node.setValues(results);
            if (STRESS_DEBUG) System.err.println("Created node <" + node + '>');
            if (n % 200000 == 0 && n > 0) log.config("Created " + n + " Nodes in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs");
        }
        mem2 = getMemory(mbean);
        log.info("Created " + numNodes + " Nodes in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs" + lineSeparator + "Memory change heap: " + printableBytes(mem2[0] - mem[0]) + " nonheap: " + printableBytes(mem2[1] - mem[1]));
        cleanMem(mbean);
        mem = getMemory(mbean);
        timer = System.currentTimeMillis();
        for (int e = 0; e < numEdges; e++) {
            final Node n1 = graph.getNode(random.nextInt(numNodes));
            final Node n2 = graph.getNode(random.nextInt(numNodes));
            graph.addEdge(et, n1, n2, 1.0);
            if (STRESS_DEBUG) System.err.println("Connected         [" + n1.getName() + "][" + n2.getName() + "] weight+=1.0" + " edgeType=" + et.getName());
            if (e % 250000 == 0 && e > 0) log.config("Created " + e + " Edges in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs");
        }
        mem2 = getMemory(mbean);
        log.info("Created " + numEdges + " Edges in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs" + lineSeparator + "Memory change heap: " + printableBytes(mem2[0] - mem[0]) + " nonheap: " + printableBytes(mem2[1] - mem[1]));
        final int[] nodeIndexes = new int[numNodes];
        for (int i = 0; i < nodeIndexes.length; i++) nodeIndexes[i] = i;
        for (int i = nodeIndexes.length; --i > 0; ) {
            final int swapidx = random.nextInt(i + 1);
            final int tmp = nodeIndexes[i];
            if (false) System.err.println("Swapping nodeIndexes[" + i + "]=" + nodeIndexes[i] + " and NodeIndexes[" + swapidx + "]=" + nodeIndexes[swapidx]);
            nodeIndexes[i] = nodeIndexes[swapidx];
            nodeIndexes[swapidx] = tmp;
        }
        if (false) {
            System.err.print("Getting Nodes in order:");
            for (int i : nodeIndexes) System.err.print(" " + i);
            System.err.println();
        }
        cleanMem(mbean);
        mem = getMemory(mbean);
        timer = System.currentTimeMillis();
        final int maxIter = 100;
        for (int count = 0; count < maxIter; count++) {
            for (int i = 0; i < numNodes; i++) {
                final Node node = graph.getNode(nodeIndexes[i]);
                final Edge[] neighborEdges = node.getEdges();
                for (int f = 0; f < numFields; f++) {
                    double result = node.getValue(f);
                    for (final Edge edge : neighborEdges) {
                        result += edge.getDest().getValue(f);
                        result += edge.getWeight();
                    }
                }
            }
            log.config("Completed loop " + count + " in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs");
        }
        mem2 = getMemory(mbean);
        log.info("Completed (" + maxIter + "x) accessing " + numNodes + " Nodes in " + (System.currentTimeMillis() - timer) / 1000.0 + " secs" + lineSeparator + "Memory change heap: " + printableBytes(mem2[0] - mem[0]) + " nonheap: " + printableBytes(mem2[1] - mem[1]));
        log.info("SUCCESS: Graph got " + graph.numNodes() + " Nodes and " + graph.numEdges() + " (actual) Edges");
        return graph;
    }
