    private void okButton_actionPerformed(ActionEvent e) {
        checkForOK();
        if (okButton.isEnabled()) {
            expFilePath = filebox1.getFilePath();
            ExpFile exp1 = new ExpFile(new File(filebox1.getFilePath()));
            String file = outFileField.getText().trim();
            if (file.toLowerCase().endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
            File f = new File(project.getPath() + file + File.separator + file + ".exp");
            int deleteFiles = JOptionPane.CANCEL_OPTION;
            if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(parent, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
                try {
                    if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    f.getParentFile().mkdirs();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
                    for (int i = 0; i < exp1.getColumns(); i++) {
                        bw.write(exp1.getLabel(i) + "\t");
                    }
                    bw.write("\n");
                    Vector allGenes = new Vector(exp1.numGenes());
                    for (int i = 0; i < exp1.numGenes(); i++) allGenes.add(exp1.getGene(i));
                    for (int i = 0; i < exp1.numGenes(); i++) {
                        String name = exp1.getGeneName(i);
                        if (allGenes.contains(exp1.getGene(exp1.findGeneName(name)))) {
                            if (name.toLowerCase().indexOf("_rep") != -1) {
                                Vector allReps = new Vector();
                                String comments = "";
                                for (int j = 0; j < allGenes.size(); j++) {
                                    if (((Gene) allGenes.get(j)).getName().startsWith(name.substring(0, name.toLowerCase().indexOf("_rep")))) {
                                        Gene g = (Gene) allGenes.get(j);
                                        allReps.add(g);
                                        if (g.getComments() != null && !g.getComments().trim().equals("")) comments += g.getComments() + " ";
                                    }
                                }
                                double avgData[] = ((Gene) allReps.get(0)).getData();
                                for (int j = 1; j < allReps.size(); j++) {
                                    double data[] = ((Gene) allReps.get(j)).getData();
                                    for (int k = 0; k < avgData.length; k++) avgData[k] += data[k];
                                }
                                for (int k = 0; k < avgData.length; k++) avgData[k] = avgData[k] / (double) allReps.size();
                                for (int j = 0; j < allReps.size(); j++) allGenes.remove(allReps.get(j));
                                String newname = name.substring(0, name.toLowerCase().indexOf("_rep"));
                                int geneNum = exp1.findGeneName(name);
                                allGenes.add(new Gene(newname, avgData, exp1.getGene(geneNum).getChromo(), exp1.getGene(geneNum).getLocation(), exp1.getGene(geneNum).getAlias(), exp1.getGene(geneNum).getProcess(), exp1.getGene(geneNum).getFunction(), exp1.getGene(geneNum).getComponent()));
                                bw.write(newname + "\t");
                                for (int j = 0; j < avgData.length; j++) bw.write("" + avgData[j] + "\t");
                                if (comments == null) comments = "";
                                bw.write(comments + "\n");
                            } else {
                                bw.write(name + "\t");
                                double data[] = exp1.getData(i);
                                for (int j = 0; j < data.length; j++) {
                                    bw.write("" + data[j] + "\t");
                                }
                                String comments = exp1.getGene(exp1.findGeneName(name)).getComments();
                                if (comments == null) comments = "";
                                bw.write(comments + "\n");
                            }
                        }
                    }
                    bw.write("/**Gene Info**/" + "\n");
                    for (int i = 0; i < allGenes.size(); i++) {
                        Gene g = (Gene) allGenes.get(i);
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
                    finished = true;
                    filename = file + File.separator + file + ".exp";
                    dispose();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "Error Writing Exp File");
                }
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
