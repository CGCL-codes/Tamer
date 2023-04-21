        private static byte[] getFinished(ProtocolVersion protocolVersion, HandshakeHash handshakeHash, int sender, SecretKey masterKey) {
            byte[] sslLabel;
            String tlsLabel;
            if (sender == CLIENT) {
                sslLabel = SSL_CLIENT;
                tlsLabel = "client finished";
            } else if (sender == SERVER) {
                sslLabel = SSL_SERVER;
                tlsLabel = "server finished";
            } else {
                throw new RuntimeException("Invalid sender: " + sender);
            }
            MessageDigest md5Clone = handshakeHash.getMD5Clone();
            MessageDigest shaClone = handshakeHash.getSHAClone();
            if (protocolVersion.v >= ProtocolVersion.TLS10.v) {
                try {
                    byte[] seed = new byte[36];
                    md5Clone.digest(seed, 0, 16);
                    shaClone.digest(seed, 16, 20);
                    TlsPrfParameterSpec spec = new TlsPrfParameterSpec(masterKey, tlsLabel, seed, 12);
                    KeyGenerator prf = JsseJce.getKeyGenerator("SunTlsPrf");
                    prf.init(spec);
                    SecretKey prfKey = prf.generateKey();
                    if ("RAW".equals(prfKey.getFormat()) == false) {
                        throw new ProviderException("Invalid PRF output, format must be RAW");
                    }
                    byte[] finished = prfKey.getEncoded();
                    return finished;
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException("PRF failed", e);
                }
            } else {
                updateDigest(md5Clone, sslLabel, MD5_pad1, MD5_pad2, masterKey);
                updateDigest(shaClone, sslLabel, SHA_pad1, SHA_pad2, masterKey);
                byte[] finished = new byte[36];
                try {
                    md5Clone.digest(finished, 0, 16);
                    shaClone.digest(finished, 16, 20);
                } catch (DigestException e) {
                    throw new RuntimeException("Digest failed", e);
                }
                return finished;
            }
        }
