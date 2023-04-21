    private int[] BreadthFirst(Graph g, int v1, int v2) {
        int node;
        int i, j;
        int child;
        float tempDist;
        int[] resultArray;
        if (!g.freshlyReset) {
            g.reset();
            reset();
        }
        if (msgLevel >= 2) System.out.println("\nFinding path with \"Breadth First\", from node " + v1 + " to node " + v2);
        g.freshlyReset = false;
        enqueue(v1);
        while (readPtr != writePtr) {
            node = dequeue();
            for (i = 0; i < g.nodeList[node].edges.length; i++) {
                child = g.getOtherNode(node, g.nodeList[node].edges[i]);
                if (child == -1) continue;
                tempDist = g.nodeList[node].accumDist + g.edgeList[g.nodeList[node].edges[i]].cost;
                if (!g.nodeList[child].visited || (g.nodeList[child].accumDist > tempDist)) {
                    g.nodeList[child].bfParent = node;
                    g.nodeList[child].bfDepth = g.nodeList[node].bfDepth + 1;
                    g.nodeList[child].visited = true;
                    g.nodeList[child].accumDist = tempDist;
                    enqueue(child);
                }
            }
        }
        if (g.nodeList[v2].visited) {
            accumDist = g.nodeList[v2].accumDist;
            resultArray = new int[g.nodeList[v2].bfDepth + 1];
            node = v2;
            for (j = 0; j < resultArray.length; j++) {
                resultArray[resultArray.length - j - 1] = node;
                node = g.nodeList[node].bfParent;
            }
        } else {
            if (msgLevel >= 1) System.out.println("No connection between start node and end node");
            return null;
        }
        if (msgLevel >= 2) printResult(resultArray, accumDist);
        return resultArray;
    }
