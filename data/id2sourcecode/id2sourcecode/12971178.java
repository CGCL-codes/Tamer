    public void sign() throws GeneralSecurityException {
        Attribute attribute;
        byte[] b;
        if (twostep_) {
            b = digest_.digest();
            attribute = new Attribute((ASN1ObjectIdentifier) MESSAGE_DIGEST.clone(), new ASN1OctetString(b));
            info_.addAuthenticatedAttribute(attribute);
            info_.update(sig_);
        }
        if (target_ instanceof SignedAndEnvelopedData) {
            SignedAndEnvelopedData saed;
            byte[] edig;
            saed = (SignedAndEnvelopedData) target_;
            edig = saed.encryptBulkData(sig_.sign());
            info_.setEncryptedDigest(edig);
        } else {
            info_.setEncryptedDigest(sig_.sign());
        }
        target_.addSignerInfo(info_);
    }
