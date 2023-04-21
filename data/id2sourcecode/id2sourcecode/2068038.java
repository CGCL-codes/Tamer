    private int readVertices(int numOfVertices, BufferedReader reader, GraphInterface<Index> graph) {
        FastMap<String, Pair<String, String>> m_lastInfo = new FastMap<String, Pair<String, String>>();
        String line = null;
        try {
            for (int i = 0; i < numOfVertices && ((line = reader.readLine()) != null); i++) {
                Matcher verticesLine = GraphRegularExpressions.VERTICES_LINE.matcher(line);
                if (verticesLine.find()) {
                    int vertexNum = Integer.parseInt(verticesLine.group(1));
                    String label = verticesLine.group(2);
                    double x = Double.parseDouble(verticesLine.group(3));
                    double y = Double.parseDouble(verticesLine.group(4));
                    double z = Double.parseDouble(verticesLine.group(5));
                    FastMap<String, Pair<String, String>> info = new FastMap<String, Pair<String, String>>();
                    int infoIndex = line.indexOf(verticesLine.group(5)) + verticesLine.group(5).length() + 1;
                    if (line.length() > infoIndex) {
                        String infoStr = line.substring(infoIndex);
                        Matcher vertexInfo = GraphRegularExpressions.OPTIONAL_INFO.matcher(infoStr);
                        while (vertexInfo.find()) {
                            String labelName = vertexInfo.group(1);
                            String labelValue = vertexInfo.group(2);
                            if (labelName != null) info.put(labelName.trim().toLowerCase(), new Pair<String, String>(labelName.trim(), labelValue));
                        }
                    }
                    m_lastInfo.putAll(info);
                    info.putAll(m_lastInfo);
                    VertexInfo vInfo = new VertexInfo(vertexNum, label, x, y, z, info);
                    graph.addVertex(Index.valueOf(vertexNum - 1), vInfo);
                } else {
                    verticesLine = GraphRegularExpressions.VERTICES_LINE_VERSION_2.matcher(line);
                    if (verticesLine.find()) {
                        int vertexNum = Integer.parseInt(verticesLine.group(1));
                        VertexInfo vInfo = new VertexInfo(vertexNum, "", 0, 0, 0, new FastMap<String, Pair<String, String>>());
                        graph.addVertex(Index.valueOf(vertexNum - 1), vInfo);
                    }
                }
            }
            return 1;
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "readVertices", ex);
            return -1;
        }
    }
