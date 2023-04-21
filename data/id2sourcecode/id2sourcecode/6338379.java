            Object execute(byte[] input, String algorithm) throws Exception {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(input);
                return md.digest();
            }
