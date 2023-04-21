    private void onDhGroupExchangeReply(final SshDhGroupExchangeReply msg) throws GeneralSecurityException, IOException {
        if ((this.keyPair == null) || this.connection.isServer()) throw new SshException("%s: unexpected %s", this.connection.uri, msg.getType());
        if ((msg.f.signum() <= 0) || (msg.f.compareTo(this.paramSpec.getP()) >= 0)) throw new SshException("%s: illegal f:\nf = %s\np = %s", this.connection.uri, msg.f, this.paramSpec.getP());
        final BigInteger k;
        {
            k = msg.f.modPow(((DHPrivateKey) this.keyPair.getPrivate()).getX(), this.paramSpec.getP());
            if ((k.compareTo(BigInteger.ONE) < 0) || (k.compareTo(this.paramSpec.getP()) >= 0)) {
                throw new SshException("%s: illegal key:\nk = %s\np = %s\ng = %s\nx = %s\ny = %s", this.connection.uri, k, this.paramSpec.getP(), this.paramSpec.getG(), ((DHPrivateKey) this.keyPair.getPrivate()).getX(), ((DHPublicKey) this.keyPair.getPublic()).getY());
            }
        }
        final byte[] h;
        {
            final MessageDigest md = createMessageDigest();
            updateString(md, SshVersion.LOCAL.toString());
            updateString(md, this.connection.getRemoteSshVersion().toString());
            updateByteArray(md, this.keyExchangeInitLocal.getPayload());
            updateByteArray(md, this.keyExchangeInitRemote.getPayload());
            updateByteArray(md, msg.hostKey);
            updateInt(md, this.dhGroupExchangeParameters.minLength);
            updateInt(md, this.dhGroupExchangeParameters.preferredLength);
            updateInt(md, this.dhGroupExchangeParameters.maxLength);
            updateBigInt(md, this.paramSpec.getP());
            updateBigInt(md, this.paramSpec.getG());
            updateBigInt(md, ((DHPublicKey) this.keyPair.getPublic()).getY());
            updateBigInt(md, msg.f);
            updateBigInt(md, k);
            h = md.digest();
        }
        if (this.sessionId == null) this.sessionId = h;
        this.h = h;
        this.k = k;
        this.connection.send(new SshKeyExchangeNewKeys());
    }
