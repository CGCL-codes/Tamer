    private DigiDState toDigid(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        DigiDState state = null;
        StringBuffer redirectURL = new StringBuffer(this.redirectURL).append(request.getRequestURI());
        String applChosen = redirectURL.toString();
        int c;
        String input;
        URL url;
        String encURL = URLEncoder.encode(applChosen, "UTF-8");
        String soapMessage = Soap.buildDigiDAuthenticate(digidURL, digidSharedSecret, digidApplID, digidServerID, applChosen, encURL);
        if (logger.isDebugEnabled()) {
            logger.debug("Send [" + soapMessage + "]");
        }
        PrintWriter pw = null;
        BufferedReader in = null;
        try {
            url = new URL(digidURL);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Close");
            connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            pw = new PrintWriter(os);
            pw.println(soapMessage);
            pw.flush();
            Soap.displayConnectionHeaders(connection);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (input = ""; ; ) {
                c = in.read();
                if (c < 0) break;
                input = input + (char) c;
            }
            if (logger.isDebugEnabled()) logger.debug("Received [" + input + "]");
        } catch (IOException e) {
            logger.warn("I/O exception: " + e.getMessage(), e);
            throw e;
        } finally {
            if (pw != null) pw.close();
            if (in != null) in.close();
        }
        String m_result = Soap.extractFromXml(input, "m:result_code");
        if (m_result == null) {
            state = new DigiDFailed();
        } else if (!m_result.equals("0000")) {
            state = new DigiDFailed(m_result);
        } else {
            state = new InitialState();
            String m_url = Soap.extractFromXml(input, "m:as_url");
            String m_rid = Soap.extractFromXml(input, "m:rid");
            String m_server = Soap.extractFromXml(input, "m:a-select-server");
            String msg = "";
            if (m_url == null) msg += " m:as_url";
            if (m_rid == null) msg += " m:rid";
            if (m_server == null) msg += " m:a-select-server";
            if (!msg.equals("")) {
                logger.error("Missing DigiD fields in SOAP 'authenticate'" + " message: " + msg.substring(1));
                state = new DigiDFailed();
                return state;
            }
            ((InitialState) state).setRid(m_rid);
            if (logger.isDebugEnabled()) {
                logger.debug("m_rid:" + m_rid);
                logger.debug("m_server:" + m_server);
                logger.debug("applChosen:" + applChosen);
            }
            session.setAttribute(DigiDState.ATTRIBUTE_NAME, state);
            String urlDigiD = m_url + "&rid=" + m_rid + "&a-select-server=" + m_server;
            logger.debug("Redirect to: " + urlDigiD);
            ((HttpServletResponse) response).sendRedirect(urlDigiD);
        }
        return state;
    }
