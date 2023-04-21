    public File extract(File folder) throws ValidationException {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LOG.warning("Problem with creating extracting folder " + folder.toString());
                throw new ValidationException(ValidationExceptionCode.EXTRACTING_PROBLEM);
            }
        }
        File file = new File(folder, getName());
        FileOutputStream output = null;
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(getData());
            output = new FileOutputStream(file);
            byte[] buf = new byte[BUFFER_SIZE];
            int readed = 0;
            while ((readed = input.read(buf)) != -1) {
                output.write(buf, 0, readed);
            }
            output.flush();
        } catch (IOException e) {
            LOG.warning("Problem with extracting file: " + toDetailedString());
            throw new ValidationException(ValidationExceptionCode.EXTRACTING_PROBLEM);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Problem with closing output stream.", e);
                }
            }
        }
        data = null;
        return file;
    }
