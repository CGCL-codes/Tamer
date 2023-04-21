    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountId = getServletContext().getInitParameter("esendexAccountRef");
        String email = getServletContext().getInitParameter("esendexUserName");
        String password = getServletContext().getInitParameter("esendexPassword");
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
        try {
            String data = URLEncoder.encode("EsendexUsername", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("EsendexPassword", "UTF-8") + "=" + password;
            data += "&" + URLEncoder.encode("EsendexAccount", "UTF-8") + "=" + accountId;
            data += "&" + URLEncoder.encode("EsendexRecipient", "UTF-8") + "=" + tel;
            data += "&" + URLEncoder.encode("EsendexBody", "UTF-8") + "=" + URLEncoder.encode(alertText, "UTF-8");
            if (logger.isInfoEnabled()) {
                logger.info("Got an alert : \n alertID : " + alertId + " \n tel : " + tel + " \n text : " + alertText);
                logger.info("Esendex : \n Account Ref : " + accountId);
                logger.info("Esendex URL : \n URL : " + ESENDEX_URL + " \n data : " + data);
            }
            URL urlObject = new URL(ESENDEX_URL);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            con.setRequestMethod(HTTP_METHOD);
            con.setDoInput(true);
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(data);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuffer = responseBuffer.append(inputLine);
            }
        } catch (Exception e) {
            logger.error("Exception caught sending SMS", e);
        }
        sendHttpResponse(response, RESPONSE_BEGIN + "response : " + responseBuffer.toString() + RESPONSE_END);
    }
