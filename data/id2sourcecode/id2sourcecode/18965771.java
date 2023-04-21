    private void writeHandlerTreeFile() {
        JFileChooser fileChooser = new JFileChooser();
        if ((currentDir != null) && (currentDir.exists())) {
            fileChooser.setCurrentDirectory(currentDir);
        }
        int returnVal = fileChooser.showSaveDialog(this);
        currentDir = fileChooser.getCurrentDirectory();
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (file.exists()) {
            int userChoice = JOptionPane.showConfirmDialog(this, "The selected file already exists.  Overwrite it?", "Confirm overwrite", JOptionPane.YES_NO_OPTION);
            if (userChoice == JOptionPane.NO_OPTION) {
                return;
            }
        }
        String fileName = "";
        try {
            fileName = file.getCanonicalPath();
        } catch (IOException ioe) {
            fileName = file.getAbsolutePath();
        }
        System.err.println("Write handler/filter tree to file " + fileName);
        try {
            JAXBContext jc = JAXBContext.newInstance("org.javasock.jssniff.handlertree");
            ObjectFactory factory = new ObjectFactory();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
            if (!(rootNode.getUserObject() instanceof DeviceData)) {
                throw new Exception("Unknown root node; should be a Device");
            }
            DeviceData deviceData = (DeviceData) rootNode.getUserObject();
            Device device = factory.createDevice();
            deviceData.initializeDevice(device);
            for (int i = 0; i < rootNode.getChildCount(); ++i) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
                Object userObject = node.getUserObject();
                if (userObject instanceof ARPData) {
                    ARPData arpData = (ARPData) userObject;
                    ARPType arpType = factory.createARPType();
                    arpData.initializeARPType(arpType);
                    device.getHandlerType().add(arpType);
                } else if (userObject instanceof IPData) {
                    IPData ipData = (IPData) userObject;
                    IPType ipType = factory.createIPType();
                    ipData.initializeIPType(ipType);
                    if (node.getChildCount() > 0) {
                        for (int j = 0; j < node.getChildCount(); ++j) {
                            DefaultMutableTreeNode subnode = (DefaultMutableTreeNode) node.getChildAt(j);
                            Object subnodeUserObject = subnode.getUserObject();
                            if (subnodeUserObject instanceof ICMPData) {
                                ICMPData icmpData = (ICMPData) subnodeUserObject;
                                ICMPType icmpType = factory.createICMPType();
                                icmpData.initializeICMPType(icmpType);
                                ipType.setICMP(icmpType);
                            } else if (subnodeUserObject instanceof TCPData) {
                                TCPData tcpData = (TCPData) subnodeUserObject;
                                TCPType tcpType = factory.createTCPType();
                                tcpData.initializeTCPType(tcpType);
                                ipType.setTCP(tcpType);
                            } else if (subnodeUserObject instanceof UDPData) {
                                UDPData udpData = (UDPData) subnodeUserObject;
                                UDPType udpType = factory.createUDPType();
                                udpData.initializeUDPType(udpType);
                                ipType.setUDP(udpType);
                            }
                        }
                    }
                    device.getHandlerType().add(ipType);
                }
            }
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            FileOutputStream outStream = new FileOutputStream(file);
            marshaller.marshal(device, outStream);
        } catch (PropertyException e) {
            System.err.println("Exception caught trying to write handler tree file:\n" + e);
            JOptionPane.showMessageDialog(this, new String("Error caught writing handler tree file:\n" + e), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (JAXBException e) {
            System.err.println("Exception caught trying to write handler tree file:\n" + e);
            JOptionPane.showMessageDialog(this, new String("Error caught writing handler tree file:\n" + e), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            System.err.println("Exception caught trying to write handler tree file:\n" + e);
            JOptionPane.showMessageDialog(this, new String("Error caught writing handler tree file:\n" + e), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
