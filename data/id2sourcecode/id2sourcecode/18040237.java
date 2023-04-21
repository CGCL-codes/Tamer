    protected Node getNode(int nodeID) {
        int left = 0;
        int right = this.nodes.length;
        while (left < right) {
            int middle = (left + right) / 2;
            if (this.nodes[middle].getID() < nodeID) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        if (right < this.nodes.length && nodeID == this.nodes[right].getID()) {
            return this.nodes[right];
        }
        return null;
    }
