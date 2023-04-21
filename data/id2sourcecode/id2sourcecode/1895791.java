    public WinstoneSession makeNewSession() {
        final String cookieValue = "Winstone_" + remoteIP + "_" + serverPort + "_" + System.currentTimeMillis() + WinstoneRequest.rnd.nextLong();
        final byte digestBytes[] = md5Digester.digest(cookieValue.getBytes());
        final char outArray[] = new char[32];
        for (int n = 0; n < digestBytes.length; n++) {
            final int hiNibble = (digestBytes[n] & 0xFF) >> 4;
            final int loNibble = (digestBytes[n] & 0xF);
            outArray[2 * n] = (hiNibble > 9 ? (char) (hiNibble + 87) : (char) (hiNibble + 48));
            outArray[(2 * n) + 1] = (loNibble > 9 ? (char) (loNibble + 87) : (char) (loNibble + 48));
        }
        final String newSessionId = new String(outArray);
        currentSessionIds.put(webappConfig.getContextPath(), newSessionId);
        return webappConfig.makeNewSession(newSessionId);
    }
