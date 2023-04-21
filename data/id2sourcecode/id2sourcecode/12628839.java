    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        synchronized (entries) {
            if (password == null) {
                throw new IllegalArgumentException("password can't be null");
            }
            byte[] encoded;
            MessageDigest md = getPreKeyedHash(password);
            DataOutputStream dos = new DataOutputStream(new DigestOutputStream(stream, md));
            dos.writeInt(MAGIC);
            dos.writeInt(VERSION_2);
            dos.writeInt(entries.size());
            for (Enumeration e = entries.keys(); e.hasMoreElements(); ) {
                String alias = (String) e.nextElement();
                Object entry = entries.get(alias);
                if (entry instanceof KeyEntry) {
                    dos.writeInt(1);
                    dos.writeUTF(alias);
                    dos.writeLong(((KeyEntry) entry).date.getTime());
                    dos.writeInt(((KeyEntry) entry).protectedPrivKey.length);
                    dos.write(((KeyEntry) entry).protectedPrivKey);
                    int chainLen;
                    if (((KeyEntry) entry).chain == null) {
                        chainLen = 0;
                    } else {
                        chainLen = ((KeyEntry) entry).chain.length;
                    }
                    dos.writeInt(chainLen);
                    for (int i = 0; i < chainLen; i++) {
                        encoded = ((KeyEntry) entry).chain[i].getEncoded();
                        dos.writeUTF(((KeyEntry) entry).chain[i].getType());
                        dos.writeInt(encoded.length);
                        dos.write(encoded);
                    }
                } else {
                    dos.writeInt(2);
                    dos.writeUTF(alias);
                    dos.writeLong(((TrustedCertEntry) entry).date.getTime());
                    encoded = ((TrustedCertEntry) entry).cert.getEncoded();
                    dos.writeUTF(((TrustedCertEntry) entry).cert.getType());
                    dos.writeInt(encoded.length);
                    dos.write(encoded);
                }
            }
            byte digest[] = md.digest();
            dos.write(digest);
            dos.flush();
        }
    }
