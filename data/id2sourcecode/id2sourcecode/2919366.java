    public boolean verify() throws SignatureException {
        if (this.verified) return this.verifyResult;
        if (this.sigAttr != null) {
            this.sig.update(this.sigAttr);
            if (this.RSAdata != null) {
                byte msd[] = this.messageDigest.digest();
                this.messageDigest.update(msd);
            }
            this.verifyResult = (Arrays.equals(this.messageDigest.digest(), this.digestAttr) && this.sig.verify(this.digest));
        } else {
            if (this.RSAdata != null) this.sig.update(this.messageDigest.digest());
            this.verifyResult = this.sig.verify(this.digest);
        }
        this.verified = true;
        return this.verifyResult;
    }
