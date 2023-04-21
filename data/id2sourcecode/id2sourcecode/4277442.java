        public void setPasscode(String code) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                String digest = hexStringFromBytes(md.digest(code.getBytes()));
                passcode_hash = digest;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
