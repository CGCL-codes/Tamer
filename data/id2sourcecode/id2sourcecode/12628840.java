    public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        synchronized (entries) {
            DataInputStream dis;
            MessageDigest md = null;
            CertificateFactory cf = null;
            Hashtable cfs = null;
            ByteArrayInputStream bais = null;
            byte[] encoded = null;
            if (stream == null) return;
            if (password != null) {
                md = getPreKeyedHash(password);
                dis = new DataInputStream(new DigestInputStream(stream, md));
            } else {
                dis = new DataInputStream(stream);
            }
            int xMagic = dis.readInt();
            int xVersion = dis.readInt();
            if (xMagic != MAGIC || (xVersion != VERSION_1 && xVersion != VERSION_2)) {
                throw new IOException("Invalid keystore format");
            }
            if (xVersion == VERSION_1) {
                cf = CertificateFactory.getInstance("X509");
            } else {
                cfs = new Hashtable(3);
            }
            entries.clear();
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                int tag;
                String alias;
                tag = dis.readInt();
                if (tag == 1) {
                    KeyEntry entry = new KeyEntry();
                    alias = dis.readUTF();
                    entry.date = new Date(dis.readLong());
                    try {
                        entry.protectedPrivKey = new byte[dis.readInt()];
                    } catch (OutOfMemoryError e) {
                        throw new IOException("Keysize too big");
                    }
                    dis.readFully(entry.protectedPrivKey);
                    int numOfCerts = dis.readInt();
                    try {
                        if (numOfCerts > 0) {
                            entry.chain = new Certificate[numOfCerts];
                        }
                    } catch (OutOfMemoryError e) {
                        throw new IOException("Too many certificates in chain");
                    }
                    for (int j = 0; j < numOfCerts; j++) {
                        if (xVersion == 2) {
                            String certType = dis.readUTF();
                            if (cfs.containsKey(certType)) {
                                cf = (CertificateFactory) cfs.get(certType);
                            } else {
                                cf = CertificateFactory.getInstance(certType);
                                cfs.put(certType, cf);
                            }
                        }
                        try {
                            encoded = new byte[dis.readInt()];
                        } catch (OutOfMemoryError e) {
                            throw new IOException("Certificate too big");
                        }
                        dis.readFully(encoded);
                        bais = new ByteArrayInputStream(encoded);
                        entry.chain[j] = cf.generateCertificate(bais);
                        bais.close();
                    }
                    entries.put(alias, entry);
                } else if (tag == 2) {
                    TrustedCertEntry entry = new TrustedCertEntry();
                    alias = dis.readUTF();
                    entry.date = new Date(dis.readLong());
                    if (xVersion == 2) {
                        String certType = dis.readUTF();
                        if (cfs.containsKey(certType)) {
                            cf = (CertificateFactory) cfs.get(certType);
                        } else {
                            cf = CertificateFactory.getInstance(certType);
                            cfs.put(certType, cf);
                        }
                    }
                    try {
                        encoded = new byte[dis.readInt()];
                    } catch (OutOfMemoryError e) {
                        throw new IOException("Certificate too big");
                    }
                    dis.readFully(encoded);
                    bais = new ByteArrayInputStream(encoded);
                    entry.cert = cf.generateCertificate(bais);
                    bais.close();
                    entries.put(alias, entry);
                } else {
                    throw new IOException("Unrecognized keystore entry");
                }
            }
            if (password != null) {
                byte computed[], actual[];
                computed = md.digest();
                actual = new byte[computed.length];
                dis.readFully(actual);
                for (int i = 0; i < computed.length; i++) {
                    if (computed[i] != actual[i]) {
                        throw new IOException("Keystore was tampered with, or " + "password was incorrect");
                    }
                }
            }
        }
    }
