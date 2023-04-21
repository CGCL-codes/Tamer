    public XMLlogin LogIn(String uName, String pwd, int iMaxMoodID) {
        XMLlogin login = new XMLlogin();
        if (!EntryFrame.m_bOnline) {
            m_szUname = uName;
            login.m_bSentOK = true;
            login.m_friendgroups = loadGroups();
            Object obj;
            obj = decode("journal.xml");
            if (obj != null) login.m_usejournals = (Vector) obj;
            obj = decode("pickw.xml");
            if (obj != null) login.m_pickws = (Vector) obj;
            return login;
        }
        XMLlogin.loginRequest loginRequest = new XMLlogin.loginRequest();
        loginRequest.m_username = uName;
        loginRequest.m_getmoods = new Integer(iMaxMoodID);
        loginRequest.m_getpickws = new Integer(1);
        loginRequest.m_getpickwurls = new Integer(1);
        loginRequest.m_getmenus = new Integer(1);
        String szMD5Pwd = new String();
        m_szUname = uName;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.reset();
            m_bytePwd = md.digest(pwd.getBytes());
            szMD5Pwd = toHex(m_bytePwd);
            if (!getchallenge()) {
                loginRequest.m_auth_method = "clear";
                loginRequest.m_hpassword = szMD5Pwd;
            } else {
                md.reset();
                byte[] MD5PWD = md.digest((m_szChallenge + szMD5Pwd.toLowerCase()).getBytes());
                String szMD5 = toHex(MD5PWD);
                loginRequest.m_auth_method = "challenge";
                loginRequest.m_auth_challenge = m_szChallenge;
                loginRequest.m_auth_response = szMD5;
            }
            m_bUseMD5 = true;
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println("No MD5 Encryption algorithm found.");
            loginRequest.m_auth_method = "clear";
            loginRequest.m_password = pwd;
        }
        if (m_bUseMD5) m_szPwd = szMD5Pwd; else m_szPwd = pwd;
        m_Request = loginRequest;
        m_Response = login;
        m_szRequestName = "LJ.XMLRPC.login";
        if (m_ProgDlg != null) {
            java.lang.Thread td = new java.lang.Thread(this);
            m_ProgDlg = new ProtProgress((java.awt.Frame) m_ProgDlg.getParent(), m_ProgDlg.isModal());
            m_ProgDlg.reset(0, DONE_STEP, InstallInfo.getString("progress.step.negotiate"), InstallInfo.getString("progress.title"), td);
            m_ProgDlg.show();
        } else {
            run();
        }
        m_bLoggedIn = login.m_bSentOK;
        if (login.m_bSentOK && EntryFrame.m_bOnline) {
            encode("groups.xml", login.getGroups());
            encode("journal.xml", login.m_usejournals);
            encode("pickw.xml", login.m_pickws);
        }
        m_Request = null;
        m_Response = null;
        if (login.m_fastserver != null) m_bFastServer = (((Integer) login.m_fastserver).intValue() == 1); else m_bFastServer = false;
        return login;
    }
