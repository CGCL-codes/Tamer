    public final void identify(Actor http, long time) {
        if (http.identity == null || http.identity.length() == 0) {
            http.identity = Strings.random(10, Strings.ALPHANUMERIC);
        }
        String[] irtd2 = new String[] { http.identity, http.rights, String.valueOf(time), http.digested };
        http.digest = IRTD2.digest(irtd2, salts[0]);
        StringBuilder sb = new StringBuilder();
        sb.append(irtd2[0]);
        sb.append(' ');
        sb.append(irtd2[1]);
        sb.append(' ');
        sb.append(irtd2[2]);
        sb.append(' ');
        if (irtd2[3] != null) {
            sb.append(irtd2[3]);
        }
        sb.append(' ');
        sb.append(http.digest);
        sb.append(_qualifier);
        http.setCookie("IRTD2", sb.toString());
    }
