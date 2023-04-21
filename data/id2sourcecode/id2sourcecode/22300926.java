    private static String sendMessage(String SOAPUrl, byte[] byteMessage) {
        String resp = null;
        boolean requestSent = false;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(SOAPUrl);
            URLConnection connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;
            httpConn.setRequestProperty("Content-Length", String.valueOf(byteMessage.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            out.write(byteMessage);
            out.close();
            requestSent = true;
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                StringBuffer sb = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                resp = sb.toString();
            } else {
                resp = httpConn.getResponseMessage();
            }
        } catch (Exception e) {
            resp = (requestSent ? "Error response received" : "Error sending soap message") + " - " + e.getMessage();
            System.out.println(resp);
            e.printStackTrace();
        } finally {
            try {
                httpConn.disconnect();
            } catch (Exception e) {
            }
        }
        return resp;
    }
