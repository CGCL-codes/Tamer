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
