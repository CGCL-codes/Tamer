    private void handleFileMessage(IoSession session, Object message) {
        FileContent theMessage = (FileContent) message;
        LightLog.info("Received clipboard from: " + session.getRemoteAddress() + ", type=file, name=" + theMessage.getFileName());
        if (SystemUtils.IS_OS_UNIX) {
            handleFileMessageUnix(session, theMessage);
        } else {
            String tmpDirPath = System.getProperty("java.io.tmpdir");
            LightLog.info("Using tmp.dir: " + tmpDirPath);
            File tmpDir = new File(tmpDirPath);
            File f = new File(tmpDir, theMessage.getFileName());
            try {
                FileUtils.writeByteArrayToFile(f, theMessage.getBytes());
                File saveAs = FileChooserUtils.saveAs(null, f.getName());
                if (saveAs != null) {
                    try {
                        FileUtils.copyFile(f, saveAs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
