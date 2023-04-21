    private JMenuItem createExportToCsvMenuItem() {
        JMenuItem ret = new JMenuItem("Export to CSV");
        ret.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ReportDescription[] rep = getAvailableReports();
                List<ReportDescription> descList = new ArrayList<ReportDescription>();
                for (int idx = 0; idx < rep.length; idx++) {
                    if (rep[idx].isInclude()) {
                        try {
                            IReport tr = rep[idx].getImpl();
                            if (tr.getRowCount() > 0) {
                                descList.add(rep[idx]);
                            }
                        } catch (InstantiationException e1) {
                        } catch (IllegalAccessException e1) {
                        }
                    }
                }
                rep = (ReportDescription[]) descList.toArray(new ReportDescription[descList.size()]);
                if (rep.length == 0) {
                    return;
                }
                ReportDescDialog rd = new ReportDescDialog(jtabbedpane);
                RefineryUtilities.centerFrameOnScreen(rd);
                rep = rd.show(rep);
                int cnt = 0;
                for (int idx = 0; idx < rep.length; idx++) {
                    if (rep[idx].isInclude()) {
                        cnt++;
                    }
                }
                if (rd.isCancel() || cnt == 0) {
                    return;
                }
                boolean combine = cnt == 1;
                if (cnt > 1) {
                    int value = JOptionPane.showConfirmDialog(jtabbedpane, "Do you want to save each report in a seperate file?", "Please Confirm", JOptionPane.YES_NO_OPTION);
                    combine = !(value == JOptionPane.YES_OPTION);
                }
                final JFileChooser fc = new JFileChooser();
                if (dataFile != null) {
                    if (dataFile.isFile()) {
                        fc.setSelectedFile(dataFile);
                    }
                }
                if (!combine && cnt > 1) {
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (dataFile != null) {
                        if (dataFile.isFile()) {
                            fc.setSelectedFile(dataFile.getParentFile());
                        } else {
                            fc.setSelectedFile(dataFile);
                        }
                    }
                }
                if (fc.showDialog(jtabbedpane, "Save Data") == JFileChooser.APPROVE_OPTION) {
                    dataFile = fc.getSelectedFile();
                    if (combine || cnt == 1) {
                        if (dataFile.isFile()) {
                            if (dataFile.exists()) {
                                int value = JOptionPane.showConfirmDialog(jtabbedpane, dataFile + " already exists.\n" + "Do you want to over write it?", "Please Confirm", JOptionPane.YES_NO_OPTION);
                                if (value != JOptionPane.YES_OPTION) {
                                    return;
                                }
                            }
                        }
                    } else {
                        if (!dataFile.exists()) {
                            int value = JOptionPane.showConfirmDialog(jtabbedpane, dataFile + " does not exists.\n" + "Do you want create it now?", "", JOptionPane.YES_NO_OPTION);
                            if (value != JOptionPane.YES_OPTION) {
                                return;
                            } else {
                                dataFile.mkdirs();
                            }
                        }
                    }
                    int value = JOptionPane.showConfirmDialog(jtabbedpane, "Format date as GMT?", "", JOptionPane.YES_NO_OPTION);
                    boolean gmt = value == JOptionPane.YES_OPTION;
                    if (cnt == 1 || combine) {
                        String data = getAnalysisData(gmt, rep);
                        OutputStream out = null;
                        try {
                            dataFile.getParentFile().mkdirs();
                            out = new FileOutputStream(dataFile);
                            out.write(data.getBytes());
                            out.flush();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(jtabbedpane, e1.toString(), "An error occurred saving the data.", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception ex) {
                                }
                            }
                        }
                    } else {
                        saveDataToDir(dataFile, rep, gmt);
                    }
                }
            }

            private void saveDataToDir(File dir, ReportDescription[] rep, boolean gmt) {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                boolean saveAll = false;
                for (int idx = 0; idx < rep.length; idx++) {
                    if (!rep[idx].isInclude()) {
                        continue;
                    }
                    OutputStream out = null;
                    AbstractReportPane report = null;
                    try {
                        report = (AbstractReportPane) rep[idx].getImpl();
                        File dataFile = new File(dir, report.getName() + ".csv");
                        if (!saveAll && dataFile.exists()) {
                            Object[] options = { "Yes", "No", "Yes to All" };
                            int value = JOptionPane.showOptionDialog(jtabbedpane, dataFile + " already exists.\nDo you want to over write it?", "Please Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                            saveAll = value == JOptionPane.CANCEL_OPTION;
                            if (value == JOptionPane.NO_OPTION) {
                                continue;
                            }
                        }
                        String data = report.getData(gmt);
                        out = new FileOutputStream(dataFile);
                        out.write(data.getBytes());
                        out.flush();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(jtabbedpane, e1.toString(), "An error occurred saving the data.", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }

            private String getAnalysisData(boolean gmt, ReportDescription[] rep) {
                StringBuffer ret = new StringBuffer();
                for (int idx = 0; idx < rep.length; idx++) {
                    if (!rep[idx].isInclude()) {
                        continue;
                    }
                    AbstractReportPane report = null;
                    try {
                        report = (AbstractReportPane) rep[idx].getImpl();
                        String data = report.getData(gmt);
                        if (ret.length() > 0) {
                            ret.append("\n");
                            ret.append("\n");
                        }
                        ret.append(data);
                    } catch (Exception e) {
                        logger.error("Can't create impl", e);
                    }
                }
                return ret.toString();
            }
        });
        return ret;
    }
