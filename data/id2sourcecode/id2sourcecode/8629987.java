    private static String mashPassword(String[] answers) {
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < answers.length; i++) {
            tmp.append(answers[i].trim());
        }
        StringBuffer mix = new StringBuffer();
        int pos = tmp.length() - 1;
        for (int i = 0; i <= pos; i++) {
            mix.append(tmp.charAt(i));
            if (i < pos) mix.append(tmp.charAt(pos));
            pos--;
        }
        String internalPassword;
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] digested = digest.digest(mix.toString().getBytes());
            internalPassword = EncodingUtils.encodeBase64(digested);
        } catch (NoSuchAlgorithmException e) {
            String msg = NLS.bind(SecAuthMessages.noDigest, DIGEST_ALGORITHM);
            AuthPlugin.getDefault().logMessage(msg);
            internalPassword = mix.toString();
        }
        return internalPassword;
    }
