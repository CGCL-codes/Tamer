    private void okButton_actionPerformed(ActionEvent e) {
        checkForOK();
        if (okButton.isEnabled()) {
            final ProgressFrame progress = new ProgressFrame("Loading Info File");
            desktop.add(progress);
            int genesFound = 0;
            ExpFile exp1 = new ExpFile(new File(filebox1.getFilePath()));
            expFilePath = filebox1.getFilePath();
            progress.setMaximum(exp1.numGenes());
            String file = outFileField.getText().trim();
            if (file.endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
            File f = new File(project.getPath() + file + File.separator + file + ".exp");
            int deleteFiles = JOptionPane.CANCEL_OPTION;
            if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(parent, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
                try {
                    if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    f.getParentFile().mkdirs();
                    progress.show();
                    progress.setTitle("Loading " + infoFileField.getText().trim());
                    InfoFile infoFile = new InfoFile(new File(infoFileField.getText().trim()));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
                    for (int i = 0; i < exp1.getColumns(); i++) {
                        bw.write(exp1.getLabel(i) + "\t");
                    }
                    bw.write("\n");
                    for (int i = 0; i < exp1.numGenes(); i++) {
                        bw.write(exp1.getGeneName(i) + "\t");
                        double data[] = exp1.getData(i);
                        for (int j = 0; j < data.length; j++) {
                            bw.write("" + data[j] + "\t");
                        }
                        int pos = exp1.findGeneName(exp1.getGeneName(i));
                        if (pos != -1) {
                            String comments = exp1.getGene(pos).getComments();
                            if (comments == null) comments = "";
                            bw.write("" + comments);
                        }
                        bw.write("\n");
                    }
                    bw.write("/**Gene Info**/" + "\n");
                    for (int i = 0; i < exp1.numGenes(); i++) {
                        Gene g = exp1.getGene(i);
                        String n = g.getName();
                        String nTemp = null;
                        if (n.indexOf("/") == -1) nTemp = n; else nTemp = n.substring(0, n.indexOf("/"));
                        int infoNumber = infoFile.findGeneName(nTemp);
                        int place;
                        if ((place = nTemp.toLowerCase().indexOf("_rep")) != -1) infoNumber = infoFile.findGeneName(nTemp.substring(0, place));
                        String a = null;
                        String c = null;
                        String l = null;
                        String p = null;
                        String fl = null;
                        String co = null;
                        if (infoNumber != -1) {
                            genesFound++;
                            a = infoFile.getGene(infoNumber).getAlias();
                            c = infoFile.getGene(infoNumber).getChromo();
                            l = infoFile.getGene(infoNumber).getLocation();
                            p = infoFile.getGene(infoNumber).getProcess();
                            fl = infoFile.getGene(infoNumber).getFunction();
                            co = infoFile.getGene(infoNumber).getComponent();
                        }
                        if (n != null) bw.write(n + "\t" + (a != null ? a : " ") + "\t" + (c != null ? c : " ") + "\t" + (l != null ? l : " ") + "\t" + (p != null ? p : " ") + "\t" + (fl != null ? fl : " ") + "\t" + (co != null ? co : " ") + "\n");
                        progress.addValue(1);
                    }
                    bw.close();
                    finished = true;
                    progress.dispose();
                    filename = file + File.separator + file + ".exp";
                    JOptionPane.showMessageDialog(parent, "Found info for " + genesFound + " of " + exp1.numGenes() + " genes.");
                    dispose();
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(parent, "Error Writing Exp File - " + e2);
                }
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
