    private synchronized String[] readURL(final String action, String param) {
        assert ((param == null) || (param.trim().length() > 0));
        if (param == null) param = "";
        final String fullParam = "action=" + action + param + ((sessionid == null) ? "" : "&" + sessionName + "=" + sessionid);
        udebug.dprintln("PUT value: " + fullParam);
        if (udebug.getVerbose()) {
            udebug.vprintln("Who is making us slow down?\n\n" + "Stack trace -- NOT an error:\n");
            (new Exception()).printStackTrace();
            udebug.vprintln("\n");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        if ((sessionid == null) && (triedConnection)) return null;
        Vector<String> lines = new Vector<String>();
        boolean gotSuccess = false;
        badStatus = false;
        try {
            URL url = new URL(baseURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(fullParam);
            out.close();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(), encoding);
            gotSuccess = readLines(in, lines);
            in.close();
            if (gotSuccess && (lines.size() > 0)) {
                String first = lines.get(0);
                if (first.indexOf(STR_ERROR) >= 0) gotSuccess = false;
            }
            if (!gotSuccess) {
                if (triedConnection || udebug.getDebug()) errorMessage(action, "status=" + statusText, fullParam);
                if (statusText.indexOf(STR_ERROR) < 0) {
                    if (udebug.getDebug()) {
                        udebug.dprintln("Bad error message: " + statusText);
                        udebug.dprintln("CGI output:");
                        final int length = Math.min(lines.size(), 50);
                        for (int i = 0; i < length; i++) udebug.dprintln(lines.get(i));
                        udebug.dprintln("\n");
                    }
                    badStatus = true;
                }
                return null;
            }
        } catch (IOException e) {
            statusText = "IO Error: " + e.getMessage();
            System.err.println(statusText);
            badStatus = true;
            return null;
        }
        return lines.toArray(new String[0]);
    }
