    public void init() {
        String file = getParameter("file");
        GraphSystem graphSystem = null;
        try {
            graphSystem = GraphSystemFactory.createGraphSystem("hypergraph.graph.GraphSystemImpl", null);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(8);
        }
        Graph graph = null;
        URL url = null;
        try {
            url = new URL(getCodeBase(), file);
            SAXReader reader = new SAXReader(graphSystem, url);
            ContentHandlerFactory ch = new ContentHandlerFactory();
            ch.setBaseUrl(getCodeBase());
            reader.setContentHandlerFactory(ch);
            graph = reader.parse();
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "Could not find file " + url.getFile() + ". \n" + "Start applet with default graph", "File not found", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception : " + fnfe);
            fnfe.printStackTrace(System.out);
        } catch (SAXException saxe) {
            JOptionPane.showMessageDialog(null, "Error while parsing file" + url.getFile() + ". \n" + "Exception : " + saxe + ". \n" + "Start applet with default graph", "Parsing error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception : " + saxe);
            saxe.getException().printStackTrace();
            saxe.printStackTrace(System.out);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "General error while reading file " + url + ". \n" + "Exception : " + e + ". \n" + "Start applet with default graph", "General error", JOptionPane.ERROR_MESSAGE);
            System.out.println(url);
            System.out.println("Exception : " + e);
            e.printStackTrace(System.out);
        }
        if (graph == null) {
            graph = GraphUtilities.createTree(graphSystem, 2, 3);
        }
        graphPanel = new GraphPanel(graph, this);
        file = getParameter("properties");
        if (file != null) try {
            url = new URL(getCodeBase(), file);
            graphPanel.loadProperties(url.openStream());
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "Could not find propertyfile " + url.getFile() + ". \n" + "Start applet with default properties", "File not found", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "General error while reading file " + url.getFile() + ". \n" + "Exception : " + e + ". \n" + "Start applet with default properties", "General error", JOptionPane.ERROR_MESSAGE);
            System.out.println(url);
            System.out.println("Exception : " + e);
            e.printStackTrace(System.out);
        }
        String center = getParameter("center");
        if (center != null) {
            Node n = (Node) graph.getElement(center);
            if (n != null) graphPanel.centerNode(n);
        }
        graphPanel.setLineRenderer(new ArrowLineRenderer());
        getContentPane().add(graphPanel);
    }
