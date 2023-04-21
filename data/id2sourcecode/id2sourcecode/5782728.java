    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = getServletContext().getInitParameter("key");
        String alertId = request.getParameter("alertId");
        String tel = request.getParameter("tel");
        String alertText = request.getParameter("alertText");
        if (alertText == null || alertText.length() < 1) {
            byte[] content = new byte[request.getContentLength()];
            request.getInputStream().read(content, 0, request.getContentLength());
            alertText = new String(content);
        }
        alertText = "Alert Id : " + alertId + ",  Alert Details :" + alertText;
        StringBuffer responseBuffer = new StringBuffer();
        String sData;
        try {
            sData = ("key=" + key);
            sData += ("&to=" + tel);
            sData += ("&message=" + alertText);
            URI uri = new URI("https", null, "sms.labs.ericsson.net", -1, "/send", sData, null);
            URL url = uri.toURL();
            if (logger.isInfoEnabled()) {
                logger.info("Got an alert : \n alertID : " + alertId + " \n tel : " + tel + " \n text : " + alertText);
                logger.info("Ericsson URL : \n URL : " + url);
            }
            InputStream in = url.openConnection().getInputStream();
            byte[] buffer = new byte[10000];
            int len = in.read(buffer);
            for (int q = 0; q < len; q++) responseBuffer.append((char) buffer[q]);
            in.close();
        } catch (Exception e) {
            logger.error("Exception caught sending SMS", e);
            responseBuffer.append(e.getMessage());
        }
        sendHttpResponse(response, RESPONSE_BEGIN + "response : Number of SMS Sent " + responseBuffer.toString() + RESPONSE_END);
    }
