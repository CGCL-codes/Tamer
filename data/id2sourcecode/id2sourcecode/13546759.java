    public void createNewRawDataFile(String rawFileName, String expFileName, String colName, int method, int ratioMethod, Object params[]) {
        System.out.println("top of createNewRawDataFile");
        if (rawFileName.toLowerCase().endsWith(".raw")) rawFileName = rawFileName.substring(0, rawFileName.lastIndexOf("."));
        final String file = rawFileName;
        File f = new File(project.getPath() + file + File.separator + file + ".raw");
        int deleteFiles = JOptionPane.CANCEL_OPTION;
        if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(null, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
            try {
                if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                f.getParentFile().mkdirs();
                System.out.println("file: " + f);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        progress = new ProgressFrame("creating new raw data file: " + file + ".raw");
                    }
                });
                getDesktopPane().add(progress);
                progress.show();
                int totalNumSpots = 0;
                for (int i = 0; i < manager.getGridNum(); i++) totalNumSpots += manager.getGrid(i).getNumOfSpots();
                progress.setMaximum(totalNumSpots);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()), 4096);
                bw.write(expFileName + ":" + colName + "\tRedFGtot\tRedBGtot\tGrnFGtot\tGrnBGtot\tRedFGavg\tRedBGavg\tGrnFGavg\tGrnBGavg");
                bw.write("\n");
                for (int i = 0; i < manager.getGridNum(); i++) {
                    Grid g = manager.getGrid(i);
                    idRed.setSpots(i);
                    for (int j = 0; j < g.getNumOfSpots(); j++) {
                        int aspot = manager.getActualSpotNum(i, j);
                        String gname = manager.getGeneName(i, aspot);
                        if (!gname.equalsIgnoreCase("empty") && !gname.equalsIgnoreCase("blank") && !gname.equalsIgnoreCase("missing") && !gname.equalsIgnoreCase("none") && !gname.equalsIgnoreCase("No Gene Specified")) {
                            SingleGeneImage currentGene = new SingleGeneImage(idRed.getCellPixels(i, aspot), idGreen.getCellPixels(i, aspot), idRed.getCellHeight(i, aspot), idRed.getCellWidth(i, aspot));
                            GeneData gd = null;
                            gd = currentGene.getData(method, params);
                            bw.write(gname + "\t");
                            if (gd != null) {
                                bw.write(String.valueOf(gd.getRedForegroundTotal()) + "\t");
                                bw.write(String.valueOf(gd.getRedBackgroundTotal()) + "\t");
                                bw.write(String.valueOf(gd.getGreenForegroundTotal()) + "\t");
                                bw.write(String.valueOf(gd.getGreenBackgroundTotal()) + "\t");
                                bw.write(String.valueOf(gd.getRedForegroundAvg()) + "\t");
                                bw.write(String.valueOf(gd.getRedBackgroundAvg()) + "\t");
                                bw.write(String.valueOf(gd.getGreenForegroundAvg()) + "\t");
                                bw.write(String.valueOf(gd.getGreenBackgroundAvg()));
                            } else {
                                bw.write("\t");
                            }
                            bw.write("\n");
                            progress.addValue(1);
                            progress.dispose();
                        }
                    }
                }
                bw.close();
                System.out.println("bottom of createNewRawDataFile");
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, "Error Writing .raw File");
                f.delete();
                if (progress != null) progress.dispose();
                e2.printStackTrace();
            }
            setCursor(Cursor.getDefaultCursor());
        }
    }
