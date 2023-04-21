    private void prepareCurrentMapConfiguration() {
        if (mapConfigurations != null && this.currentMapConfigurationIndex < mapConfigurations.size()) {
            MapConfiguration mapConfiguration = mapConfigurations.get(currentMapConfigurationIndex);
            if (mapConfiguration instanceof WMSConfiguration) {
                String layersAndStylesString = "";
                WMSConfiguration wmsConfiguration = (WMSConfiguration) mapConfiguration;
                if (wmsConfiguration.getLayers() != null && wmsConfiguration.getStyles() != null) {
                    String[] layers = wmsConfiguration.getLayers();
                    String[] styles = wmsConfiguration.getStyles();
                    if (layers.length == styles.length) {
                        layersAndStylesString += "&layers=";
                        int numLayers = layers.length;
                        for (int i = 0; i < numLayers; i++) {
                            layersAndStylesString += layers[i];
                            if (i < (numLayers - 1)) layersAndStylesString += ",";
                        }
                        layersAndStylesString += "&styles=";
                        for (int i = 0; i < numLayers; i++) {
                            layersAndStylesString += styles[i];
                            if (i < (numLayers - 1)) layersAndStylesString += ",";
                        }
                    }
                    wmsConfiguration.setLayersAndStylesString(layersAndStylesString);
                }
                metersPerUnit_CRS_X = 1.0;
                metersPerUnit_CRS_Y = 1.0;
                virtualToMapTransform.setIdentity();
                scaleSlider_maxValue = wmsConfiguration.getWMS_NUM_ZOOM_LEVELS() - 1;
                int scaleSliderValue = scaleSlider_maxValue / 2;
                if (Navigator.startConfig.getMapZoomLevel() != null) {
                    scaleSliderValue = Navigator.startConfig.getMapZoomLevel().intValue();
                    if (scaleSliderValue < scaleSlider_minValue) scaleSliderValue = scaleSlider_minValue;
                    if (scaleSliderValue > scaleSlider_maxValue) scaleSliderValue = scaleSlider_maxValue;
                }
                double scaleDenominator = wmsConfiguration.getWMS_minScaleDenominator() * Math.pow(2.0, scaleSliderValue);
                tileMatrix = new TileMatrix();
                tileMatrix.setIdentifier("" + scaleSliderValue);
                tileMatrix.setScaleDenominator(scaleDenominator);
                tileMatrix.setTopLeftPoint(new Point2d(WMS_TILEMATRIX_MIN_X, WMS_TILEMATRIX_MAX_Y));
                tileMatrix.setTileWidth(wmsConfiguration.getWMS_TILE_WIDTH());
                tileMatrix.setTileHeight(wmsConfiguration.getWMS_TILE_HEIGHT());
                tileMatrix.setMatrixWidth(Integer.MAX_VALUE);
                tileMatrix.setMatrixHeight(Integer.MAX_VALUE);
                initLabels(wmsConfiguration.getWMS_TILE_WIDTH(), wmsConfiguration.getWMS_TILE_HEIGHT());
                scaleSlider.setInverted(true);
                scaleSlider.setOrientation(0);
                scaleSlider.setBounds(00, 310, 205, 40);
                scaleSlider.setToolTipText("");
                scaleSlider.setMinimum(scaleSlider_minValue);
                scaleSlider.setMaximum(scaleSlider_maxValue);
            } else if (mapConfiguration instanceof WMTSConfiguration) {
                WMTSConfiguration wmtsConfiguration = (WMTSConfiguration) mapConfiguration;
                String serviceEndPoint = wmtsConfiguration.getServiceEndPoint().trim();
                String version = wmtsConfiguration.getVersion();
                if (!serviceEndPoint.endsWith("/")) serviceEndPoint += "/";
                String getCapabilitiesUrlString = serviceEndPoint + version + "/WMTSCapabilities.xml";
                System.out.println("WMTS getCapabilitiesUrlString " + getCapabilitiesUrlString);
                TileMatrixSet found_tileMatrixSet = null;
                URL url = null;
                try {
                    InputStream urlIn;
                    url = new URL(getCapabilitiesUrlString);
                    URLConnection urlc = url.openConnection();
                    urlc.setReadTimeout(Navigator.TIME_OUT);
                    urlIn = urlc.getInputStream();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(urlIn);
                    NodeList tileMatrixSet_NodeList = document.getElementsByTagName("TileMatrixSet");
                    NodeList tileMatrixSet_NodeList2 = document.getElementsByTagName("*");
                    NodeList tileMatrixSet_NodeList3 = document.getElementsByTagNameNS("*", "TileMatrixSet");
                    int numTileMatrixSets = tileMatrixSet_NodeList.getLength();
                    int numTileMatrixSets2 = tileMatrixSet_NodeList2.getLength();
                    int numTileMatrixSets3 = tileMatrixSet_NodeList3.getLength();
                    List<Node> tileMatrixSet_NodeList4 = new Vector<Node>();
                    for (int n = 0; n < numTileMatrixSets2; n++) {
                        Node node = tileMatrixSet_NodeList2.item(n);
                        if (nodeName(node).equals("TileMatrixSet")) {
                            tileMatrixSet_NodeList4.add(node);
                        }
                    }
                    int numTileMatrixSets4 = tileMatrixSet_NodeList4.size();
                    for (int a = 0; a < numTileMatrixSets4; a++) {
                        TileMatrixSet tileMatrixSet = new TileMatrixSet();
                        tileMatrixSet.setTileMatrixArray(new ArrayList<TileMatrix>());
                        Node TileMatrixSet_Node = tileMatrixSet_NodeList4.get(a);
                        NodeList TileMatrixSet_Children = TileMatrixSet_Node.getChildNodes();
                        for (int b = 0; b < TileMatrixSet_Children.getLength(); b++) {
                            Node node = TileMatrixSet_Children.item(b);
                            if (nodeName(node).equals("Identifier")) {
                                String identifier = node.getTextContent().trim();
                                tileMatrixSet.setIdentifier(identifier);
                                if (identifier.equals(wmtsConfiguration.getTileMatrixSetName())) {
                                    found_tileMatrixSet = tileMatrixSet;
                                } else {
                                    continue;
                                }
                            } else if (nodeName(node).equals("SupportedCRS")) {
                                String srid = node.getTextContent().trim();
                                tileMatrixSet.setSrid(srid);
                            } else if (nodeName(node).equals("TileMatrix")) {
                                TileMatrix tileMatrix = parseTileMatrix(node);
                                tileMatrixSet.getTileMatrixArray().add(tileMatrix);
                            }
                        }
                        Vector<SortItem> sortList = new Vector<SortItem>();
                        ArrayList<TileMatrix> tileMatrixArray = tileMatrixSet.getTileMatrixArray();
                        for (int s = 0; s < tileMatrixArray.size(); s++) {
                            TileMatrix tm = tileMatrixArray.get(s);
                            SortItem item = new SortItem((float) tm.getScaleDenominator(), tm, s);
                            sortList.add(item);
                        }
                        SortAlgorithm algorithm = (SortAlgorithm) Class.forName("de.i3mainz.sort.FastQSortAlgorithm4").newInstance();
                        algorithm.sort(sortList);
                        ArrayList<TileMatrix> reverse_tileMatrixArray = new ArrayList<TileMatrix>();
                        for (int s = tileMatrixArray.size() - 1; s >= 0; s--) {
                            reverse_tileMatrixArray.add(tileMatrixArray.get(s));
                        }
                        tileMatrixSet.setTileMatrixArray(reverse_tileMatrixArray);
                    }
                    urlIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                wmtsConfiguration.setTileMatrixSet(found_tileMatrixSet);
                metersPerUnit_CRS_X = 111111.1111;
                metersPerUnit_CRS_Y = 111111.1111;
                Transform3D scale_T3D = new Transform3D();
                scale_T3D.setScale(new Vector3d(1.0 / metersPerUnit_CRS_X, 1.0 / metersPerUnit_CRS_Y, 1.0));
                Transform3D trans_T3D = new Transform3D();
                trans_T3D.setTranslation(new Vector3d(-208145.399, -3318531.999, 0.0));
                Transform3D trans2_T3D = new Transform3D();
                trans2_T3D.setTranslation(new Vector3d(-90.024209, 29.962976, 0.0));
                virtualToMapTransform.set(trans2_T3D);
                virtualToMapTransform.mul(scale_T3D);
                virtualToMapTransform.mul(trans_T3D);
                if (found_tileMatrixSet != null) {
                    int numZoomLevels = found_tileMatrixSet.getTileMatrixArray().size();
                    scaleSlider_minValue = 0;
                    scaleSlider_maxValue = numZoomLevels - 1;
                    int value = scaleSlider.getValue();
                    if (value < scaleSlider_minValue) scaleSlider.setMinimum(scaleSlider_minValue);
                    if (value > scaleSlider_maxValue) scaleSlider.setValue(scaleSlider_maxValue);
                    tileMatrix = found_tileMatrixSet.getTileMatrixArray().get(scaleSlider.getValue());
                    initLabels(tileMatrix.getTileWidth(), tileMatrix.getTileHeight());
                    scaleSlider.setInverted(true);
                    scaleSlider.setOrientation(0);
                    scaleSlider.setBounds(00, 310, 205, 40);
                    scaleSlider.setToolTipText("");
                    scaleSlider.setMinimum(scaleSlider_minValue);
                    scaleSlider.setMaximum(scaleSlider_maxValue);
                } else {
                    System.out.println("MapPanel.prepareMapConfiguration: TileMatrixSet " + wmtsConfiguration.getTileMatrixSetName() + " not found in WMTS");
                }
            }
        }
    }
