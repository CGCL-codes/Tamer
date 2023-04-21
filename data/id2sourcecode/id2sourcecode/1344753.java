    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountId = getServletContext().getInitParameter("accountId");
        String email = getServletContext().getInitParameter("email");
        String password = getServletContext().getInitParameter("password");
        String alertId = request.getParameter("alertId");
        String tel = request.getParameter("tel");
        String alertText = request.getParameter("alertText");
        if (alertText == null || alertText.length() < 1) {
            byte[] content = new byte[request.getContentLength()];
            request.getInputStream().read(content, 0, request.getContentLength());
            alertText = new String(content);
        }
        alertId = URLEncoder.encode(alertId, "UTF-8");
        Integer nResult;
        StringBuffer strResponse = new StringBuffer();
        String sData;
        try {
            sData = ("AccountId=" + URLEncoder.encode(accountId, "UTF-8"));
            sData += ("&Email=" + email);
            sData += ("&Password=" + URLEncoder.encode(password, "UTF-8"));
            sData += ("&Recipient=" + URLEncoder.encode(tel, "UTF-8"));
            sData += ("&Message=" + URLEncoder.encode(alertText, "UTF-8"));
            if (logger.isInfoEnabled()) {
                logger.info("Got an alert : \n alertID : " + alertId + " \n tel : " + tel + " \n text : " + alertText);
                logger.info("Red Oxygen : \n Account Id : " + accountId + " \n email : " + email + " \n password : " + password);
                logger.info("Red Oxygen URL : \n URL : " + RED_OXYGEN_URL + " \n data : " + sData);
            }
            URL urlObject = new URL(RED_OXYGEN_URL);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            con.setRequestMethod(HTTP_METHOD);
            con.setDoInput(true);
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(sData);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                responseBuffer = responseBuffer.append(inputLine);
                responseBuffer = responseBuffer.append("\n\n\n");
            }
            strResponse.replace(0, 0, responseBuffer.toString());
            String sResultCode = strResponse.substring(0, 4);
            nResult = new Integer(sResultCode);
            in.close();
        } catch (Exception e) {
            logger.error("Exception caught sending SMS", e);
            nResult = -2;
        }
        sendHttpResponse(response, RESPONSE_BEGIN + "result : " + nResult + "<br/>response : " + strResponse + RESPONSE_END);
    }
