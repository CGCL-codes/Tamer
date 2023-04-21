    public String callSOAPRequest(String xml) {
        OutputStream os;
        InputStream is;
        errorString = "";
        long length;
        try {
            URL l_url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) l_url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("CallingType", "SJ");
            if (auth != null) conn.setRequestProperty("Authorization", auth);
            conn.setRequestProperty("SOAPAction", "http://sap.com/xi/WebService/soap1.1");
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            OutputStreamWriter osr = new OutputStreamWriter(os);
            osr.write(xml);
            osr.flush();
            osr.close();
            is = conn.getInputStream();
            length = conn.getContentLength();
            String str = "";
            if (length != -1) {
                byte incomingData[] = new byte[(int) length];
                is.read(incomingData);
                str = new String(incomingData);
            }
            is.close();
            return str;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
    }
