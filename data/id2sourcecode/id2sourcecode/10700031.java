    public void initialiseInternalStates() {
        int nodeCount = tree.getNodeCount();
        int maxState = microsatPat.getDataType().getStateCount() - 1;
        int internalNodeCount = tree.getInternalNodeCount();
        Parameter.DefaultBounds bounds = new Parameter.DefaultBounds(maxState, 0, internalNodeCount);
        for (int nodeNum = tree.getExternalNodeCount(); nodeNum < nodeCount; nodeNum++) {
            NodeRef node = tree.getNode(nodeNum);
            NodeRef leftChild = tree.getChild(node, 0);
            NodeRef rightChild = tree.getChild(node, 1);
            int leftChildState = getNodeValue(leftChild);
            int rightChildState = getNodeValue(rightChild);
            int nodeValue = (leftChildState + rightChildState) / 2;
            parameter.setParameterValueQuietly(nodeNum - tree.getExternalNodeCount(), nodeValue);
        }
        parameter.addBounds(bounds);
    }
