    public void writeData() {
        if (scanStopper.getScanStopper().isRunning()) {
            scanStopper.getScanStopper().warning("Can not write data during the scan.");
            return;
        }
        if (bpmNumber == 0 || cavityNumber == 0) return;
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Write Data");
        if (dataFile != null) {
            ch.setSelectedFile(dataFile);
        }
        int returnVal = ch.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                dataFile = ch.getSelectedFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(dataFile));
                out.write("%=======double (voltage+phase) cavity scan data  ===");
                out.newLine();
                out.write(" " + cavityNumber + " -numbers of cavity");
                out.newLine();
                out.write(" " + bpmNumber + " -numbers of bpm");
                out.newLine();
                for (int i = 0; i < cavityNumber; i++) {
                    for (int j = 0; j < bpmNumber; j++) {
                        out.write("%====new  set of graphs for cavity N=" + i + " and bpm N=" + j);
                        out.newLine();
                        int nDataCont = bpmPhasesMeasAll[i][j].getNumberOfDataContainers();
                        BasicGraphData gd;
                        BasicGraphData gdRB;
                        out.write(" " + nDataCont + " -number of data containers");
                        out.newLine();
                        for (int k = 0; k < nDataCont; k++) {
                            out.write("%-----------new graph data-------------------------");
                            out.newLine();
                            gd = bpmPhasesMeasAll[i][j].getDataContainer(k);
                            gdRB = bpmPhasesMeasAll[i][j].getDataContainerRB(k);
                            int nPoints = gd.getNumbOfPoints();
                            out.write("    " + nPoints + " -number of data points");
                            out.newLine();
                            Double cavAmp = (Double) gd.getGraphProperty(cavAmpSetNames[i]);
                            out.write(cavAmpSetNames[i] + "  " + cavAmp + " -cavity voltage (Set) for this data set");
                            out.newLine();
                            Double cavAmpRB = (Double) gd.getGraphProperty(cavAmpRbNames[i]);
                            out.write(cavAmpRbNames[i] + "  " + cavAmpRB + " -cavity voltage (Read Back) for this data set");
                            out.newLine();
                            for (int l = 0; l < nPoints; l++) {
                                out.write(" " + gd.getX(l) + " " + gd.getY(l) + " " + gd.getErr(l));
                                out.newLine();
                            }
                            out.write("%   now read back data----------------------");
                            out.newLine();
                            for (int l = 0; l < nPoints; l++) {
                                out.write(" " + gdRB.getX(l) + " " + gdRB.getY(l) + " " + gdRB.getErr(l));
                                out.newLine();
                            }
                        }
                    }
                }
                out.write("%-----end of data---------");
                out.newLine();
                out.flush();
                out.close();
            } catch (IOException e) {
                Toolkit.getDefaultToolkit().beep();
                System.out.println(e.toString());
            }
        }
    }
