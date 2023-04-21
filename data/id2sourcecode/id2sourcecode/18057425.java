    public void readArrayPVViewerDocument(URL url) {
        XmlDataAdaptor readAdp = null;
        readAdp = XmlDataAdaptor.adaptorForUrl(url, false);
        if (readAdp != null) {
            XmlDataAdaptor arrViewerData_Adaptor = readAdp.childAdaptor(dataRootName);
            if (arrViewerData_Adaptor != null) {
                cleanUp();
                setTitle(arrViewerData_Adaptor.stringValue("title"));
                XmlDataAdaptor params_font = arrViewerData_Adaptor.childAdaptor("font");
                int font_size = params_font.intValue("size");
                int style = params_font.intValue("style");
                String font_Family = params_font.stringValue("name");
                globalFont = new Font(font_Family, style, font_size);
                fontSize_PrefPanel_Spinner.setValue(new Integer(font_size));
                setFontForAll(globalFont);
                XmlDataAdaptor params_DA = arrViewerData_Adaptor.childAdaptor("PARAMS");
                boolean autoUpdateOn = params_DA.booleanValue("AutoUpdate");
                int frequency = params_DA.intValue("Frequency");
                freq_ViewPanel_Spinner.setValue(new Integer(frequency));
                autoUpdateView_Button.setSelected(false);
                XmlDataAdaptor xPosPanelDA = arrViewerData_Adaptor.childAdaptor("ARRAY_PVS_PANEL");
                arrayPVsGraphPanel.setConfig(xPosPanelDA);
                XmlDataAdaptor arrayPVsDA = arrViewerData_Adaptor.childAdaptor("ARRAY_PVs");
                java.util.Iterator<XmlDataAdaptor> da_iter = arrayPVsDA.childAdaptorIterator();
                while (da_iter.hasNext()) {
                    XmlDataAdaptor g_DA = da_iter.next();
                    ArrayViewerPV arrPV = new ArrayViewerPV(arrayPVGraphs);
                    arrPV.setConfig(g_DA);
                    arrayPVs.add(arrPV);
                    updatingController.addArrayDataPV(arrPV.getArrayDataPV());
                }
                for (int i = 0, n = arrayPVs.size(); i < n; i++) {
                    ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(i);
                    PVTreeNode pvNodeNew = new PVTreeNode(arrPV.getChannelName());
                    pvNodeNew.setChannel(arrPV.getChannel());
                    pvNodeNew.setAsPVName(true);
                    pvNodeNew.setCheckBoxVisible(true);
                    rootArrayPV_Node.add(pvNodeNew);
                    pvNodeNew.setSwitchedOn(arrPV.getArrayDataPV().getSwitchOn());
                    pvNodeNew.setSwitchedOnOffListener(switchPVTreeListener);
                    pvNodeNew.setCreateRemoveListener(createDeletePVTreeListener);
                    pvNodeNew.setRenameListener(renamePVTreeListener);
                }
                ((DefaultTreeModel) pvsSelector.getPVsTreePanel().getJTree().getModel()).reload();
                ((DefaultTreeModel) pvsTreePanelView.getJTree().getModel()).reload();
                setColors(rootArrayPV_Node, -1);
                updateGraphPanel();
                autoUpdateView_Button.setSelected(autoUpdateOn);
            }
        }
    }
