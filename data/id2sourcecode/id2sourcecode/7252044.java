    private void listRecordsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (dataProviderField.getText().trim().length() == 0) {
            return;
        }
        final int maxRecords;
        try {
            final URL url = new URL(dataProviderField.getText().trim());
            final URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("verb=ListRecords");
            writer.write("&metadataPrefix=oai_dc");
            if (fromField.getText().trim().length() > 0) {
                writer.write("&from=" + fromField.getText().trim());
            }
            if (untilField.getText().trim().length() > 0) {
                writer.write("&until=" + untilField.getText().trim());
            }
            if (setField.getText().trim().length() > 0) {
                writer.write("&set=" + setField.getText().trim());
            }
            int tmpMax = -1;
            if (maxField.getText().trim().length() > 0) {
                try {
                    tmpMax = Integer.parseInt(maxField.getText());
                } catch (NumberFormatException nfe) {
                    tmpMax = -1;
                }
            }
            maxRecords = tmpMax;
            writer.flush();
            conn.connect();
            final ProgressHandle p = ProgressHandleFactory.createHandle("Import OAI-PMH");
            Runnable run = new Runnable() {

                public void run() {
                    final OaiPmhParser parser = new OaiPmhParser();
                    parser.addProgressListener(new ProgressListener() {

                        public void progress(String message, int progress) {
                            p.progress(message);
                        }

                        public void finish() {
                            p.finish();
                            recordList = parser.getRecordList();
                            numberLabel.setText("" + parser.getRecordList().size());
                            recordTable.setModel(new AbstractTableModel() {

                                public int getRowCount() {
                                    return parser.getRecordList().size();
                                }

                                public int getColumnCount() {
                                    return 5;
                                }

                                public Object getValueAt(int rowIndex, int columnIndex) {
                                    switch(columnIndex) {
                                        case 0:
                                            return parser.getRecordList().get(rowIndex).getIdentifier();
                                        case 1:
                                            return parser.getRecordList().get(rowIndex).getTitle();
                                        case 2:
                                            return StringUtility.join(", ", parser.getRecordList().get(rowIndex).getAuthors());
                                        case 3:
                                            return StringUtility.join(", ", parser.getRecordList().get(rowIndex).getDescriptions());
                                        case 4:
                                            return parser.getRecordList().get(rowIndex).getLanguage();
                                        default:
                                            return null;
                                    }
                                }
                            });
                        }

                        public void start(int max) {
                            p.start();
                        }
                    });
                    try {
                        parser.process(conn.getInputStream(), maxRecords);
                        while (parser.getResumptionToken() != null && (maxRecords == -1 || parser.getRecordList().size() < maxRecords)) {
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setDoOutput(true);
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                            writer.write("verb=ListRecords");
                            writer.write("&resumptionToken=" + parser.getResumptionToken());
                            writer.flush();
                            conn.connect();
                            if (conn.getResponseCode() == 500) {
                                continue;
                            }
                            parser.process(conn.getInputStream(), maxRecords);
                        }
                    } catch (IOException ioe) {
                        throw new RuntimeException("IO Error: " + ioe, ioe);
                    }
                }
            };
            Thread thread = new Thread(run);
            thread.start();
        } catch (IOException ioe) {
            throw new RuntimeException("IO Error: " + ioe, ioe);
        }
    }
