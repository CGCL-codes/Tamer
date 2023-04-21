    private boolean isSameContent(DelayedOutputStream stream, Target target) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(stream.getContent());
            String streamDigest = SourceUtil.encodeBASE64(new String(md5.digest()));
            String targetDigest = (String) checksums.get(target.getSourceURI());
            if (streamDigest.equals(targetDigest)) {
                return true;
            } else {
                checksums.put(target.getSourceURI(), streamDigest);
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
