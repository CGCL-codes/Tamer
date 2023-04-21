    public Response executeGET(Request rq) throws Exception {
        Response result = new Response();
        HttpURLConnection connection = null;
        BufferedReader inputBufferReader = null;
        String fullAuthHeader = rq.getHeader("Authorization");
        String user = rq.getUsername();
        String password = rq.getPassword();
        String httpURL = rq.getUrl();
        try {
            Logger.getLogger(this.getClass()).debug("HTTP REQUEST DATA:\nURL: " + httpURL + "\nAUTH: " + fullAuthHeader + "\nUSER: " + user + "\nPASSWD: " + password);
            URL url = new URL(httpURL);
            connection = ((HttpURLConnection) url.openConnection());
            if (fullAuthHeader != null && !fullAuthHeader.equalsIgnoreCase("")) {
            } else {
                if (((user != null) && (password != null)) && ((!user.equals("")) && (!password.equals("")))) {
                    String userpasswd = URLEncoder.encode(user, charset) + ":" + URLEncoder.encode(password, charset);
                    String userpasswd64 = new sun.misc.BASE64Encoder().encode(userpasswd.getBytes());
                    connection.setRequestProperty("Authorization", "Basic " + userpasswd64);
                }
            }
            for (Iterator it = rq.getHeaders().keySet().iterator(); it.hasNext(); ) {
                String k = (String) it.next();
                connection.setRequestProperty(k, rq.getHeader(k));
                Logger.getLogger(this.getClass()).debug("Setting header #" + k + "# -> #" + rq.getHeader(k) + "#");
            }
            connection.setRequestMethod("GET");
            connection.setRequestProperty("connection", "close");
            connection.setRequestProperty("connection-token", "close");
            connection.setRequestProperty("HTTP-Version", httpProtoVersion);
            connection.connect();
            Logger.getLogger(this.getClass()).debug("\nCONNECTED\nREADING...");
            result.setHeaders(connection.getHeaderFields());
            InputStream resInStream = null;
            if (connection.getResponseCode() == 200) {
                resInStream = connection.getInputStream();
            } else {
                Logger.getLogger(this.getClass()).debug("ERROR CODE RECEIVED: " + connection.getResponseCode());
                resInStream = connection.getErrorStream();
            }
            inputBufferReader = new BufferedReader(new InputStreamReader(resInStream, charset), inputBufferSize);
            String line = null;
            StringBuffer lineBuffer = new StringBuffer();
            while ((line = inputBufferReader.readLine()) != null) {
                lineBuffer.append(line);
                lineBuffer.append("\n");
            }
            inputBufferReader.close();
            Logger.getLogger(this.getClass()).debug("READ COMPLETED, DISCONNECTING...");
            connection.disconnect();
            Logger.getLogger(this.getClass()).debug("DISCONNECTED");
            result.setBody(lineBuffer.toString());
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("\nHTTP REQUEST FAILED. \nERROR MESSAGE: " + e.getMessage() + "\nURL: " + httpURL);
            throw new Exception("HttpServiceProvider.executeGET: " + e.getMessage());
        } finally {
            try {
                inputBufferReader.close();
            } catch (Exception e) {
            }
            try {
                connection.disconnect();
            } catch (Exception e) {
            }
        }
        return result;
    }
