    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        synchronized (entries) {
            if (password == null) {
                throw new IllegalArgumentException("password can't be null");
            }
            byte[] encoded;
            MessageDigest md = getPreKeyedHash(password);
            DataOutputStream dos = new DataOutputStream(new DigestOutputStream(stream, md));
            ObjectOutputStream oos = null;
            try {
                dos.writeInt(JCEKS_MAGIC);
                dos.writeInt(VERSION_2);
                dos.writeInt(entries.size());
                Enumeration e = entries.keys();
                while (e.hasMoreElements()) {
                    String alias = (String) e.nextElement();
                    Object entry = entries.get(alias);
                    if (entry instanceof PrivateKeyEntry) {
                        PrivateKeyEntry pentry = (PrivateKeyEntry) entry;
                        dos.writeInt(1);
                        dos.writeUTF(alias);
                        dos.writeLong(pentry.date.getTime());
                        dos.writeInt(pentry.protectedKey.length);
                        dos.write(pentry.protectedKey);
                        int chainLen;
                        if (pentry.chain == null) {
                            chainLen = 0;
                        } else {
                            chainLen = pentry.chain.length;
                        }
                        dos.writeInt(chainLen);
                        for (int i = 0; i < chainLen; i++) {
                            encoded = pentry.chain[i].getEncoded();
                            dos.writeUTF(pentry.chain[i].getType());
                            dos.writeInt(encoded.length);
                            dos.write(encoded);
                        }
                    } else if (entry instanceof TrustedCertEntry) {
                        dos.writeInt(2);
                        dos.writeUTF(alias);
                        dos.writeLong(((TrustedCertEntry) entry).date.getTime());
                        encoded = ((TrustedCertEntry) entry).cert.getEncoded();
                        dos.writeUTF(((TrustedCertEntry) entry).cert.getType());
                        dos.writeInt(encoded.length);
                        dos.write(encoded);
                    } else {
                        dos.writeInt(3);
                        dos.writeUTF(alias);
                        dos.writeLong(((SecretKeyEntry) entry).date.getTime());
                        oos = new ObjectOutputStream(dos);
                        oos.writeObject(((SecretKeyEntry) entry).sealedKey);
                    }
                }
                byte digest[] = md.digest();
                dos.write(digest);
                dos.flush();
            } finally {
                if (oos != null) {
                    oos.close();
                } else {
                    dos.close();
                }
            }
        }
    }
