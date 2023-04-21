        @Override
        protected Response doInBackground() {
            try {
                sleep(_reconnectTimeout);
            } catch (InterruptedException ex) {
            }
            _testForSSL();
            HttpURLConnection conn = null;
            Response response = new Response(null);
            try {
                Socket sock;
                if (_serverSelected.getUseProxy()) {
                    sock = new Socket(_serverSelected.getProxyHost(), _serverSelected.getProxyPort());
                } else {
                    sock = new Socket(_serverSelected.getHost(), _serverSelected.getPort());
                }
                InetAddress ia = sock.getLocalAddress();
                String data = URLEncoder.encode(IntChatConstants.AuthFields.LOGIN, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(_serverSelected.getLogin(), IntChatConstants.ENCODING) + "&" + URLEncoder.encode(IntChatConstants.AuthFields.PASSWORD, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(_serverSelected.getPassword(), IntChatConstants.ENCODING) + "&" + URLEncoder.encode(IntChatConstants.AuthFields.IC_CLIENTVERSION, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(IntChatMainFrame.MAJOR_VERSION + "." + IntChatMainFrame.MINOR_VERSION + "." + IntChatMainFrame.BUILD_NUMBER, IntChatConstants.ENCODING) + "&" + URLEncoder.encode(IntChatConstants.AuthFields.IC_HOSTADDRESS, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(ia.getHostAddress(), IntChatConstants.ENCODING) + "&" + URLEncoder.encode(IntChatConstants.AuthFields.IC_HOSTNAME, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(ia.getHostName(), IntChatConstants.ENCODING) + "&" + URLEncoder.encode(IntChatConstants.AuthFields.IC_DNSNAME, IntChatConstants.ENCODING) + "=" + URLEncoder.encode(ia.getCanonicalHostName(), IntChatConstants.ENCODING);
                sock.close();
                URL url = new URL(_serverSelected.getURIString() + "/Login");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + IntChatConstants.ENCODING);
                conn.setRequestProperty("Accept-Encoding", LocalSettings.getAcceptEncodingData());
                conn.setInstanceFollowRedirects(false);
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), IntChatConstants.ENCODING);
                writer.write(data);
                writer.flush();
                int responseCode = conn.getResponseCode();
                response.setResponseCode(responseCode);
                response.setResponseMessage(conn.getResponseMessage());
                if (responseCode > 0 && responseCode < 400) dropContent(conn.getInputStream()); else if (responseCode >= 400) dropContent(conn.getErrorStream()); else throw new Exception("Wrong HTTP response code");
                if (responseCode >= 300 && responseCode <= 399) {
                    String path = conn.getHeaderField("location");
                    int semicolon = path.lastIndexOf(';');
                    int question = path.indexOf('?');
                    if (semicolon > -1) _sessionId = path.substring(semicolon, question > -1 ? question : path.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) try {
                    conn.getInputStream().close();
                } catch (IOException ioe) {
                }
            }
            return response;
        }
