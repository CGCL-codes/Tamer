    private MimeBodyPart encrypt(MimeMessage msg) {
        try {
            SMIMEEnvelopedGenerator genE = new SMIMEEnvelopedGenerator();
            genE.addKeyTransRecipient(rcptCert);
            MessageDigest dig = MessageDigest.getInstance("SHA1", "BC");
            dig.update(rcptCert.getPublicKey().getEncoded());
            genE.addKeyTransRecipient(rcptCert.getPublicKey(), dig.digest());
            MimeBodyPart mpp = genE.generate(msg, SMIMEEnvelopedGenerator.DES_EDE3_CBC, "BC");
            return mpp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
