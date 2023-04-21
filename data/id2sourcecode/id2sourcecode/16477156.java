    private void doReplay() {
        RandomAccessFile raFile = null;
        String line = null;
        long offset = 0;
        txtStatus.setText("");
        txtStatus.setCaretPosition(txtStatus.getDocument().getLength());
        int iterations = new Integer(txtIterations.getText()).intValue();
        long WaitTime = new Long(txtWaitTimeRequest.getText()).longValue();
        for (int i = 0; i < iterations; i++) {
            try {
                raFile = new RandomAccessFile(requestLogFile, "r");
                while ((line = raFile.readLine()) != null) {
                    if (line.indexOf("Date:") > -1) {
                        StringBuffer bufRequest = new StringBuffer();
                        String server = null;
                        int port = 80;
                        txtStatus.append("Iteration: " + (i + 1) + ", Sending Request from " + line + System.getProperty("line.separator"));
                        txtStatus.setCaretPosition(txtStatus.getDocument().getLength());
                        while ((line = raFile.readLine()) != null) {
                            if (line.length() != 0) {
                                bufRequest.append(line + System.getProperty("line.separator"));
                                if (line.indexOf("Host:") > -1) {
                                    if (txtAlternateHost.getText().length() == 0) {
                                        String host = line.substring(new String("Host: ").length());
                                        server = host.substring(0, host.indexOf(":"));
                                        port = new Integer(host.substring(host.indexOf(":") + 1)).intValue();
                                    } else {
                                        server = txtAlternateHost.getText().substring(0, txtAlternateHost.getText().indexOf(":"));
                                        port = new Integer(txtAlternateHost.getText().substring(txtAlternateHost.getText().indexOf(":") + 1)).intValue();
                                    }
                                }
                            } else {
                                offset = raFile.getFilePointer();
                                if (raFile.readLine().length() == 0 || raFile.length() == offset) {
                                    try {
                                        Socket socReq = new Socket(server, port);
                                        OutputStream socReqOut = socReq.getOutputStream();
                                        DataOutputStream os = new DataOutputStream(socReqOut);
                                        DataInputStream is = new DataInputStream(socReq.getInputStream());
                                        os.writeBytes(bufRequest.toString());
                                        os.flush();
                                        try {
                                            String nextLine;
                                            int len = -1;
                                            while ((nextLine = is.readLine()) != null && nextLine.length() > 0) {
                                                if (nextLine.startsWith("Content-Length:")) len = Integer.parseInt(nextLine.substring(16));
                                                System.out.println(nextLine);
                                            }
                                            System.out.println();
                                            byte[] buf = new byte[512];
                                            int byteCount = len;
                                            while (byteCount > 0) {
                                                int b;
                                                if (byteCount < 512) b = is.read(buf, 0, byteCount); else b = is.read(buf, 0, 512);
                                                if (b == -1) break;
                                                System.out.write(buf, 0, b);
                                                byteCount -= b;
                                            }
                                        } catch (IOException e) {
                                        }
                                        System.out.println();
                                        is.close();
                                        os.close();
                                        socReqOut.close();
                                        socReq.close();
                                    } catch (IOException ioe) {
                                        System.err.println("An I/O Exception occured whilst writting a Request to the server");
                                        System.err.println(ioe.getMessage());
                                    }
                                    synchronized (this) {
                                        try {
                                            wait(WaitTime);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                    break;
                                } else {
                                    raFile.seek(offset);
                                    bufRequest.append(System.getProperty("line.separator"));
                                }
                            }
                        }
                    }
                }
                raFile.close();
            } catch (IOException ioe) {
                System.err.println("An I/O Exception occured whilst reading the Request file");
                btnStart.setEnabled(true);
                return;
            }
        }
        btnStart.setEnabled(true);
    }
