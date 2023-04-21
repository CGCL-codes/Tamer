    public void readBpmViewerDocument(URL url) {
        XmlDataAdaptor readAdp = null;
        readAdp = XmlDataAdaptor.adaptorForUrl(url, false);
        if (readAdp != null) {
            XmlDataAdaptor bpmViewerData_Adaptor = readAdp.childAdaptor(dataRootName);
            if (bpmViewerData_Adaptor != null) {
                cleanUp();
                setTitle(bpmViewerData_Adaptor.stringValue("title"));
                XmlDataAdaptor params_font = bpmViewerData_Adaptor.childAdaptor("font");
                int font_size = params_font.intValue("size");
                int style = params_font.intValue("style");
                String font_Family = params_font.stringValue("name");
                globalFont = new Font(font_Family, style, font_size);
                fontSize_PrefPanel_Spinner.setValue(new Integer(font_size));
                setFontForAll(globalFont);
                XmlDataAdaptor params_DA = bpmViewerData_Adaptor.childAdaptor("PARAMS");
                boolean autoUpdateOn = params_DA.booleanValue("AutoUpdate");
                int frequency = params_DA.intValue("Frequency");
                freq_ViewPanel_Spinner.setValue(new Integer(frequency));
                autoUpdateView_Button.setSelected(false);
                XmlDataAdaptor phasePanelDA = bpmViewerData_Adaptor.childAdaptor("PHASE_PANEL");
                XmlDataAdaptor xPosPanelDA = bpmViewerData_Adaptor.childAdaptor("X_POSITION_PANEL");
                XmlDataAdaptor yPosPanelDA = bpmViewerData_Adaptor.childAdaptor("Y_POSITION_PANEL");
                phaseGraphPanel.setConfig(phasePanelDA);
                xPosGraphPanel.setConfig(xPosPanelDA);
                yPosGraphPanel.setConfig(yPosPanelDA);
                XmlDataAdaptor phasePVsDA = bpmViewerData_Adaptor.childAdaptor("PHASE_PVs");
                XmlDataAdaptor xPosPVsDA = bpmViewerData_Adaptor.childAdaptor("X_POS_PVs");
                XmlDataAdaptor yPosPVsDA = bpmViewerData_Adaptor.childAdaptor("Y_POS_PVs");
                java.util.Iterator<XmlDataAdaptor> da_iter = phasePVsDA.childAdaptorIterator();
                while (da_iter.hasNext()) {
                    XmlDataAdaptor g_DA = da_iter.next();
                    BpmViewerPV bpmPV = new BpmViewerPV(phaseGraphs);
                    bpmPV.setConfig(g_DA);
                    phasePVs.add(bpmPV);
                    updatingController.addArrayDataPV(bpmPV.getArrayDataPV());
                }
                da_iter = xPosPVsDA.childAdaptorIterator();
                while (da_iter.hasNext()) {
                    XmlDataAdaptor g_DA = da_iter.next();
                    BpmViewerPV bpmPV = new BpmViewerPV(xPosGraphs);
                    bpmPV.setConfig(g_DA);
                    xPosPVs.add(bpmPV);
                    updatingController.addArrayDataPV(bpmPV.getArrayDataPV());
                }
                da_iter = yPosPVsDA.childAdaptorIterator();
                while (da_iter.hasNext()) {
                    XmlDataAdaptor g_DA = da_iter.next();
                    BpmViewerPV bpmPV = new BpmViewerPV(yPosGraphs);
                    bpmPV.setConfig(g_DA);
                    yPosPVs.add(bpmPV);
                    updatingController.addArrayDataPV(bpmPV.getArrayDataPV());
                }
                for (int i = 0, n = phasePVs.size(); i < n; i++) {
                    BpmViewerPV bpmPV = (BpmViewerPV) phasePVs.get(i);
                    PVTreeNode pvNodeNew = new PVTreeNode(bpmPV.getChannelName());
                    pvNodeNew.setChannel(bpmPV.getChannel());
                    pvNodeNew.setAsPVName(true);
                    pvNodeNew.setCheckBoxVisible(true);
                    rootPhasePV_Node.add(pvNodeNew);
                    pvNodeNew.setSwitchedOn(bpmPV.getArrayDataPV().getSwitchOn());
                    pvNodeNew.setSwitchedOnOffListener(switchPVTreeListener);
                    pvNodeNew.setCreateRemoveListener(createDeletePVTreeListener);
                    pvNodeNew.setRenameListener(renamePVTreeListener);
                }
                for (int i = 0, n = xPosPVs.size(); i < n; i++) {
                    BpmViewerPV bpmPV = (BpmViewerPV) xPosPVs.get(i);
                    PVTreeNode pvNodeNew = new PVTreeNode(bpmPV.getChannelName());
                    pvNodeNew.setChannel(bpmPV.getChannel());
                    pvNodeNew.setAsPVName(true);
                    pvNodeNew.setCheckBoxVisible(true);
                    rootXposPV_Node.add(pvNodeNew);
                    pvNodeNew.setSwitchedOn(bpmPV.getArrayDataPV().getSwitchOn());
                    pvNodeNew.setSwitchedOnOffListener(switchPVTreeListener);
                    pvNodeNew.setCreateRemoveListener(createDeletePVTreeListener);
                    pvNodeNew.setRenameListener(renamePVTreeListener);
                }
                for (int i = 0, n = yPosPVs.size(); i < n; i++) {
                    BpmViewerPV bpmPV = (BpmViewerPV) yPosPVs.get(i);
                    PVTreeNode pvNodeNew = new PVTreeNode(bpmPV.getChannelName());
                    pvNodeNew.setChannel(bpmPV.getChannel());
                    pvNodeNew.setAsPVName(true);
                    pvNodeNew.setCheckBoxVisible(true);
                    rootYposPV_Node.add(pvNodeNew);
                    pvNodeNew.setSwitchedOn(bpmPV.getArrayDataPV().getSwitchOn());
                    pvNodeNew.setSwitchedOnOffListener(switchPVTreeListener);
                    pvNodeNew.setCreateRemoveListener(createDeletePVTreeListener);
                    pvNodeNew.setRenameListener(renamePVTreeListener);
                }
                ((DefaultTreeModel) pvsSelector.getPVsTreePanel().getJTree().getModel()).reload();
                ((DefaultTreeModel) pvsTreePanelView.getJTree().getModel()).reload();
                setColors(rootPhasePV_Node, -1);
                setColors(rootXposPV_Node, -1);
                setColors(rootYposPV_Node, -1);
                updateGraphPanels();
                autoUpdateView_Button.setSelected(autoUpdateOn);
            }
        }
    }
