    protected boolean fillLoginValues(XMLRPCLJ XMLObj) {
        XMLObj.m_username = m_szUname;
        XMLObj.m_ver = new Integer(1);
        if (m_bUseMD5) {
            if (!getchallenge()) {
                XMLObj.m_auth_method = "clear";
                XMLObj.m_hpassword = m_szPwd;
            } else {
                try {
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                    md.reset();
                    byte[] MD5PWD = md.digest((m_szChallenge + m_szPwd.toLowerCase()).getBytes());
                    String szMD5 = toHex(MD5PWD);
                    XMLObj.m_auth_method = "challenge";
                    XMLObj.m_auth_challenge = m_szChallenge;
                    XMLObj.m_auth_response = szMD5;
                } catch (java.security.NoSuchAlgorithmException e) {
                    System.err.println("No MD5 Encryption algorithm found.");
                    XMLObj.m_auth_method = "clear";
                    XMLObj.m_hpassword = m_szPwd;
                }
            }
        } else {
            XMLObj.m_auth_method = "clear";
            XMLObj.m_password = m_szPwd;
        }
        return true;
    }
