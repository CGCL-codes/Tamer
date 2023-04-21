    private String addNewDevice(EIBDeviceConfigurator finder, NewEndDeviceDialog dialog, String enddeviceID) {
        EIBDevicesDataModel devmodel = (EIBDevicesDataModel) this.project.getApplication().getBusDeviceDataModel("EIB");
        InstallationModel imodel = this.project.getInstallationModel();
        Vector sensors = new Vector();
        sensors = finder.findNewSensors(enddeviceID);
        if (sensors.isEmpty()) {
            JOptionPane.showMessageDialog(null, locale.getString("mess.noDeviceFound"));
            return null;
        }
        Vector sensorlist = new Vector();
        for (Enumeration e = sensors.elements(); e.hasMoreElements(); ) {
            String id = (String) e.nextElement();
            if (DEBUG) {
                logger.debug(devmodel.getName(id));
            }
            String text = devmodel.getManufacturerName(id) + " - " + devmodel.getName(id);
            ListEntry le = new ListEntry(text, id);
            sensorlist.addElement(le);
        }
        ListSelectorDialog lsd2 = new ListSelectorDialog(new Frame(), locale.getString("tit.selDevice"), sensorlist);
        lsd2.setVisible(true);
        if (lsd2.getSelection() == null) {
            return null;
        }
        String name = JOptionPane.showInputDialog(null, locale.getString("mess.inputname"), locale.getString("tit.input"), JOptionPane.WARNING_MESSAGE);
        if (name == null) {
            return null;
        }
        if (name.equals("")) {
            name = locale.getString("noname");
        }
        String pastring = "";
        EIBPhaddress pad;
        while (true) {
            pastring = JOptionPane.showInputDialog(null, locale.getString("mess.inputPhAddr"));
            if (pastring == null) {
                return null;
            }
            try {
                pad = new EIBPhaddress(pastring);
                if (imodel.isEIBPhAddressInUse(pad)) {
                    JOptionPane.showMessageDialog(null, locale.getString("mess.phAddrInUse"));
                } else {
                    break;
                }
            } catch (EIBAddressFormatException afe) {
                JOptionPane.showMessageDialog(null, locale.getString("mess.wrongPhAddrFormat"));
            }
        }
        String dID = ((ListEntry) lsd2.getSelection()).getValue();
        String sensorID = imodel.addSensor(name);
        imodel.setProperty(sensorID, "device-name", devmodel.getName(dID));
        imodel.setProperty(sensorID, "device-id", dID);
        imodel.setProperty(sensorID, "device-state", "unprogrammed");
        imodel.setProperty(sensorID, "manufacturer", devmodel.getManufacturerName(dID));
        imodel.setProperty(sensorID, "bussystem", "EIB");
        imodel.setProperty(sensorID, "installation-location", dialog.getInstallationLocation());
        imodel.setProperty(sensorID, "eib-physical-address", pad.toString());
        logger.debug("eib device id: " + dID);
        Vector fgroups = (Vector) devmodel.getFunctionGroupIDs(dID);
        for (Enumeration e = fgroups.elements(); e.hasMoreElements(); ) {
            String devfuncgroupID = (String) e.nextElement();
            String fgid = imodel.addFunctionGroup(sensorID);
            Vector funcs = (Vector) devmodel.getFunctionIDs(devfuncgroupID);
            for (Enumeration f = funcs.elements(); f.hasMoreElements(); ) {
                String devfuncID = (String) f.nextElement();
                String fid = imodel.addFunction(fgid);
                imodel.setName(fid, devmodel.getName(devfuncID));
                Node source = devmodel.getDataRootNode(devfuncID);
                Node dest = imodel.getDataRootNode(fid);
                imodel.writeDOMNodeValue(dest, new StringTokenizer("type", "/"), devmodel.readDOMNodeValue(source, new StringTokenizer("type", "/")));
                imodel.writeDOMNodeValue(dest, new StringTokenizer("eis-type", "/"), devmodel.readDOMNodeValue(source, new StringTokenizer("eis-type", "/")));
                imodel.writeDOMNodeValue(dest, new StringTokenizer("com-object", "/"), devmodel.readDOMNodeValue(source, new StringTokenizer("com-object", "/")));
                imodel.writeDOMNodeValue(dest, new StringTokenizer("devmodelid", "/"), devfuncID);
                imodel.writeDOMNodeValue(dest, new StringTokenizer("state", "/"), "unused");
            }
        }
        ArchitecturalDataModel amodel = this.project.getArchitecturalDataModel();
        amodel.addBusDevice(finder.getInstallationLocationID(), sensorID);
        logger.debug("SensorID: " + sensorID);
        return sensorID;
    }
