    private void writeFilteredExpFile(GrpFile temp) {
        String old = expMain.getName();
        if (old.endsWith(".exp")) old = old.substring(0, old.lastIndexOf(".exp"));
        if (old.lastIndexOf(File.separator) != -1) old = old.substring(old.lastIndexOf(File.separator) + 1);
        String name = (String) JOptionPane.showInputDialog(this, "You have selected " + temp.getNumGenes() + " genes. \nPlease enter new file name", "Save As...", JOptionPane.QUESTION_MESSAGE, null, null, expMain.name + "_" + "filtered.exp");
        if (name != null) {
            if (name.toLowerCase().endsWith(".exp")) name = name.substring(0, name.lastIndexOf("."));
            String file = openProject.getPath() + name + File.separator + name + ".exp";
            File f = new File(file);
            if (!f.exists() || JOptionPane.showConfirmDialog(this, "File Already Exists! Do You Wish To Overwrite?") == JOptionPane.OK_OPTION) {
                try {
                    f.getParentFile().mkdirs();
                    if (!file.toLowerCase().endsWith(".exp")) file += ".exp";
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    for (int i = 0; i < expMain.getColumns(); i++) {
                        bw.write(expMain.getLabel(i) + (i == expMain.getColumns() - 1 ? "" : "\t"));
                    }
                    bw.write("\n");
                    Object o[] = temp.getGroup();
                    for (int i = 0; i < o.length; i++) {
                        int pos = expMain.findGeneName((String) o[i]);
                        if (pos != -1) {
                            bw.write((String) o[i] + "\t");
                            double data[] = expMain.getData(pos);
                            for (int j = 0; j < data.length; j++) {
                                bw.write("" + data[j] + (j == data.length - 1 ? "" : "\t"));
                            }
                            String comments;
                            if ((comments = expMain.getGene(pos).getComments()) != null) bw.write("\t" + comments);
                            bw.write("\n");
                        }
                    }
                    bw.write("/**Gene Info**/" + "\n");
                    for (int i = 0; i < o.length; i++) {
                        int pos = expMain.findGeneName((String) o[i]);
                        if (pos != -1) {
                            Gene g = expMain.getGene(pos);
                            String n = g.getName();
                            String a = g.getAlias();
                            String c = g.getChromo();
                            String l = g.getLocation();
                            String p = g.getProcess();
                            String fl = g.getFunction();
                            String co = g.getComponent();
                            if (n != null) bw.write(n + "\t" + (a != null ? a : " ") + "\t" + (c != null ? c : " ") + "\t" + (l != null ? l : " ") + "\t" + (p != null ? p : " ") + "\t" + (fl != null ? fl : " ") + "\t" + (co != null ? co : " ") + "\n");
                        }
                    }
                    bw.close();
                    this.addExpFile(f.getPath());
                    openProject.addFile(name + File.separator + name + ".exp");
                    if (openProject.getGroupMethod() <= 1) {
                        String groupFiles[] = openProject.getGroupFiles(old);
                        for (int i = 0; i < groupFiles.length; i++) {
                            GrpFile gf = new GrpFile(new File(openProject.getPath() + groupFiles[i]));
                            gf.setExpFile(name);
                            Object[] genes = gf.getGroup();
                            for (int j = 0; j < genes.length; j++) {
                                if (expMain.findGeneName((String) genes[j]) == -1) gf.remove(genes[j]);
                            }
                            try {
                                gf.writeGrpFile(openProject.getPath() + name + File.separator + gf.getTitle());
                                openProject.addFile(name + File.separator + gf.getTitle());
                            } catch (DidNotFinishException e3) {
                            }
                        }
                    }
                    expMain = new ExpFile(f);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error Writing Exp File");
                }
            } else writeFilteredExpFile(temp);
        }
    }
