    public void dumpConfig(XmlDataAdaptor da) {
        XmlDataAdaptor bpmPV_DA = da.createChild("BPM_PV");
        String chName = "empty";
        String chName0 = arrayDataPV.getChannelName();
        if (chName0 != null) {
            chName = chName0;
        }
        bpmPV_DA.setValue("chName", chName);
        bpmPV_DA.setValue("switchOn", arrayDataPV.getSwitchOn());
        bpmPV_DA.setValue("nGraphPoints", gd.getSize());
        if (gd.getSize() > 0) {
            for (int j = 0, nj = gd.getSize(); j < nj; j++) {
                XmlDataAdaptor g_DA = bpmPV_DA.createChild("point");
                g_DA.setValue("x", int_Format.format(gd.getX(j)));
                g_DA.setValue("y", dbl_Format.format(gd.getY(j)));
            }
        }
    }
