    public static void read(BufferedReader reader, WriterDataLayoutDISP writer) {
        while (writer.options.graph.getNumberOfGroups() > 1) {
            writer.options.graph.removeGroup(1);
        }
        try {
            Group currentGroup = null;
            String line = null;
            GraphData graph = writer.graph;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0 && line.charAt(0) == '\t') {
                    String name = line.substring(1, line.length());
                    GraphVertex currentVertex = graph.getVertexByName(name);
                    if (currentVertex != null) {
                        currentGroup.addNode(currentVertex);
                    }
                } else {
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    currentGroup = new Group(st.nextToken(), writer.graph);
                    currentGroup.setColor(Colors.valueOfUpper(st.nextToken()).get());
                    currentGroup.visible = (Boolean.valueOf(st.nextToken())).booleanValue();
                    currentGroup.info = (Boolean.valueOf(st.nextToken())).booleanValue();
                    writer.options.graph.addGroup(currentGroup);
                }
            }
        } catch (Exception e) {
            System.err.println("Exception while reading (ReaderWriterGroup.read): ");
            System.err.println(e);
        }
    }
