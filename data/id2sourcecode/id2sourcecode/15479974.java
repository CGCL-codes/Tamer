    public boolean setPassphrase(byte[] _passphrase) throws JSchException {
        try {
            if (encrypted) {
                if (_passphrase == null) return false;
                byte[] passphrase = _passphrase;
                int hsize = hash.getBlockSize();
                byte[] hn = new byte[key.length / hsize * hsize + (key.length % hsize == 0 ? 0 : hsize)];
                byte[] tmp = null;
                if (keytype == OPENSSH) {
                    for (int index = 0; index + hsize <= hn.length; ) {
                        if (tmp != null) {
                            hash.update(tmp, 0, tmp.length);
                        }
                        hash.update(passphrase, 0, passphrase.length);
                        hash.update(iv, 0, iv.length);
                        tmp = hash.digest();
                        System.arraycopy(tmp, 0, hn, index, tmp.length);
                        index += tmp.length;
                    }
                    System.arraycopy(hn, 0, key, 0, key.length);
                } else if (keytype == FSECURE) {
                    for (int index = 0; index + hsize <= hn.length; ) {
                        if (tmp != null) {
                            hash.update(tmp, 0, tmp.length);
                        }
                        hash.update(passphrase, 0, passphrase.length);
                        tmp = hash.digest();
                        System.arraycopy(tmp, 0, hn, index, tmp.length);
                        index += tmp.length;
                    }
                    System.arraycopy(hn, 0, key, 0, key.length);
                }
                Util.bzero(passphrase);
            }
            if (decrypt()) {
                encrypted = false;
                return true;
            }
            P_array = Q_array = G_array = pub_array = prv_array = null;
            return false;
        } catch (Exception e) {
            if (e instanceof JSchException) throw (JSchException) e;
            if (e instanceof Throwable) throw new JSchException(e.toString(), (Throwable) e);
            throw new JSchException(e.toString());
        }
    }
