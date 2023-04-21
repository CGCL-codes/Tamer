    public void dumpConfig(XmlDataAdaptor da) {
        XmlDataAdaptor arrPV_DA = da.createChild("ARR_PV");
        String chName = "empty";
        String chName0 = arrayDataPV.getChannelName();
        if (chName0 != null) {
            chName = chName0;
        }
        arrPV_DA.setValue("chName", chName);
        arrPV_DA.setValue("switchOn", arrayDataPV.getSwitchOn());
        arrPV_DA.setValue("nGraphPoints", gd.getSize());
        arrPV_DA.setValue("wrapping", getWrapDataProperty());
        if (gd.getSize() > 0) {
            for (int j = 0, nj = gd.getSize(); j < nj; j++) {
                XmlDataAdaptor g_DA = arrPV_DA.createChild("point");
                g_DA.setValue("x", int_Format.format(gd.getX(j)));
                g_DA.setValue("y", dbl_Format.format(gd.getY(j)));
            }
        }
    }
