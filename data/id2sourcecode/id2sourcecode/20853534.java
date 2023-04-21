    public void perform() throws ActionProcessingException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int readed;
            long count = 0;
            while ((readed = inputStream.read(buffer)) > 0) {
                bos.write(buffer, 0, readed);
                count += readed;
                if (limit > 0 && count > limit) {
                    throw new ActionProcessingException("Exceeded read limit: " + limit);
                }
            }
            inputStream.close();
            bos.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            throw new ActionProcessingException(e);
        }
    }
