    private int readEdges(BufferedReader reader, GraphInterface<Index> graph) {
        FastMap<String, Pair<String, String>> m_lastInfo = new FastMap<String, Pair<String, String>>();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                Matcher edgesLine = GraphRegularExpressions.EDGE_LINE.matcher(line);
                if (edgesLine.find()) {
                    FastMap<String, Pair<String, String>> info = new FastMap<String, Pair<String, String>>();
                    int infoIndex = line.lastIndexOf(edgesLine.group(3)) + edgesLine.group(3).length() + 1;
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
                    graph.addEdge(Index.valueOf(Integer.parseInt(edgesLine.group(1)) - 1), Index.valueOf(Integer.parseInt(edgesLine.group(2)) - 1), new EdgeInfo(Double.parseDouble(edgesLine.group(3)), info));
                }
            }
            return 1;
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "readEdges", ex);
            return -1;
        }
    }
