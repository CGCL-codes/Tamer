        private static byte[] hash(String article) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                return md5.digest(article.getBytes());
            } catch (NoSuchAlgorithmException nsae) {
                throw new Error(nsae);
            }
        }
