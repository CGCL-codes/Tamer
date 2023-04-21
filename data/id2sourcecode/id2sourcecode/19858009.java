    public GetCapabilitiesPanel2() {
        super();
        JPanel ServerPanel = new JPanel();
        this.getVerticalScrollBar().setUnitIncrement(20);
        ServerPanel.setBackground(new java.awt.Color(255, 255, 255));
        ServerPanel.setLayout(null);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Root");
        SAXTreeBuilder saxTree = new SAXTreeBuilder(top);
        InputStream urlIn = null;
        for (int w = 0; w < Navigator.web3DServices.length; w++) {
            Web3DService web3DService = Navigator.web3DServices[w];
            try {
                SAXParser saxParser = new SAXParser();
                saxParser.setContentHandler(saxTree);
                String request = web3DService.getServiceEndPoint() + "?" + "SERVICE=" + web3DService.getService() + "&" + "REQUEST=GetCapabilities&" + "ACCEPTFORMATS=text/xml&" + "ACCEPTVERSIONS=";
                for (int i = 0; i < web3DService.getAcceptVersions().length; i++) {
                    if (i > 0) request += ",";
                    request += web3DService.getAcceptVersions()[i];
                }
                System.out.println(request);
                URL url = new URL(request);
                URLConnection urlc = url.openConnection();
                urlc.setReadTimeout(Navigator.TIME_OUT);
                if (web3DService.getEncoding() != null) {
                    urlc.setRequestProperty("Authorization", "Basic " + web3DService.getEncoding());
                }
                urlIn = urlc.getInputStream();
                saxParser.parse(new InputSource(urlIn));
            } catch (Exception ex) {
                top.add(new DefaultMutableTreeNode(ex.getMessage()));
            }
        }
        try {
            urlIn.close();
        } catch (Exception e) {
        }
        JTree tree = new JTree(saxTree.getTree());
        ClassLoader cl = this.getClass().getClassLoader();
        ImageIcon leafIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(cl.getResource("resources/leaficon.png")));
        ImageIcon openIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(cl.getResource("resources/openicon.png")));
        ImageIcon closedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(cl.getResource("resources/closedicon.png")));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closedIcon);
        tree.setCellRenderer(renderer);
        this.setViewportView(tree);
        expandAll(tree);
    }
