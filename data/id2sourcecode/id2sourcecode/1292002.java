    private void cmd_file() {
        Media file = null;
        try {
            file = Fileupload.get(true);
            if (file == null) return;
        } catch (InterruptedException e) {
            log.warning(e.getLocalizedMessage());
            return;
        }
        FileOutputStream fos = null;
        String fileName = null;
        try {
            File tempFile = File.createTempFile("compiere_", "_" + file.getName());
            fileName = tempFile.getAbsolutePath();
            fos = new FileOutputStream(tempFile);
            byte[] bytes = null;
            if (file.inMemory()) {
                bytes = file.getByteData();
            } else {
                InputStream is = file.getStreamData();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1000];
                int byteread = 0;
                while ((byteread = is.read(buf)) != -1) baos.write(buf, 0, byteread);
                bytes = baos.toByteArray();
            }
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return;
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
            }
        }
        getComponent().setText(fileName);
    }
