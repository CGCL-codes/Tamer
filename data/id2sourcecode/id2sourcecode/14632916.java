    public Data(InputStream dataIS, MetaData metaDataIn) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageDigest md = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM);
        int x = -1;
        while ((x = dataIS.read()) != -1) baos.write(x);
        this.dataReference = new DEROctetString(baos.toByteArray());
        this.metaData = metaDataIn;
        this.messageImprint = LTAPMessageDigest.digest(new DEROctetString(baos.toByteArray()));
    }
