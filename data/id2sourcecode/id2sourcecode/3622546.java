    private GraphInterface<Index> analyzeFile(InputStream in, AbstractExecution progress, double percentage, int Linesinfile) {
        GraphInterface<Index> graph = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ASRelationShipStructure relationstruct = new ASRelationShipStructure();
        int res = fillASRelationsList(relationstruct, reader, Linesinfile);
        if (res != -1) {
            graph = new GraphAsHashMap<Index>();
        } else {
            cleanClose(reader, "Vertices Could not be read from File or File is empty, exiting the program.");
            graph = null;
        }
        updateLoadProgress(progress, percentage);
        res = writeVerticesToGraph(relationstruct, graph);
        if (res == -1) {
            cleanClose(reader, "Could not write vertices to graph properly, exiting the program.");
            graph = null;
        }
        updateLoadProgress(progress, percentage);
        try {
            if (reader != null) reader.close();
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem("An exception has occured while closing BufferedReader.\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "analyzeFile", ex);
            graph = null;
        }
        return graph;
    }
