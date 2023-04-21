    private void endGraph() throws IOException {
        if (prev_state != null) addNode(prev_state);
        if (format == GDF_FORMAT) {
            graph.write("edgedef>node1,node2,label,labelvisible,directed,thread INT");
            graph.newLine();
            int size = gdfEdges.size();
            for (int i = 0; i < size; i++) {
                graph.write(gdfEdges.get(i));
                graph.newLine();
            }
        } else {
            graph.write("}");
            graph.newLine();
        }
        graph.close();
    }
