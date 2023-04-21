    public boolean isPasswordValid(String plainTextPassword, String authenticationMechanism, Object authenticationData) {
        if (authenticationMechanism.startsWith("CRAM")) {
            try {
                JESMac jesMac = JESMac.getInstance("Hmac" + authenticationMechanism.substring(5));
                byte[] challengeBytes = (byte[]) authenticationData;
                byte[] key = password.startsWith("{ENC}") ? password.substring(5).getBytes("UTF-8") : password.getBytes("UTF-8");
                jesMac.init(new SecretKeySpec(key, "Hmac" + authenticationMechanism.substring(5)), new HMACParameterSpec());
                jesMac.update(challengeBytes, 0, challengeBytes.length);
                return Arrays.equals(jesMac.doFinal(), ByteUtils.toByteArray(plainTextPassword.toCharArray()));
            } catch (Exception ex) {
                log.error(ex);
                return false;
            }
        } else if (authenticationMechanism.startsWith("SCRAM")) {
            try {
                JESMac jesMac = JESMac.getInstance("Hmac" + authenticationMechanism.substring(6));
                SCRAMServerMode.AuthenticationData authenticationDataSCRAM = (SCRAMServerMode.AuthenticationData) authenticationData;
                MessageDigest md = JESMessageDigest.getInstance(authenticationMechanism.substring(6));
                byte[] clientKey;
                if (storedKey == null) {
                    String password = this.password.startsWith("{ENC}") ? this.password.substring(5) : this.password;
                    byte[] key = password.getBytes("UTF-8");
                    jesMac.init(new SecretKeySpec(key, "Hmac" + authenticationMechanism.substring(6)), new HMACParameterSpec());
                    byte[] saltedPassword = new byte[jesMac.getMacLength()];
                    PBKDF2 pbkdf2 = new PBKDF2();
                    pbkdf2.getDerivedKey(jesMac, authenticationDataSCRAM.getSalt(), authenticationDataSCRAM.getIteration(), saltedPassword);
                    jesMac.init(new SecretKeySpec(saltedPassword, "Hmac" + authenticationMechanism.substring(6)), new HMACParameterSpec());
                    clientKey = "Client Key".getBytes();
                    jesMac.update(clientKey, 0, clientKey.length);
                    clientKey = jesMac.doFinal();
                    storedKey = md.digest(clientKey);
                    jesMac.init(new SecretKeySpec(saltedPassword, "Hmac" + authenticationMechanism.substring(6)), new HMACParameterSpec());
                    serverKey = "Server Key".getBytes();
                    jesMac.update(serverKey, 0, serverKey.length);
                    serverKey = jesMac.doFinal();
                }
                jesMac.init(new SecretKeySpec(storedKey, "Hmac" + authenticationMechanism.substring(6)), new HMACParameterSpec());
                byte[] authMessageBytes = authenticationDataSCRAM.getAuthMessage().getBytes("UTF-8");
                jesMac.update(authMessageBytes, 0, authMessageBytes.length);
                clientKey = jesMac.doFinal();
                byte[] clientProof = plainTextPassword.getBytes("UTF-8");
                if (clientKey.length != clientProof.length) {
                    return false;
                }
                for (int i = 0; i < clientKey.length; i++) {
                    clientKey[i] ^= clientProof[i];
                }
                jesMac.init(new SecretKeySpec(serverKey, "Hmac" + authenticationMechanism.substring(6)), new HMACParameterSpec());
                jesMac.update(authMessageBytes, 0, authMessageBytes.length);
                serverSignature = jesMac.doFinal();
                return Arrays.equals(md.digest(clientKey), storedKey);
            } catch (Exception ex) {
                log.error(ex);
                return false;
            }
        } else {
            return false;
        }
    }
