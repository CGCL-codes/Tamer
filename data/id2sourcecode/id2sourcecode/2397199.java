    DataInputStream getStream(String command, String[] args) {
        long start = printOps ? new Date().getTime() : 0;
        DataInputStream in = null;
        HttpURLConnection conn = null;
        status = null;
        StringBuffer s = null;
        try {
            s = new StringBuffer();
            s.append("?command=").append(command);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        String arg = URLEncoder.encode(args[i], "UTF-8");
                        s.append("&arg").append(i + 1).append("=").append(arg);
                    }
                }
            }
            String actionString = flushUserActions();
            if (actionString != null) {
                assert actionString.length() > 0;
                String encodedActions = URLEncoder.encode(actionString, "UTF-8");
                assert encodedActions.length() > 0;
                if (encodedActions.length() > 0) s.append("&userActions=").append(encodedActions);
            }
            if (sessionID != null) s.append("&session=").append(sessionID);
            String url = s.toString();
            if (printOps) {
                System.out.println(URLDecoder.decode(url, "UTF-8"));
                if (SwingUtilities.isEventDispatchThread()) {
                    System.err.println("Calling ServletInterface in event dispatch thread! " + url);
                }
            }
            conn = (HttpURLConnection) (new URL(host + url)).openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-length", "0");
            in = new DataInputStream(new InflaterInputStream(new BufferedInputStream(conn.getInputStream())));
        } catch (Throwable e) {
            if (conn != null) try {
                status = conn.getResponseMessage();
            } catch (IOException nested) {
                nested.printStackTrace();
            }
            if (status == null) status = e.toString();
        }
        if (status == null && conn != null) {
            try {
                status = conn.getResponseMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (status.equals("OK")) status = null;
        if (status != null) System.err.println("\ngetStream status: " + status + "\nin response to " + s + "\n");
        if (printOps) System.out.println(command + " took " + (new Date().getTime() - start) + "ms");
        return in;
    }
