    private boolean download(URL url, File dst) {
        try {
            this.listener.notifyProcess(TuxGuitar.getProperty("jsa.soundbank-assistant.process.downloading"));
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length = 0;
            while (!isCancelled() && (length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.flush();
            os.close();
            return true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }
