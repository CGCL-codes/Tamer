        public String getDigest(String key) throws UnsupportedEncodingException {
            return TranscodeUtil.binToHex(md.digest(key.getBytes("UTF-8")));
        }
