            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(getShell());
                String filePath = fileDialog.open();
                FileInputStream fis = null;
                ByteArrayOutputStream baos = null;
                if (filePath != null) {
                    try {
                        fis = new FileInputStream(filePath);
                        baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int read = fis.read(buffer, 0, 1024);
                        while (read > -1) {
                            baos.write(buffer, 0, read);
                            read = fis.read(buffer, 0, 1024);
                        }
                        messageData.setBinaryData(baos.toByteArray());
                        initFromMessageData();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        closeQuitely(fis);
                        closeQuitely(baos);
                    }
                }
            }
