    protected ImageIcon getPicture(final File file) {
        final File[] scrnshts = getScreenshots((File) fileBrowser.getModel().getRoot(), file);
        if (scrnshts.length > 0) {
            File theScreenShot = scrnshts[0];
            InputStream is = null;
            try {
                if (theScreenShot instanceof ZipEntryFileProxy) {
                    theScreenShot = ZipEntryFileProxy.extractFromZip((ZipEntryFileProxy) theScreenShot);
                }
                if (theScreenShot.getName().endsWith(".gz")) {
                    theScreenShot = ZipEntryFileProxy.extractFromGZ(theScreenShot);
                }
                is = new FileInputStream(theScreenShot);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] bytes = new byte[(int) theScreenShot.length()];
                int readBytes = 0, complete = 0;
                do {
                    readBytes = is.read(bytes, complete, bytes.length - complete);
                    if (readBytes > 0) {
                        os.write(bytes, complete, readBytes);
                        complete += readBytes;
                    }
                } while (readBytes > 0);
                return new ImageIcon(os.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
