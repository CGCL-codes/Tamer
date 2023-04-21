    public static byte[] verifyResourceData(byte[] signedData, KeyStore trustStore) throws IOException, SignServerException, SignatureException {
        byte[] retval = null;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(signedData));
        boolean isSigned = dis.readBoolean();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (dis.available() != 0) {
            baos.write(dis.read());
        }
        retval = baos.toByteArray();
        if (isSigned) {
            try {
                retval = verifySignature(getCACertificatesFromKeyStore(trustStore), null, retval, new Date());
            } catch (KeyStoreException e) {
                throw new IOException("Error reading certificates from truststore : " + e.getMessage());
            }
        } else {
            if (trustStore != null) {
                throw new SignServerException("Error: all resource data in the ClusterClassLoader have to be signed.");
            }
        }
        return retval;
    }
