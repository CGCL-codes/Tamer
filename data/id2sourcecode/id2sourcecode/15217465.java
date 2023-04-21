        private void generateIntegrityKeyPair(boolean clientMode) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException {
            byte[] cimagic = CLIENT_INT_MAGIC.getBytes(encoding);
            byte[] simagic = SVR_INT_MAGIC.getBytes(encoding);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] keyBuffer = new byte[H_A1.length + cimagic.length];
            System.arraycopy(H_A1, 0, keyBuffer, 0, H_A1.length);
            System.arraycopy(cimagic, 0, keyBuffer, H_A1.length, cimagic.length);
            md5.update(keyBuffer);
            byte[] Kic = md5.digest();
            System.arraycopy(simagic, 0, keyBuffer, H_A1.length, simagic.length);
            md5.update(keyBuffer);
            byte[] Kis = md5.digest();
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST12:Kic: ", Kic);
                traceOutput(DI_CLASS_NAME, "generateIntegrityKeyPair", "DIGEST13:Kis: ", Kis);
            }
            if (clientMode) {
                myKi = Kic;
                peerKi = Kis;
            } else {
                myKi = Kis;
                peerKi = Kic;
            }
        }
