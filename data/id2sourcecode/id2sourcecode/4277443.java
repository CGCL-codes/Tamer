        public boolean checkPasscode(String code) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                String digest = hexStringFromBytes(md.digest(code.getBytes()));
                return passcode_hash.equals(digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return false;
        }
