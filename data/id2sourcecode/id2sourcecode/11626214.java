    private byte[] encryptContent(byte[] data, char[] password) throws IOException {
        byte[] encryptedData = null;
        AlgorithmParameters algParams = getAlgorithmParameters("PBEWithSHA1AndRC2_40");
        DerOutputStream bytes = new DerOutputStream();
        AlgorithmId algId = new AlgorithmId(pbeWithSHAAnd40BitRC2CBC_OID, algParams);
        algId.encode(bytes);
        byte[] encodedAlgId = bytes.toByteArray();
        try {
            SecretKey skey = getPBEKey(password);
            Cipher cipher = Cipher.getInstance("PBEWithSHA1AndRC2_40");
            cipher.init(Cipher.ENCRYPT_MODE, skey, algParams);
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            IOException ioe = new IOException("Failed to encrypt" + " safe contents entry: " + e);
            ioe.initCause(e);
            throw ioe;
        }
        DerOutputStream bytes2 = new DerOutputStream();
        bytes2.putOID(ContentInfo.DATA_OID);
        bytes2.write(encodedAlgId);
        DerOutputStream tmpout2 = new DerOutputStream();
        tmpout2.putOctetString(encryptedData);
        bytes2.writeImplicit(DerValue.createTag(DerValue.TAG_CONTEXT, false, (byte) 0), tmpout2);
        DerOutputStream out = new DerOutputStream();
        out.write(DerValue.tag_Sequence, bytes2);
        return out.toByteArray();
    }
