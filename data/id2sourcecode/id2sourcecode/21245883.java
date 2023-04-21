    private boolean accept() {
        Debug.log("[Servent] Accept connection from " + ip + ":" + port);
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            writerThread.setOutputStream(out);
            readerThread.setInputStream(in);
        } catch (IOException ie) {
            Debug.log(ie.toString());
            return false;
        }
        StringTokenizer t = new StringTokenizer(firstLine, "/");
        if (t.countTokens() < 2) {
            sendInvalidLogin(out, "Wrong number of args");
            return false;
        }
        t.nextToken();
        String version = t.nextToken();
        if (compareVersion(version, "0.4") == -2) return false; else if (compareVersion(version, "0.4") == 0) {
            try {
                out.write(NETWORK_NAME.getBytes());
                out.write(LOGIN_OK.getBytes());
                out.write(10);
                out.write(10);
                out.flush();
            } catch (IOException ie) {
                return false;
            }
            return true;
        } else if (compareVersion(version, "0.4") > 0) {
            StringBuffer loginOk = new StringBuffer();
            loginOk.append(NETWORK_NAME);
            loginOk.append('/');
            loginOk.append(VERSION);
            loginOk.append(" 200 OK");
            try {
                out.write(loginOk.toString().getBytes());
                out.write(13);
                out.write(10);
                sendServerHeaders(out);
                out.flush();
            } catch (IOException ie) {
                return false;
            }
            ReadLineReader rlr = new ReadLineReader(in);
            String response = rlr.readLine();
            if (response != null && response.indexOf("200") != -1) sendInvalidLogin(out, "Wrong response" + response); else {
                sendInvalidLogin(out, "Wrong response");
                return false;
            }
        }
        return true;
    }
