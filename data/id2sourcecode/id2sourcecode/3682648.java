    protected void reSetRoutePath(short nodeNum, boolean overwrite) {
        if (!overwrite && this.checkIfAlreadyRouted(nodeNum)) {
            System.err.println("Already routed");
            return;
        }
        this.initNodes();
        short last;
        this.nodeList[nodeNum].addExtraProperty("Distance", new Short((short) 0));
        count++;
        while (true) {
            last = getUnknownSmallestNode();
            if (last < 0) break;
            this.nodeList[last].addExtraProperty("Known", new Boolean(true));
            this.updateNeighbors(last, nodeNum);
        }
        this.cleanNodes();
        System.err.println(count + " " + new Date(System.currentTimeMillis()).toString());
    }
