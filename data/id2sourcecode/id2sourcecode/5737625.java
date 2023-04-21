    public String generate() {
        String lResult = null;
        SHA lSha = new SHA();
        byte[] lRndbuf = new byte[100];
        rnd.nextBytes(lRndbuf);
        lSha.update(lRndbuf, 0, lRndbuf.length);
        int lIters;
        if (isReadable) {
            lIters = length * 3;
        } else {
            lIters = (int) Math.round(Math.ceil((double) length / 20.0));
        }
        byte[] lPwdCandidate = new byte[lIters * 20];
        while ((lResult == null) || (lResult.length() < length)) {
            for (int i = 0; i < lIters; i++) {
                lSha.update(entropy[i % entropy.length]);
                lSha.update((byte) i);
                lSha.update((byte) rnd.nextInt());
                byte[] lDigest = lSha.digest();
                System.arraycopy(lDigest, 0, lPwdCandidate, i * 20, lDigest.length);
            }
            StringBuilder lPwdBuf = new StringBuilder();
            for (int i = 0; i < lPwdCandidate.length; i++) {
                int lSrc = lPwdCandidate[i] % alphabet.length();
                if (lSrc < 0) lSrc *= -1;
                char lPwdChar = alphabet.charAt(lSrc);
                lPwdBuf.append(lPwdChar);
            }
            if (isReadable) {
                String lOldTrial = null;
                String lNewTrial = lPwdBuf.toString();
                while (!lNewTrial.equals(lOldTrial)) {
                    lOldTrial = lNewTrial;
                    lNewTrial = lNewTrial.replaceAll("([aeiou][aeiou])[aeiou]", "$1");
                    lNewTrial = lNewTrial.replaceAll("([^aeiou])[^aeiou]", "$1");
                    lNewTrial = lNewTrial.replaceAll("ii", "i");
                    lNewTrial = lNewTrial.replaceAll("uo", "u");
                    lNewTrial = lNewTrial.replaceAll("ue", "u");
                    lNewTrial = lNewTrial.replaceAll("eo", "e");
                    lNewTrial = lNewTrial.replaceAll("ua", "a");
                }
                lResult = lNewTrial;
            } else {
                lResult = lPwdBuf.toString();
            }
        }
        return lResult.substring(0, length);
    }
