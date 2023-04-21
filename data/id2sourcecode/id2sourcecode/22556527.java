            public void actionPerformed(ActionEvent event) {
                EditHighLevelProcessGui gui = new EditHighLevelProcessGui(model);
                ComponentFrame frame = MainUI.getInstance().createAndReturnFrame("View/Edit High Level Process", gui.getVisualization());
                frame.addInternalFrameListener(new InternalFrameListener() {

                    public void internalFrameClosing(InternalFrameEvent e) {
                        HLPetriNet cloned = (HLPetriNet) model.clone();
                        ColoredPetriNet netToExport = new ColoredPetriNet(cloned);
                        CpnExportSettings exportCPN = new CpnExportSettings(myAlgorithm, netToExport, false);
                        exportCPN.saveCPNmodel();
                        String distrLoc = locationForCurrentSimSettings + "\\" + "arrivalRate.sml";
                        FileWriter dout = null;
                        try {
                            dout = new FileWriter(distrLoc);
                            dout.write("fun readArrivalRate() = " + CpnUtils.getCpnDistributionFunction(model.getHLProcess().getGlobalInfo().getCaseGenerationScheme()) + ";");
                            dout.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    public void internalFrameActivated(InternalFrameEvent e) {
                    }

                    public void internalFrameClosed(InternalFrameEvent e) {
                    }

                    public void internalFrameDeiconified(InternalFrameEvent e) {
                    }

                    public void internalFrameIconified(InternalFrameEvent e) {
                    }

                    public void internalFrameOpened(InternalFrameEvent e) {
                    }

                    public void internalFrameDeactivated(InternalFrameEvent e) {
                    }
                });
            }
