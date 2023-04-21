    public void run() {
        log.debug("begin Data Extraction");
        VisualizerFrame vf = (VisualizerFrame) refBase.getXplorePanel().getGraphicalViewer();
        vf.showWaitPanel("Data Exctraction ...");
        extractionList.clear();
        GeneralPath gp = null;
        if (this.selectionType == DataExtraction.E_RECTANGLE) {
            gp = new GeneralPath(rect.getShape());
        } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
            gp = new GeneralPath(ellipse.getShape());
        } else if (this.selectionType == DataExtraction.E_POLY) {
            gp = new GeneralPath(poly.getShape());
        }
        if (gp == null) throw new IllegalArgumentException("Extraction failed : No general path created.");
        updateScreenList();
        log.debug("Size of dataList : " + dataList.size());
        log.debug("Size of screenList : " + screenList.size());
        for (int i = 0; i < screenList.size(); i++) {
            Point scp = screenList.get(i);
            if (gp.contains(scp) == true) {
                extractionList.add(dataList.get(i));
                log.debug("add  " + dataList.get(i));
            } else log.debug("not contained : " + dataList.get(i));
        }
        IDataNode dn = refBase.getXplorePanel().getSourceNode().getMethod();
        IDataGrid sourceGrid = (IDataGrid) dn.getOutput(IDataGrid.class);
        ArrayList<Integer> indicesList = new ArrayList<Integer>();
        for (Point2D ep : extractionList) {
            int[] attributeNo = ((ScatterPlot2D) refBase).getPlotDataVariables();
            int att1 = attributeNo[0];
            int att2 = attributeNo[1];
            Vector<Integer> canditatesX = sourceGrid.getRowIndex(att1, ep.getX());
            Vector<Integer> canditatesY = sourceGrid.getRowIndex(att2, ep.getY());
            for (Integer cx : canditatesX) {
                if (canditatesY.contains(cx) == true) {
                    indicesList.add(cx);
                }
            }
        }
        StringBuffer iStb = new StringBuffer();
        int iSize = indicesList.size();
        if (iSize >= 1) {
            iStb.append(indicesList.get(0));
        }
        for (int i = 1; i < iSize; i++) {
            Integer ix = indicesList.get(i);
            iStb.append("," + ix);
        }
        vf.textFlushToWaitPanel("Indices to Subselector...");
        String indices = iStb.toString();
        DataNavigation nav = refBase.getXplorePanel().getExpertice().getDataNavigation();
        if (selection == null) {
            selection = ExtensionFactory.createDataNode("vademecum.data.subsetSelector@subsetSelector");
            nav.addNode(refBase.getXplorePanel().getSourceNode(), selection);
        }
        selection.getMethod().setProperty("selection", indices);
        selection.setState(ExperimentNode.READY);
        log.debug("finished with extraction");
        vf.hideWaitPanel();
        dataList = null;
        screenList = null;
    }
