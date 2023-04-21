    private void okButton_actionPerformed(ActionEvent e) {
        String file = fileField.getText().trim();
        if (file.endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
        File f = new File(project.getPath() + file + File.separator + file + ".exp");
        int deleteFiles = JOptionPane.CANCEL_OPTION;
        if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(null, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
            try {
                if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                f.getParentFile().mkdirs();
                String[] cols = twoPanel.getFirstElements();
                boolean useColumn[] = new boolean[exp.getColumns()];
                BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
                int colnum = 0;
                for (int i = 0; i < exp.getColumns(); i++) {
                    useColumn[i] = false;
                    for (int j = 0; j < cols.length; j++) {
                        if (cols[j].equals(exp.getLabel(i))) {
                            useColumn[i] = true;
                            colnum++;
                            break;
                        }
                    }
                    if (useColumn[i]) bw.write(exp.getLabel(i) + "\t");
                }
                bw.write("\n");
                for (int i = 0; i < exp.numGenes(); i++) {
                    bw.write(exp.getGeneName(i) + "\t");
                    double data[] = exp.getData(i);
                    int written = 0;
                    for (int j = 0; j < data.length; j++) {
                        if (useColumn[j]) {
                            bw.write("" + data[j] + (written == colnum - 1 ? "" : "\t"));
                            written++;
                        }
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
                finished = true;
                filename = file + File.separator + file + ".exp";
                this.dispose();
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, "Error Writing Exp File");
            }
        }
    }
