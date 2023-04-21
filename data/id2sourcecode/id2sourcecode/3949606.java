    public static void doOutputRewrite(FlowGraph graph, Set<SerializableNetworkArc> outgoingNetworkArcs) throws UnknownHostException, IOException {
        for (SerializableNetworkArc outgoingNetworkArc : outgoingNetworkArcs) {
            OperatorCore outputOperator = ClusteringOperatorFactory.createNetworkWriterOperator();
            graph.addOperator(outputOperator);
            Arc newArc = new Arc((OutputPort) graph.getPortByID(outgoingNetworkArc._sourcePort), outputOperator.getInputPorts().get(0));
            graph.addArc(newArc);
            Socket socket = OhuaSocketFactory.getInstance().createSocket(outgoingNetworkArc._ip, outgoingNetworkArc._remotePort);
            socket.getChannel().register(LocalConnectionManager.getInstance().getGlobalSelector(), SelectionKey.OP_CONNECT);
        }
    }
