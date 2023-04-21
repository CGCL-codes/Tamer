    @Override
    public void saveDocumentAs(URL url) {
        XmlDataAdaptor da = XmlDataAdaptor.newEmptyDocumentAdaptor();
        XmlDataAdaptor scalarpvviewerData_Adaptor = da.createChild(dataRootName);
        scalarpvviewerData_Adaptor.setValue("title", url.getFile());
        XmlDataAdaptor params_font = scalarpvviewerData_Adaptor.createChild("font");
        params_font.setValue("name", globalFont.getFamily());
        params_font.setValue("style", globalFont.getStyle());
        params_font.setValue("size", globalFont.getSize());
        XmlDataAdaptor params_pts = scalarpvviewerData_Adaptor.createChild("Panels_titles");
        params_pts.setValue("values_panel_title", viewValuesPanel.getTitle());
        params_pts.setValue("charts_panel_title", viewChartsPanel.getTitle());
        XmlDataAdaptor params_data = scalarpvviewerData_Adaptor.createChild("PARAMETERS");
        params_data.setValue("lastMemorizingTime", viewValuesPanel.getLastMemorizingTime());
        XmlDataAdaptor params_uc = scalarpvviewerData_Adaptor.createChild("UpdateController");
        params_uc.setValue("updateTime", updatingController.getUpdateTime());
        params_uc.setValue("ChartUpdateTime", viewChartsPanel.getTimeStep());
        params_uc.setValue("listenToEPICS", viewValuesPanel.listenModeOn());
        params_uc.setValue("recordChartFromEPICS", viewChartsPanel.recordOn());
        int nPVs = spvs.getSize();
        for (int i = 0; i < nPVs; i++) {
            ScalarPV pv = spvs.getScalarPV(i);
            XmlDataAdaptor pvDA = scalarpvviewerData_Adaptor.createChild("ScalarPV");
            pvDA.setValue("pvName", pv.getMonitoredPV().getChannelName());
            pvDA.setValue("referenceValue", pv.getRefValue());
            pvDA.setValue("value", pv.getValue());
            pvDA.setValue("showValueChart", pv.showValueChart());
            pvDA.setValue("showRefChart", pv.showRefChart());
            pvDA.setValue("showDifChart", pv.showDifChart());
            pvDA.setValue("showDif", pv.showDif());
            pvDA.setValue("showValue", pv.showValue());
            pvDA.setValue("showRef", pv.showRef());
        }
        try {
            StringWriter strW = new StringWriter();
            scalarpvviewerData_Adaptor.writeTo(strW);
            BufferedWriter out = new BufferedWriter(new FileWriter(url.getFile()));
            out.write(strW.toString());
            spvs.writeChart(out);
            setHasChanges(true);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException e=" + e);
        }
    }
