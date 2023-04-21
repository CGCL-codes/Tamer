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
