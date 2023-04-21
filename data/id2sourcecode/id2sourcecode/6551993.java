    private String hash(final String text) {
        BigInteger rv = BigInteger.ZERO;
        if (data.getSimpleHash()) {
            final char[] chars = text.toCharArray();
            for (final char c : chars) {
                rv = rv.add(new BigInteger("" + (int) c));
            }
        } else {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
                rv = new BigInteger(md.digest(text.getBytes("UTF-8")));
            } catch (final NoSuchAlgorithmException e) {
                LogUtil.logError(e);
            } catch (final UnsupportedEncodingException e) {
                LogUtil.logError(e);
            }
        }
        return rv.mod(data.getQ()).toString(Constants.HEXBASE);
    }
