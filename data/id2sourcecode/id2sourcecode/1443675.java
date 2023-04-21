    private final boolean verify() {
        if (this._signatureSeq == null) {
            return false;
        }
        try {
            _mdigest.reset();
            _mdigest.update(this.secret());
            this._sign.initVerify(this._creatorCert.getPublicKey());
            this._sign.update(_mdigest.digest());
            return this._sign.verify(this.getSignature());
        } catch (InvalidKeyException excpt) {
            System.err.println(excpt.toString());
            return false;
        } catch (SignatureException excpt) {
            System.err.println(excpt.toString());
            return false;
        }
    }
