    public ByteBlock genChatSecurityInfo(FullRoomInfo chatInfo, String sn) throws SecureSessionException {
        try {
            SecretKey key = getChatKey(chatInfo.getRoomName());
            byte[] keyData = key.getEncoded();
            Cipher c = Cipher.getInstance("1.2.840.113549.1.1.1", "BC");
            X509Certificate cert = getCert(sn);
            c.init(Cipher.ENCRYPT_MODE, cert);
            byte[] encryptedKey = c.doFinal(keyData);
            X509Name xname = new X509Name(cert.getSubjectDN().getName());
            IssuerAndSerialNumber ias = new IssuerAndSerialNumber(xname, cert.getSerialNumber());
            KeyTransRecipientInfo ktr = new KeyTransRecipientInfo(new RecipientIdentifier(ias), new AlgorithmIdentifier("1.2.840.113549.1.1.1"), new DEROctetString(encryptedKey));
            ASN1EncodableVector vec = new ASN1EncodableVector();
            vec.add(ktr);
            vec.add(new DERObjectIdentifier("2.16.840.1.101.3.4.1.42"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ASN1OutputStream aout = new ASN1OutputStream(bout);
            aout.writeObject(new DERSequence(vec));
            aout.close();
            ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
            new MiniRoomInfo(chatInfo).write(bout2);
            BinaryTools.writeUShort(bout2, bout.size());
            bout.writeTo(bout2);
            return ByteBlock.wrap(signData(bout2.toByteArray()));
        } catch (Exception e) {
            throw new SecureSessionException(e);
        }
    }
