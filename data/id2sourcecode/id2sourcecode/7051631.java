    private String getFileContent(File file) throws GLMRessourceManagerException {
        if (file == null || !file.exists()) throw new GLMRessourceFileException(1);
        int len = 0;
        byte[] buffer = ContentManager.getDefaultBuffer();
        try {
            BufferedInputStream buff_in = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
            while ((len = buff_in.read(buffer)) > 0) byte_out.write(buffer, 0, len);
            return new String(byte_out.toByteArray());
        } catch (Exception e) {
            throw new GLMRessourceFileException(2);
        }
    }
