    public void openConnection(String url, String user, String pwd, boolean tracing, String version, String referralType, boolean useSSL) {
        if (referralType == null) referralType = "follow";
        if ("ignorefollowthrow".indexOf(referralType) == -1) {
            error("unknown referraltype " + referralType, null);
            referralType = "follow";
        }
        if (url.toLowerCase().startsWith("ldap://") == false) url = "ldap://" + url;
        try {
            if (debug) out.println("opening connection with :\n url: " + url + "\n user: " + user + "\n pwd: " + pwd + "\n tracing: " + tracing + "\n version: " + version + "\n referral: " + referralType);
            if ((url == null) || (url.length() == 0)) {
                error("unable to open connection - no url supplied", null);
                return;
            }
            if ((version == null) || (version.length() == 0)) version = "3";
            char[] password = (pwd == null || pwd.length() == 0) ? null : pwd.toCharArray();
            int versn = Integer.parseInt(version);
            ConnectionData cData = new ConnectionData(versn, url, user, password, tracing, null, null);
            DirContext ctx = BasicOps.openContext(cData);
            if (ctx != null) myOps = new DXOps(ctx);
            if (myOps == null) error("unable to open connection " + url, null); else if (debug) out.println("connection " + url + " open. ");
        } catch (Exception e) {
            error("error opening connection " + url + "\n  ", e);
        }
    }
