    void doOpenUserGuideUrl(boolean online) {
        Program browser = Program.findProgram("html");
        if (browser == null) {
            MessageBox box = new MessageBox(sShell, SWT.ICON_WARNING | SWT.OK);
            box.setText("Browser not found");
            box.setMessage("Could not find a browser program to open html files.\n" + "Visit javahexeditor.sourceforge.net/userGuide.html to see the User Guide.\n");
            box.open();
            return;
        }
        String fileName = "userGuide.html";
        if (online) {
            fileName = "javahexeditor.sourceforge.net/" + fileName;
        } else {
            File theFile = new File(fileName);
            if (!theFile.exists()) {
                InputStream inStream = ClassLoader.getSystemResourceAsStream(fileName);
                if (inStream != null) {
                    try {
                        FileOutputStream outStream = new FileOutputStream(theFile);
                        byte[] buffer = new byte[512];
                        int read = 0;
                        try {
                            while ((read = inStream.read(buffer)) > 0) {
                                outStream.write(buffer, 0, read);
                            }
                        } finally {
                            outStream.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        inStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        browser.execute(fileName);
    }
