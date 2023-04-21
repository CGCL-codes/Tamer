    public StyleEditionFrame(LayerLegendPanel layerLegendPanel) {
        this.layerLegendPanel = layerLegendPanel;
        this.layerViewPanel = this.layerLegendPanel.getLayerViewPanel();
        this.graphicStylePanel = new JPanel();
        if (layerLegendPanel.getSelectedLayers().size() == 1) {
            this.layer = layerLegendPanel.getSelectedLayers().iterator().next();
        }
        DataSet dataset = layerLegendPanel.getLayerViewPanel().getProjectFrame().getDataSet();
        this.setInitialSLD(new StyledLayerDescriptor(dataset));
        CharArrayWriter writer = new CharArrayWriter();
        layerLegendPanel.getModel().marshall(writer);
        Reader reader = new CharArrayReader(writer.toCharArray());
        this.setInitialSLD(StyledLayerDescriptor.unmarshall(reader));
        this.getInitialSLD().setDataSet(dataset);
        if (this.layer.getSymbolizer().isPolygonSymbolizer()) {
            this.initPolygon();
        } else if (this.layer.getSymbolizer().isLineSymbolizer()) {
            this.initLine();
        } else if (this.layer.getSymbolizer().isPointSymbolizer()) {
            this.initPoint();
        }
        this.textStylePanel = new JPanel();
        this.textStylePanel.setLayout(new BoxLayout(this.textStylePanel, BoxLayout.Y_AXIS));
        this.initTextStylePanel();
        this.tabPane = new JTabbedPane();
        this.tabPane.add(I18N.getString("StyleEditionFrame.Symbology"), this.graphicStylePanel);
        this.tabPane.add(I18N.getString("StyleEditionFrame.Toponyms"), this.textStylePanel);
        this.add(this.tabPane);
        this.setTitle(I18N.getString("StyleEditionFrame.StyleEdition"));
        this.pack();
        this.pack();
        this.textStylePanel.setSize(600, 500);
        this.graphicStylePanel.setSize(600, 700);
        this.setSize(650, 750);
        this.setLocation(200, 200);
        this.setAlwaysOnTop(true);
    }
