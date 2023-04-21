    private void okButton_actionPerformed(ActionEvent e) {
        final Component parent = this;
        Thread thread = new Thread() {

            public void run() {
                ExpFile exp1 = new ExpFile(new File(filebox1.getFilePath()));
                ExpFile exp2 = new ExpFile(new File(filebox2.getFilePath()));
                mainExpFile = (functionCombo.getSelectedIndex() == 0 ? filebox1.getFilePath() : filebox2.getFilePath());
                String file = outFileField.getText().trim();
                if (file.endsWith(".exp")) file = file.substring(0, file.lastIndexOf("."));
                File f = new File(project.getPath() + file + File.separator + file + ".exp");
                int deleteFiles = JOptionPane.CANCEL_OPTION;
                if (!f.exists() || (deleteFiles = JOptionPane.showConfirmDialog(parent, "File Already Exists! Do You Wish To Overwrite?\nOverwriting The File Will Delete All Files Which Used The Previous File")) == JOptionPane.OK_OPTION) {
                    try {
                        if (deleteFiles == JOptionPane.OK_OPTION) f.getParentFile().delete();
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        f.getParentFile().mkdirs();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
                        for (int i = 0; i < exp1.getColumns(); i++) {
                            bw.write(exp1.getLabel(i) + "_" + nick1.getText() + "\t");
                        }
                        for (int i = 0; i < exp2.getColumns(); i++) {
                            bw.write(exp2.getLabel(i) + "_" + nick2.getText() + (i == exp2.getColumns() - 1 ? "" : "\t"));
                        }
                        bw.write("\n");
                        for (int i = 0; i < exp1.numGenes(); i++) {
                            int pos;
                            if ((pos = exp2.findGeneName(exp1.getGeneName(i))) != -1) {
                                bw.write(exp1.getGeneName(i) + "\t");
                                double data[] = exp1.getData(i);
                                for (int j = 0; j < data.length; j++) {
                                    bw.write("" + data[j] + "\t");
                                }
                                double data2[] = exp2.getData(pos);
                                for (int j = 0; j < data2.length; j++) {
                                    bw.write("" + data2[j] + (j == data2.length - 1 ? "" : "\t"));
                                }
                                String comments = "", comments1, comments2;
                                comments1 = exp1.getGene(i).getComments();
                                comments2 = exp2.getGene(pos).getComments();
                                if (comments1 != null) comments += comments1 + " ";
                                if (comments2 != null) comments += comments2;
                                bw.write("\t" + comments);
                                bw.write("\n");
                            }
                        }
                        bw.write("/**Gene Info**/" + "\n");
                        ExpFile tempexp = exp1;
                        if (functionCombo.getSelectedIndex() == 1) tempexp = exp2;
                        for (int i = 0; i < tempexp.numGenes(); i++) {
                            Gene g = tempexp.getGene(i);
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
                        JOptionPane.showMessageDialog(parent, "Error Writing Exp File");
                        e2.printStackTrace();
                    }
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        thread.start();
    }
