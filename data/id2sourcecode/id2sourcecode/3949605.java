    private static void handleNetworkInputOperators(FlowGraph graph, OperatorCore operator) throws IOException {
        for (InputPort inPort : operator.getInputPorts()) {
            if (inPort.hasIncomingArc()) {
                continue;
            }
            OperatorCore inputOperator = ClusteringOperatorFactory.createNetworkReaderOperator();
            graph.addOperator(inputOperator);
            Arc newArc = new Arc(inputOperator.getOutputPorts().get(0), inPort);
            graph.addArc(newArc);
            ServerSocket socket = OhuaServerSocketFactory.getInstance().createServerSocket(1112);
            socket.getChannel().register(LocalConnectionManager.getInstance().getGlobalSelector(), SelectionKey.OP_ACCEPT);
        }
    }
