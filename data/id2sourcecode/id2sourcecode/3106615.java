    private void writeInvocations(PrintWriter pw) throws GraphException {
        Collection<Node> nextNodes = getNextNodes();
        while (nextNodes.size() > 0) {
            boolean thread = (nextNodes.size() > 1);
            for (Node node : nextNodes) {
                if (node instanceof WSNode) {
                    WSNode wsNode = (WSNode) node;
                    writeInvocation(wsNode, thread, pw);
                } else {
                }
                this.notYetInvokedNodes.remove(node);
            }
            nextNodes = getNextNodes();
        }
    }
