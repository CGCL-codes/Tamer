    private void calculate() {
        final Thread calcThread = new Thread() {

            public void run() {
                try {
                    buffer = new byte[8192];
                    digest = new byte[8192];
                    statusProgressBar.setValue(0);
                    status.setText("Calculating, Please wait...");
                    getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    md = MessageDigest.getInstance(algorithm.getSelectedItem().toString());
                    if (userText.getText().equals("")) {
                        if (textMode) status.setText("Please fill in the text to be hashed..."); else status.setText("Please select the File to be hashed...");
                    } else {
                        if (textMode) {
                            buffer = userText.getText().getBytes();
                            md.update(buffer);
                            digest = md.digest();
                            hex = "";
                            int j = 0;
                            for (int i = 0; i < digest.length; i++) {
                                statusProgressBar.setValue(j += (i * 100) / digest.length);
                                statusProgressBar.update(statusProgressBar.getGraphics());
                                int b = digest[i] & 0xff;
                                if (Integer.toHexString(b).length() == 1) hex = hex + "0";
                                hex = hex + Integer.toHexString(b);
                            }
                            hashText.setText(algorithm.getSelectedItem().toString() + " Hash: " + hex);
                            copyString(hex);
                            status.setText(algorithm.getSelectedItem().toString() + " Hash copied to ClipBoard");
                        } else {
                            FileInputStream in = new FileInputStream(userText.getText());
                            int length = 0, total = in.available(), readPercentage = 0, lenRead = 0, calcPercentage = 0;
                            while ((length = in.read(buffer)) != -1) {
                                lenRead += length;
                                readPercentage = ((lenRead * 74) / total);
                                statusProgressBar.setValue(readPercentage);
                                statusProgressBar.update(statusProgressBar.getGraphics());
                                md.update(buffer, 0, length);
                            }
                            digest = md.digest();
                            hex = "";
                            for (int i = 0; i < digest.length; i++) {
                                calcPercentage = readPercentage + (((i + 1) * 26) / digest.length);
                                statusProgressBar.setValue(calcPercentage);
                                statusProgressBar.update(statusProgressBar.getGraphics());
                                int b = digest[i] & 0xff;
                                if (Integer.toHexString(b).length() == 1) hex = hex + "0";
                                hex = hex + Integer.toHexString(b);
                            }
                            hashText.setText(algorithm.getSelectedItem().toString() + " Hash: " + hex);
                            copyString(hex);
                            status.setText(algorithm.getSelectedItem().toString() + " Hash copied to ClipBoard");
                            in.close();
                        }
                        setMyMenuBarEnabled();
                        setMyToolBarButtonsEnabled();
                        setTextFieldContextMenuEnabled();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getRootPane().setCursor(Cursor.getDefaultCursor());
            }
        };
        try {
            calcThread.setDaemon(true);
            calcThread.start();
            calcThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setExtendedState(JFrame.NORMAL);
        toFront();
    }
