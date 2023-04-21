    public static String getCurrentRealm() {
        if (gCURRENT_AUTH.equals(AUTH_TYPE.BASIC)) {
            if (gBASIC_REALM == null) {
                gBASIC_REALM = "BASIC realm=\"" + gREALM + "\"";
            }
            return gBASIC_REALM;
        } else {
            synchronized (MD_LOCK) {
                if (gDIGEST_REALM == null) {
                    gDIGEST_REALM = "Digest realm=\"" + gREALM + "\",qop=\"auth\",opaque=\"" + StringUtil.bytesToHexString(gMD.digest(gOPAQUE.getBytes())) + "\",nonce=\"";
                }
                final String nonce = gNONCE_PREFIX + System.nanoTime() + gNONCE_POSTFIX;
                return gDIGEST_REALM + StringUtil.bytesToHexString(nonce.getBytes()) + "\"";
            }
        }
    }
