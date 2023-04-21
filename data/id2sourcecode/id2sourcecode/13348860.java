    public void dumpData(XmlDataAdaptor da) {
        da.setValue("name", name);
        da.setValue("isActive", isActive.booleanValue());
        da.setValue("itIsTrim", itIsTrim);
        da.setValue("currentPV", wrpChCurrent.getChannelName());
        da.setValue("fieldRbPV", wrpChRBField.getChannelName());
        da.setValue("current", current);
        da.setValue("field", field);
        XmlDataAdaptor coefSetDA = (XmlDataAdaptor) da.createChild("COEFFICIENT_SET");
        Iterator itr = coefXmap.keySet().iterator();
        while (itr.hasNext()) {
            BPM_Element bpmElm = (BPM_Element) itr.next();
            double x = ((Double) coefXmap.get(bpmElm)).doubleValue();
            double y = ((Double) coefYmap.get(bpmElm)).doubleValue();
            double x_err = ((Double) coefErrXmap.get(bpmElm)).doubleValue();
            double y_err = ((Double) coefErrYmap.get(bpmElm)).doubleValue();
            XmlDataAdaptor coefDA = (XmlDataAdaptor) coefSetDA.createChild("COEFFICIENTS");
            coefDA.setValue("bpm", bpmElm.getName());
            coefDA.setValue("dx_di", numberFormat.format(x));
            coefDA.setValue("dy_di", numberFormat.format(y));
            coefDA.setValue("dx_di_err", numberFormat.format(x_err));
            coefDA.setValue("dy_di_err", numberFormat.format(y_err));
        }
        XmlDataAdaptor offSetsDA = (XmlDataAdaptor) da.createChild("OFFSET_VALUES");
        itr = offsetXmap.keySet().iterator();
        while (itr.hasNext()) {
            BPM_Element bpmElm = (BPM_Element) itr.next();
            double x = ((Double) offsetXmap.get(bpmElm)).doubleValue();
            double y = ((Double) offsetYmap.get(bpmElm)).doubleValue();
            double ratioX = ((Double) ratioXmap.get(bpmElm)).doubleValue();
            double ratioY = ((Double) ratioYmap.get(bpmElm)).doubleValue();
            double trM01X = ((Double) trM01Xmap.get(bpmElm)).doubleValue();
            double trM23Y = ((Double) trM23Ymap.get(bpmElm)).doubleValue();
            XmlDataAdaptor offsetDA = (XmlDataAdaptor) offSetsDA.createChild("OFFSETs");
            offsetDA.setValue("bpm", bpmElm.getName());
            offsetDA.setValue("x", numberFormat.format(x));
            offsetDA.setValue("y", numberFormat.format(y));
            offsetDA.setValue("rX", numberFormat.format(ratioX));
            offsetDA.setValue("rY", numberFormat.format(ratioY));
            offsetDA.setValue("m01", numberFormat.format(trM01X));
            offsetDA.setValue("m23", numberFormat.format(trM23Y));
        }
        XmlDataAdaptor measureSetDA = (XmlDataAdaptor) da.createChild("MEASUREMENT_SET");
        itr = quadMeasurementsV.iterator();
        while (itr.hasNext()) {
            QuadMeasure quadMeasure = (QuadMeasure) itr.next();
            XmlDataAdaptor measureDA = (XmlDataAdaptor) measureSetDA.createChild("MEASURE");
            quadMeasure.dumpData(measureDA);
        }
    }
