    private final void sign(PrivateKey key) {
        if (key == null) {
            System.err.println("Sharedsecret.sign(): null private key");
            return;
        }
        try {
            _mdigest.reset();
            _mdigest.update(this.secret());
            this._sign.initSign(key);
            this._sign.update(_mdigest.digest());
            this.setSignature(this._sign.sign());
        } catch (InvalidKeyException excpt) {
            System.err.println(excpt.toString());
            return;
        } catch (SignatureException excpt) {
            System.err.println(excpt.toString());
            return;
        }
    }
