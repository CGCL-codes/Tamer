    @Override
    public Long importGraphUrl(Long graphId, String url) {
        ONDEXGraph graph = ONDEXGraphRegistry.graphs.get(graphId);
        try {
            read(graph, new InputStreamReader(new URL(url).openStream()));
            databases.get(graph.getSID()).commit();
        } catch (IOException e) {
            logger.error("Error importing into graph '" + graph.getName() + "'", e);
        } catch (XMLStreamException e) {
            logger.error("Error importing into graph '" + graph.getName() + "'", e);
        } catch (ClassNotFoundException e) {
            logger.error("Error importing into graph '" + graph.getName() + "'", e);
        }
        return new WSGraph(graph).getId();
    }
