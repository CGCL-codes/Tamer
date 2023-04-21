            public void run() {
                if (jTable1.getModel().getRowCount() > 0) {
                    if (checkAnchors(fls, fta)) {
                        JFileChooser jfc = new JFileChooser(cwd);
                        jfc.setDialogTitle("Select output directory for anchor files");
                        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int save = jfc.showSaveDialog(SwingUtilities.getRoot(jc));
                        if (save == JFileChooser.APPROVE_OPTION) {
                            setEnabled(false);
                            File outputDir = jfc.getSelectedFile();
                            ArrayList<String> anchorFiles = new ArrayList<String>();
                            for (String key : fls) {
                                File output = new File(outputDir, new File(StringTools.removeFileExt(key) + ".txt").getName());
                                if (output.exists()) {
                                    int ret = JOptionPane.showOptionDialog(SwingUtilities.getRoot(jc), "File " + output + " already exists, overwrite?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                                    if (ret == JOptionPane.NO_OPTION) {
                                        hasSaved = false;
                                        return;
                                    }
                                }
                                String fileName = output.getName();
                                String fnameNoSuffix = fileName.substring(0, (fileName.lastIndexOf(".") < 0 ? fileName.length() : fileName.lastIndexOf(".")));
                                StringBuffer sb = new StringBuffer();
                                sb.append(">" + key + "\n");
                                sb.append("Name\tRI\tRT\tScan\n");
                                DefaultTableModel dtm = fta.get(key);
                                int lastIndex = -1;
                                for (int i = 0; i < dtm.getRowCount(); i++) {
                                    String o = dtm.getValueAt(i, 1).toString();
                                    int value = Integer.parseInt(o);
                                    if (o.equals("-1")) {
                                        sb.append(dtm.getValueAt(i, 0) + "\t-\t-\t" + "-" + "\n");
                                    } else {
                                        if (value > lastIndex) {
                                            sb.append(dtm.getValueAt(i, 0) + "\t-\t-\t" + o + "\n");
                                            lastIndex = value;
                                        } else {
                                            jComboBox1.setSelectedItem(key);
                                            editAnchorsForFile(new ActionEvent(jTable1, 1, "editAnchorsForFile"));
                                            setEnabled(true);
                                            setProblem("Anchor " + dtm.getValueAt(i, 0) + ": Scan no. values must be in ascending order!");
                                            return;
                                        }
                                    }
                                }
                                cwd = output.getParentFile();
                                try {
                                    PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter(output)));
                                    bw.append(sb.toString());
                                    bw.flush();
                                    bw.close();
                                    anchorFiles.add(output.getAbsolutePath());
                                } catch (IOException e) {
                                    System.err.println(e.getLocalizedMessage());
                                }
                                StringBuffer sb2 = new StringBuffer();
                                for (int i = 0; i < anchorFiles.size(); i++) {
                                    sb2.append(anchorFiles.get(i) + ((i < anchorFiles.size() - 1) ? (",") : ("")));
                                }
                                hasSaved = true;
                                putWizardData("anchors.location", sb2.toString());
                                putWizardData("anchors.use", new Boolean(true));
                            }
                            setEnabled(true);
                        }
                    }
                }
            }
