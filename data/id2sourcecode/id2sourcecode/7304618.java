        public Cipher create(boolean encryptMode) {
            try {
                int mode = (encryptMode) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
                Cipher cipher = Cipher.getInstance(algorithm);
                String keyAlgorithm = algorithm;
                if (algorithm.indexOf('/') != -1) {
                    keyAlgorithm = algorithm.substring(0, algorithm.indexOf('/'));
                }
                ByteBuffer bbPass = ByteBuffer.allocate(32);
                MessageDigest md = MessageDigest.getInstance("MD5");
                bbPass.put(md.digest(passPhrase.getBytes()));
                md.reset();
                byte[] saltDigest = md.digest(salt);
                bbPass.put(saltDigest);
                boolean isCBC = algorithm.indexOf("/CBC/") != -1;
                SecretKey key = null;
                int ivLength = 8;
                AlgorithmParameterSpec paramSpec = null;
                if (keyBytes == null) {
                    keyBytes = bbPass.array();
                }
                if (algorithm.startsWith("AES")) {
                    ivLength = 16;
                    key = new SecretKeySpec(keyBytes, "AES");
                } else if (algorithm.startsWith("Blowfish")) {
                    key = new SecretKeySpec(keyBytes, "Blowfish");
                } else if (algorithm.startsWith("DESede")) {
                    KeySpec keySpec = new DESedeKeySpec(keyBytes);
                    key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
                } else if (algorithm.startsWith("DES")) {
                    KeySpec keySpec = new DESKeySpec(keyBytes);
                    key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
                } else if (algorithm.startsWith("PBEWith")) {
                    paramSpec = new PBEParameterSpec(salt, iterationCount);
                    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
                    key = SecretKeyFactory.getInstance(keyAlgorithm).generateSecret(keySpec);
                }
                if (isCBC) {
                    byte[] iv = (ivLength == 8) ? salt : saltDigest;
                    paramSpec = new IvParameterSpec(iv);
                }
                cipher.init(mode, key, paramSpec);
                return cipher;
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            return null;
        }
