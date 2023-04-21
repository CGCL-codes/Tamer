    public byte[] encryptChatMsg(String chat, String msg) throws SecureSessionException {
        try {
            byte[] dataToEncrypt = getCmsSignedBlock(msg);
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            Cipher c = Cipher.getInstance("2.16.840.1.101.3.4.1.42", "BC");
            c.init(Cipher.ENCRYPT_MODE, getChatKey(chat), new IvParameterSpec(iv));
            byte[] encrypted = c.doFinal(dataToEncrypt);
            EncryptedContentInfo eci = new EncryptedContentInfo(new DERObjectIdentifier("1.2.840.113549.1.7.1"), new AlgorithmIdentifier(new DERObjectIdentifier("2.16.840.1.101.3.4.1.42"), new DEROctetString(iv)), new BERConstructedOctetString(encrypted));
            EncryptedData ed = new EncryptedData(eci, null);
            BERTaggedObject bert = new BERTaggedObject(0, ed.getDERObject());
            DERObjectIdentifier rootid = new DERObjectIdentifier("1.2.840.113549.1.7.6");
            ASN1EncodableVector vec = new ASN1EncodableVector();
            vec.add(rootid);
            vec.add(bert);
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            ASN1OutputStream out = new ASN1OutputStream(fout);
            out.writeObject(new BERSequence(vec));
            out.close();
            return fout.toByteArray();
        } catch (Exception e) {
            throw new SecureSessionException(e);
        }
    }
