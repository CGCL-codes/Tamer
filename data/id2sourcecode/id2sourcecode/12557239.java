    public MessageDigestAttribute(byte[] message0, String digestAlg0) throws SMIMEException {
        super("ID_MESSAGEDIGEST", "NAME_STRING");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(digestAlg0);
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "constructor");
        }
        md.update(message0);
        DEROctetString dig = new DEROctetString(md.digest());
        DERSet digValue = new DERSet();
        digValue.addContent(dig.getDEREncoded());
        super.addContent(digValue.getDEREncoded());
    }
