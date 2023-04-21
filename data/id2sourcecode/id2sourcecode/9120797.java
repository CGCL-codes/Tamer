    public void writeExpFile(ExpFile exp, String defaultName, String oldExpFile, boolean modify) {
        String old = null;
        if (oldExpFile != null && !oldExpFile.trim().equals("")) {
            old = oldExpFile;
            if (old.endsWith(".exp")) old = old.substring(0, old.lastIndexOf(".exp"));
            if (old.lastIndexOf(File.separator) != -1) old = old.substring(old.lastIndexOf(File.separator) + 1);
        }
        String file = (String) JOptionPane.showInputDialog(this, "Please Enter New Expression File Name", "Select File Name", JOptionPane.QUESTION_MESSAGE, null, null, defaultName);
        if (file != null) {
            if (file.toLowerCase().endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
            File f = new File(openProject.getPath() + file + File.separator + file + ".exp");
            int deleteFiles = JOptionPane.CANCEL_OPTION;
            if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(this, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
                try {
                    if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                    f.getParentFile().mkdirs();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
                    for (int i = 0; i < exp.getColumns(); i++) {
                        bw.write(exp.getLabel(i) + "\t");
                    }
                    bw.write("\n");
                    for (int i = 0; i < exp.numGenes(); i++) {
                        bw.write(exp.getGeneName(i) + "\t");
                        double data[] = exp.getData(i);
                        for (int j = 0; j < data.length; j++) {
                            bw.write("" + data[j] + (j == data.length - 1 ? "" : "\t"));
                        }
                        String comments;
                        if ((comments = exp.getGene(i).getComments()) != null) bw.write("\t" + comments);
                        bw.write("\n");
                    }
                    bw.write("/**Gene Info**/" + "\n");
                    for (int i = 0; i < exp.numGenes(); i++) {
                        Gene g = exp.getGene(i);
                        String n = g.getName();
                        String a = g.getAlias();
                        String c = g.getChromo();
                        String l = g.getLocation();
                        String p = g.getProcess();
                        String fl = g.getFunction();
                        String co = g.getComponent();
                        if (n != null) bw.write(n + "\t" + (a != null ? a : " ") + "\t" + (c != null ? c : " ") + "\t" + (l != null ? l : " ") + "\t" + (p != null ? p : " ") + "\t" + (fl != null ? fl : " ") + "\t" + (co != null ? co : " ") + "\n");
                    }
                    bw.close();
                    String filename = file + File.separator + file + ".exp";
                    openProject.addFile(filename);
                    addExpFile(f.getPath());
                    if (old != null && (openProject.getGroupMethod() == 0 || (!modify && openProject.getGroupMethod() == 1))) {
                        String groupFiles[] = openProject.getGroupFiles(old);
                        for (int i = 0; i < groupFiles.length; i++) {
                            GrpFile gf = new GrpFile(new File(openProject.getPath() + groupFiles[i]));
                            gf.setExpFile(file);
                            try {
                                gf.writeGrpFile(openProject.getPath() + file + File.separator + gf.getTitle());
                                openProject.addFile(file + File.separator + gf.getTitle());
                            } catch (DidNotFinishException e3) {
                            }
                        }
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(this, "Error Writing Exp File");
                }
            } else writeExpFile(exp, defaultName, oldExpFile, modify);
        }
    }
