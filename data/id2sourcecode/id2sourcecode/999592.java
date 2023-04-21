    public int SendSMS(java.lang.String smsNumber, java.lang.String smsMessage) {
        System.out.println(smsNumber);
        System.out.println(smsMessage);
        try {
            String sData;
            StringBuffer strResponse = new StringBuffer();
            int nResult = -1;
            try {
                sData = ("AccountId=" + URLEncoder.encode(smsAccountId, "UTF-8"));
                sData += ("&Email=" + URLEncoder.encode(smsEmail, "UTF-8"));
                sData += ("&Password=" + URLEncoder.encode(smsPassword, "UTF-8"));
                sData += ("&Recipient=" + URLEncoder.encode(smsNumber, "UTF-8"));
                sData += ("&Message=" + URLEncoder.encode(smsMessage, "UTF-8"));
                URL urlObject = new URL(urlRequest);
                HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                DataOutputStream out;
                out = new DataOutputStream(con.getOutputStream());
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
                System.out.println("Response Text = " + strResponse + "\n");
            } catch (Exception e) {
                System.out.println("Exception caught sending SMS\n");
                nResult = -1;
            }
            return nResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
