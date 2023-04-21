    public byte[] sign(Symbol signkey, SymbolList msgparts) throws FormatException {
        OutputStream _out = null;
        try {
            Object _key = signkey.getValue();
            if (_key instanceof PrivateKey) {
                Signature _sig = this.getSignature(signkey);
                _sig.initSign((PrivateKey) _key, this.getSecureRandom());
                _out = this.getLinkedSigningStream(_sig, signkey);
                ((Format) this).convert(msgparts, _out);
                return _sig.sign();
            } else if (_key instanceof SecretKey) {
                Mac _mac = this.getMac(signkey);
                _mac.init((SecretKey) _key);
                _out = this.getLinkedMACStream(_mac, signkey);
                ((Format) this).convert(msgparts, _out);
                return _mac.doFinal();
            } else if (_key instanceof String) {
                MessageDigest _md = MessageDigest.getInstance((String) _key);
                ByteArrayOutputStream _bout = new ByteArrayOutputStream();
                _out = new DigestOutputStream(_bout, _md);
                ((Format) this).convert(msgparts, _out);
                _out.flush();
                _bout.close();
                return _md.digest();
            } else {
                throw new FormatException(__me + ".sign(): Bogus key " + "value type " + _key.getClass().getName());
            }
        } catch (Exception e) {
            throw (FormatException) new FormatException().initCause(e);
        } finally {
            try {
                if (null != _out) {
                    _out.close();
                }
            } catch (IOException e) {
            }
        }
    }
