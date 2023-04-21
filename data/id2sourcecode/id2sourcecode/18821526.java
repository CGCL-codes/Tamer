    private byte[] calculateInfoHash(Map<?, ?> infoMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BEncoder.getEncoder(baos, true, false, "UTF-8").encodeDict(infoMap);
        } catch (IOException ioe) {
            ErrorService.error(ioe);
        }
        MessageDigest md = new SHA1();
        return md.digest(baos.toByteArray());
    }
