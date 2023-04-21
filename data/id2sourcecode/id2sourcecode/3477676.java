    private String addNewDevice(EIBDeviceConfigurator finder, NewEndDeviceDialog dialog) {
        EIBDevicesDataModel devmodel = (EIBDevicesDataModel) this.project.getApplication().getBusDeviceDataModel("EIB");
        InstallationModel imodel = this.project.getInstallationModel();
        Vector actuators = new Vector();
        actuators = finder.findNewActuators();
        if (actuators.isEmpty()) {
            JOptionPane.showMessageDialog(null, locale.getString("mess.noDeviceFound"));
            return null;
        }
        Vector actuatorlist = new Vector();
        for (Enumeration e = actuators.elements(); e.hasMoreElements(); ) {
            String id = (String) e.nextElement();
            if (DEBUG) {
                logger.debug(devmodel.getName(id));
            }
            String text = devmodel.getManufacturerName(id) + " - " + devmodel.getName(id);
            ListEntry le = new ListEntry(text, id);
            actuatorlist.addElement(le);
        }
        ListSelectorDialog lsd2 = new ListSelectorDialog(new Frame(), locale.getString("tit.selDevice"), actuatorlist);
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
        String actuatorID = imodel.addActuator(name);
        imodel.setProperty(actuatorID, "device-name", devmodel.getName(dID));
        imodel.setProperty(actuatorID, "device-id", dID);
        imodel.setProperty(actuatorID, "device-state", "unprogrammed");
        imodel.setProperty(actuatorID, "manufacturer", devmodel.getManufacturerName(dID));
        imodel.setProperty(actuatorID, "bussystem", "EIB");
        imodel.setProperty(actuatorID, "installation-location", dialog.getInstallationLocation());
        imodel.setProperty(actuatorID, "eib-physical-address", pad.toString());
        Vector fgroups = (Vector) devmodel.getFunctionGroupIDs(dID);
        for (Enumeration e = fgroups.elements(); e.hasMoreElements(); ) {
            String devfuncgroupID = (String) e.nextElement();
            String fgid = imodel.addFunctionGroup(actuatorID);
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
        amodel.addBusDevice(finder.getInstallationLocationID(), actuatorID);
        return actuatorID;
    }
