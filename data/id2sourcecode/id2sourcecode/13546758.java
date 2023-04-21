    public void createNewExpressionFile(String name, String colName, boolean newFile, String appendname, boolean byname, int method, int ratiomethod, Object params[]) {
        final String theName = name;
        final String colname = colName;
        final String appendName = appendname;
        final boolean byName = byname;
        final int meth = method;
        final int ratioMethod = ratiomethod;
        final Object[] par = params;
        final boolean createNew = newFile;
        if (project != null) {
            String file = theName;
            if (file.toLowerCase().endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
            File f = new File(project.getPath() + file + File.separator + file + ".exp");
            int deleteFiles = JOptionPane.CANCEL_OPTION;
            if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(null, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
                try {
                    if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    f.getParentFile().mkdirs();
                    File temp = null;
                    if (!createNew) temp = File.createTempFile(file, null);
                    BufferedWriter bw;
                    if (createNew) bw = new BufferedWriter(new FileWriter(f.getPath())); else bw = new BufferedWriter(new FileWriter(temp.getPath()));
                    bw.write(colname);
                    bw.write("\n");
                    progress = new ProgressFrame("Creating New Expression File: " + file + ".exp");
                    if (!createNew) progress.setTitle("Generating Expression Data");
                    getDesktopPane().add(progress);
                    progress.show();
                    int totalNumSpots = 0;
                    for (int i = 0; i < manager.getGridNum(); i++) {
                        totalNumSpots += manager.getGrid(i).getNumOfSpots();
                    }
                    progress.setMaximum(totalNumSpots);
                    int num = 0;
                    for (int i = 0; i < manager.getGridNum(); i++) {
                        Grid g = manager.getGrid(i);
                        idRed.setSpots(i);
                        for (int j = 0; j < g.getNumOfSpots(); j++) {
                            int aspot = manager.getActualSpotNum(i, j);
                            String gname = manager.getGeneName(i, aspot);
                            if (!gname.equalsIgnoreCase("empty") && !gname.equalsIgnoreCase("blank") && !gname.equalsIgnoreCase("missing") && !gname.equalsIgnoreCase("none") && !gname.equalsIgnoreCase("No Gene Specified")) {
                                SingleGeneImage currentGene = new SingleGeneImage(idRed.getCellPixels(i, aspot), idGreen.getCellPixels(i, aspot), idRed.getCellHeight(i, aspot), idRed.getCellWidth(i, aspot));
                                GeneData gd = null;
                                gd = currentGene.getData(meth, par);
                                bw.write(gname + "\t");
                                if (gd != null) {
                                    bw.write(String.valueOf(gd.getRatio(ratioMethod)));
                                } else {
                                    bw.write("\t");
                                }
                                bw.write("\n");
                                progress.addValue(1);
                                num++;
                            }
                        }
                    }
                    bw.close();
                    boolean add = true;
                    String app = appendName;
                    if (!createNew && appendName.toLowerCase().endsWith(".exp")) app = appendName.substring(0, appendName.lastIndexOf("."));
                    if (!createNew) add = mergeFiles(f, new File(project.getPath() + app + File.separator + app + ".exp"), temp, byName);
                    if (add) project.addFile(file + File.separator + file + ".exp");
                    progress.dispose();
                    if (add) main.addExpFile(f.getPath());
                    if (!createNew && add && project.getGroupMethod() == Project.ALWAYS_CREATE) {
                        String shortfile = theName;
                        if (shortfile.toLowerCase().endsWith(".exp")) shortfile = shortfile.substring(0, shortfile.toLowerCase().lastIndexOf(".exp"));
                        if (shortfile.lastIndexOf(File.separator) != -1) shortfile = shortfile.substring(shortfile.lastIndexOf(File.separator) + 1);
                        String old = appendName;
                        if (old != null && old.endsWith(".exp")) old = old.substring(0, old.lastIndexOf(".exp"));
                        if (old != null && old.lastIndexOf(File.separator) != -1) old = old.substring(old.lastIndexOf(File.separator) + 1);
                        String groupFiles[] = project.getGroupFiles(old);
                        for (int i = 0; i < groupFiles.length; i++) {
                            GrpFile gf = new GrpFile(new File(project.getPath() + groupFiles[i]));
                            gf.setExpFile(shortfile);
                            try {
                                gf.writeGrpFile(project.getPath() + shortfile + File.separator + gf.getTitle());
                                project.addFile(shortfile + File.separator + gf.getTitle());
                            } catch (DidNotFinishException e3) {
                            }
                        }
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Error Writing Exp File");
                    f.delete();
                    if (progress != null) progress.dispose();
                    e2.printStackTrace();
                }
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
