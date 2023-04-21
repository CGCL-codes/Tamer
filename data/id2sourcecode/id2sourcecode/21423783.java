    private void initGUI() {
        this.setContentPane(getJContentPane());
        showTM3CTonf = "true".equalsIgnoreCase(getParameter("showTM3Conf"));
        showTree = "true".equalsIgnoreCase(getParameter("viewTree"));
        showWeight = "true".equalsIgnoreCase(getParameter("showWeight"));
        weightPrefix = getParameter("weightPrefix");
        valuePrefix = getParameter("valuePrefix");
        if (showTM3CTonf) {
            addPanelEast(getJContentPane());
        }
        final String dataFile = getParameter("dataFile");
        final String dataFileType = getParameter("dataFileType");
        TreeMapNode root = null;
        if (TM3.equalsIgnoreCase(dataFileType)) {
            try {
                builderTM3 = new BuilderTM3(createReader(dataFile));
                root = builderTM3.getRoot();
                if (showTM3CTonf) {
                    setTM3Fields();
                    panelTM3.setVisible(true);
                }
            } catch (final IOException e) {
                root = handleException(e);
            }
        } else if (XML.equalsIgnoreCase(dataFileType)) {
            try {
                final URL url = new URL(getCodeBase(), dataFile);
                final URLConnection connection = url.openConnection();
                final BuilderXML bXml = new BuilderXML(connection.getInputStream());
                root = bXml.getRoot();
            } catch (final ParseException e) {
                root = handleException(e);
            } catch (final MalformedURLException e) {
                root = handleException(e);
            } catch (final IOException e) {
                root = handleException(e);
            }
        } else {
            root = DemoUtil.buildDemoRoot();
        }
        this.jTreeMap = new JTreeMap(root, new SplitBySortedWeight(), treeView, weightPrefix, valuePrefix, showWeight);
        this.jTreeMap.setFont(new Font(null, Font.BOLD, DEFAULT_FONT_SIZE));
        final String colourProvider = getParameter("colorProvider");
        ColorProvider colourProviderInstance = null;
        if ("Random".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new RandomColorProvider(this.jTreeMap);
        } else if ("HSBLinear".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new HSBTreeMapColorProvider(jTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Linear, Color.GREEN, Color.RED);
        } else if ("HSBLog".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new HSBTreeMapColorProvider(jTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Log, Color.GREEN, Color.RED);
        } else if ("HSBSquareRoot".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new RandomColorProvider(this.jTreeMap);
        } else if ("HSBCubicRoot".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new HSBTreeMapColorProvider(jTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.CubicRoot, Color.GREEN, Color.RED);
        } else if ("HSBExp".equalsIgnoreCase(colourProvider)) {
            colourProviderInstance = new HSBTreeMapColorProvider(jTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Exp, Color.GREEN, Color.RED);
        }
        if (colourProviderInstance == null) {
            colourProviderInstance = new RedGreenColorProvider(this.jTreeMap);
        }
        this.jTreeMap.setColorProvider(colourProviderInstance);
        new ZoomPopupMenu(this.jTreeMap, true);
        if (showTree) {
            final JSplitPane splitPaneCenter = new JSplitPane();
            splitPaneCenter.setBorder(BorderFactory.createEmptyBorder());
            getJContentPane().add(splitPaneCenter, BorderLayout.CENTER);
            final JScrollPane jScrollPane1 = new JScrollPane();
            splitPaneCenter.setTopComponent(jScrollPane1);
            splitPaneCenter.setBottomComponent(this.jTreeMap);
            treeModel = new DefaultTreeModel(root);
            treeView = new JTree(this.treeModel);
            jTreeMap.setTreeView(treeView);
            jScrollPane1.getViewport().add(this.treeView);
            jScrollPane1.setPreferredSize(new Dimension(SCROLLPANE_WIDTH, jTreeMap.getRoot().getHeight()));
            treeView.addTreeSelectionListener(new TreeSelectionListener() {

                public void valueChanged(final TreeSelectionEvent e) {
                    TreeMapNode dest = (TreeMapNode) JTreeMapAppletExample.this.treeView.getLastSelectedPathComponent();
                    if (dest != null && dest.isLeaf()) {
                        dest = (TreeMapNode) dest.getParent();
                    }
                    if (dest == null) {
                        return;
                    }
                    JTreeMapAppletExample.this.jTreeMap.zoom(dest);
                    JTreeMapAppletExample.this.jTreeMap.repaint();
                }
            });
        } else {
            getJContentPane().add(this.jTreeMap, BorderLayout.CENTER);
        }
    }
