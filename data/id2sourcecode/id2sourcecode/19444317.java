    public final boolean equals(Object obj) {
        if (obj instanceof MessageDigestInputStream) {
            return MessageDigest.isEqual(digest(), ((MessageDigestInputStream) obj).digest());
        } else if (obj instanceof InputStream) {
            MessageDigestInputStream mdi = null;
            try {
                mdi = new MessageDigestInputStream((InputStream) obj, algorithm, provider);
                return MessageDigest.isEqual(digest(), mdi.digest());
            } catch (NoSuchAlgorithmException e) {
            } catch (NoSuchProviderException e) {
            } catch (IOException e) {
            } finally {
            }
        }
        return false;
    }
