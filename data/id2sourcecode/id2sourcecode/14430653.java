    public synchronized void run() {
        OutputStreamWriter out;
        InputStream inputStream;
        BufferedReader reader;
        Request request = null;
        String str, tmp, sessionValue;
        int i;
        if (!thread.isAlive()) {
            runState = 1;
            thread.start();
            return;
        }
        if (runState > 1) return;
        runState++;
        sessionValue = SEAUser.getUser().getSessionValue();
        if (digestSessionValue) sessionValue = ((tmp = computeHash(sessionValue)) == null) ? "none" : tmp;
        while (true) {
            while ((request = (Request) requests.poll()) != null) {
                try {
                    http = (HttpURLConnection) new URL(url).openConnection();
                    http.setDoOutput(true);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("Content-Type", "text/xml; charset=ISO-8859-1");
                    if (useCookies) http.setRequestProperty("Cookie", SEAUser.getUser().getSessionName() + "=" + sessionValue);
                    http.setConnectTimeout(HTTP_TIMEOUT);
                    switch(request.getType()) {
                        case Request.READ_DATA:
                            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            http.connect();
                            out = new OutputStreamWriter(http.getOutputStream());
                            if (!useCookies) out.write("session_value=" + sessionValue);
                            for (i = 0; i < request.getNumParameters(); i += 2) out.write('&' + request.getParameter(i) + '=' + request.getParameter(i + 1));
                            out.flush();
                            out.close();
                            inputStream = http.getInputStream();
                            SEAUser.getUser().parseData(inputStream);
                            inputStream.close();
                            break;
                        case Request.WRITE_DATA:
                            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            http.connect();
                            out = new OutputStreamWriter(http.getOutputStream());
                            if (!useCookies) out.write("session_value=" + sessionValue);
                            for (i = 0; i < request.getNumParameters(); i += 2) out.write('&' + request.getParameter(i) + '=' + request.getParameter(i + 1));
                            out.flush();
                            out.close();
                            break;
                        case Request.INVOKE_SERVICE:
                            http.connect();
                            out = new OutputStreamWriter(http.getOutputStream());
                            out.write("<?xml version='1.0' encoding='ISO-8859-1'?><SOAP-ENV:Envelope SOAP-ENV:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/'>");
                            if ((str = request.getName()) == null) {
                                request.setOutput("No specified web service name");
                                break;
                            }
                            out.write("<SOAP-ENV:Body><" + str + ">");
                            if (!useCookies) out.write("<session_value xsi:type='xsd:string'>" + sessionValue + "</session_value>");
                            for (i = 0; i < request.getNumParameters(); i += 2) out.write("<" + (tmp = request.getParameter(i)) + " xsi:type='xsd:string'>" + request.getParameter(i + 1) + "</" + tmp + ">");
                            out.write("</" + str + "></SOAP-ENV:Body></SOAP-ENV:Envelope>");
                            out.flush();
                            out.close();
                            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                            str = "";
                            while ((tmp = reader.readLine()) != null) str += tmp;
                            reader.close();
                            request.setOutput(getReturnString(str));
                            break;
                    }
                    request.setProcessed(true);
                } catch (Exception e) {
                    request.setOutput(e.getMessage());
                } finally {
                    request.finalize();
                    http.disconnect();
                }
            }
            synchronized (lock) {
                if (!serverUptodate) {
                    serverUptodate = true;
                    String[] params = new String[dirtyData.size() << 1];
                    Iterator itr = dirtyData.iterator();
                    for (i = 0; itr.hasNext(); i += 2) {
                        SEAUser.Preference pref = (SEAUser.Preference) itr.next();
                        params[i] = pref.getPath();
                        params[i + 1] = pref.getStringValue();
                    }
                    if (params.length > 1) addRequest(new Request(Request.WRITE_DATA, params));
                }
            }
            try {
                thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
