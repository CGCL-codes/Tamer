                @Override
                protected Object doInBackground() throws Exception {
                    for (int i = 0; i < dtmAttachments.getRowCount(); i++) {
                        SimpleCombobBoxItem scbi = (SimpleCombobBoxItem) dtmAttachments.getValueAt(i, 0);
                        if (!dtmAttachments.getValueAt(i, 1).getClass().getName().contains("String")) {
                            AttachmentsTableCellValue atcv = (AttachmentsTableCellValue) dtmAttachments.getValueAt(i, 1);
                            if (!atcv.isLocatedOnServer()) {
                                String path = atcv.getPath();
                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("http://" + ServletConnector.serverURL + ":8080/newgenlibctxt/AttachmentUploadServlet?Type=" + scbi.getIndex() + "&Id=" + catId + "&LibId=" + libId);
                                    FileBody bin = new FileBody(new File(path));
                                    StringBody comment = new StringBody("Filename: " + atcv.getName());
                                    lbMessage.setText("Uploading " + atcv.getName());
                                    MultipartEntity reqEntity = new MultipartEntity();
                                    reqEntity.addPart("bin", bin);
                                    reqEntity.addPart("comment", comment);
                                    httppost.setEntity(reqEntity);
                                    HttpResponse response = httpclient.execute(httppost);
                                    HttpEntity resEntity = response.getEntity();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                    lbMessage.setText("Upload completed");
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), "Upload completed", "Uploaded completed", JOptionPane.INFORMATION_MESSAGE);
                    dialogProgress.dispose();
                    progressbar.setIndeterminate(false);
                    return null;
                }
