    private void createCipher() {
        try {
            String password = prefs.getString(PREF_PASSWORD, null);
            if (password != null) {
                String uniqueID = null;
                try {
                    AndroidID id = AndroidID.newInstance(this);
                    uniqueID = id.getAndroidID();
                } catch (Exception e1) {
                }
                if (uniqueID == null) uniqueID = SALT; else uniqueID = uniqueID.concat(SALT);
                byte[] uniqueIDBytes = uniqueID.getBytes(ENCODING);
                byte[] finalSalt = new byte[uniqueIDBytes.length + salt.length];
                for (int i = 0; i < uniqueIDBytes.length; i++) {
                    finalSalt[i] = uniqueIDBytes[i];
                }
                for (int j = 0; j < salt.length; j++) {
                    finalSalt[uniqueIDBytes.length + j] = salt[j];
                }
                MessageDigest hasher = MessageDigest.getInstance(SALT_HASH);
                for (int i = 0; i < KEY_ITERATION_COUNT; i++) finalSalt = hasher.digest(finalSalt);
                byte[] pwd = password.concat(uniqueID).getBytes(ENCODING);
                for (int i = 0; i < KEY_ITERATION_COUNT; i++) pwd = hasher.digest(pwd);
                PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();
                generator.init(pwd, finalSalt, KEY_ITERATION_COUNT);
                iv = ((ParametersWithIV) generator.generateDerivedParameters(KEY_SIZE, IV_SIZE));
                RijndaelEngine engine = new RijndaelEngine();
                cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
            } else {
                cipher = null;
                iv = null;
            }
        } catch (Exception e) {
            cipher = null;
            iv = null;
        }
    }
