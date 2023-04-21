            public void actionPerformed(ActionEvent ev) {
                int row = fileselector.getSelectedRow();
                int col = 0;
                String filename = (String) sourcemodel.getValueAt(0, 0);
                Boolean isURL = (Boolean) sourcemodel.getValueAt(0, 1);
                BufferedReader br = null;
                File file = null;
                DataInputStream in = null;
                if (isURL.booleanValue()) {
                    try {
                        URL url2goto = new URL(filename);
                        in = new DataInputStream(url2goto.openStream());
                    } catch (Exception err) {
                        throw new RuntimeException("Problem constructing URI for " + filename + ".", err);
                    }
                } else {
                    file = new File(filename);
                    if (!file.exists()) {
                        throw new RuntimeException("The file named '" + filename + "' does not exist.");
                    }
                    FileInputStream fstream = null;
                    try {
                        fstream = new FileInputStream(filename);
                        in = new DataInputStream(fstream);
                    } catch (Exception err) {
                        throw new RuntimeException("Problem creating FileInputStream for " + filename + ".", err);
                    }
                }
                br = new BufferedReader(new InputStreamReader(in));
                JTable hdrTable = formatTable.getTableHeader().getTable();
                try {
                    String strLine;
                    int line = 0;
                    int ignorePreHdrLines = ((Integer) preheaderlines.getValue()).intValue();
                    int ignorePostHdrLines = ((Integer) postheaderlines.getValue()).intValue();
                    int numhdr = 0;
                    boolean hasHeaderLine = false;
                    if (hasHeaderLineBox.isSelected()) {
                        hasHeaderLine = true;
                    }
                    if (hasHeaderLine) {
                        numhdr = 1;
                    }
                    String FD = fieldDelimiter.getText();
                    while ((strLine = br.readLine()) != null) {
                        if (line <= (ignorePreHdrLines + numhdr + ignorePostHdrLines)) {
                        } else {
                            String[] putative_cols = strLine.split(FD);
                            String FMT = "";
                            while (formatTable.getColumnCount() > putative_cols.length) {
                                TableColumn tcol = formatTable.getColumnModel().getColumn(0);
                                formatTable.removeColumn(tcol);
                            }
                            for (int i = 0; i < putative_cols.length; i++) {
                                String fmt = "";
                                try {
                                    Double dummy = new Double(putative_cols[i]);
                                    fmt = "%f";
                                } catch (Exception err) {
                                    fmt = "%s";
                                }
                                FMT = FMT + fmt;
                            }
                            formatmodel.setFormat(FMT);
                            break;
                        }
                        line++;
                    }
                    in.close();
                } catch (Exception err) {
                    throw new RuntimeException("Problem reading single line from file.", err);
                }
                for (int j = 0; j < formatTable.getColumnCount(); j++) {
                    hdrTable.getColumnModel().getColumn(j).setHeaderValue("" + (j + 1));
                }
                formatTable.repaint();
                int selectedRow = fileselector.getSelectedRow();
                if ((selectedRow < sourcemodel.getRowCount()) && (selectedRow >= 0)) {
                    updateDetailsFor(selectedRow);
                }
            }
