        private static void updateSignature(Signature sig, ProtocolVersion protocolVersion, HandshakeHash handshakeHash, String algorithm, SecretKey masterKey) throws SignatureException {
            MessageDigest md5Clone = handshakeHash.getMD5Clone();
            MessageDigest shaClone = handshakeHash.getSHAClone();
            boolean tls = protocolVersion.v >= ProtocolVersion.TLS10.v;
            if (algorithm.equals("RSA")) {
                if (tls) {
                } else {
                    updateDigest(md5Clone, MD5_pad1, MD5_pad2, masterKey);
                    updateDigest(shaClone, SHA_pad1, SHA_pad2, masterKey);
                }
                RSASignature.setHashes(sig, md5Clone, shaClone);
            } else {
                if (tls) {
                } else {
                    updateDigest(shaClone, SHA_pad1, SHA_pad2, masterKey);
                }
                sig.update(shaClone.digest());
            }
        }
