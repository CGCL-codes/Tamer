    private InputStream getSoapInputStream(String userid, String pass, String mobiles, String msg, String time) throws Exception {
        URLConnection conn = null;
        InputStream is = null;
        try {
            String soap = getSoapSmssend(userid, pass, mobiles, msg, time);
            if (soap == null) {
                return null;
            }
            try {
                URL url = new URL("http://service2.winic.org:8003/Service.asmx");
                conn = url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
                conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                conn.setRequestProperty("HOST", "service2.winic.org");
                conn.setRequestProperty("SOAPAction", "\"http://tempuri.org/SendMessages\"");
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
                osw.write(soap);
                osw.flush();
            } catch (Exception ex) {
                System.out.print("SmsSoap.openUrl error:" + ex.getMessage());
            }
            try {
                is = conn.getInputStream();
            } catch (Exception ex1) {
                System.out.print("SmsSoap.getUrl error:" + ex1.getMessage());
            }
            return is;
        } catch (Exception e) {
            System.out.print("SmsSoap.InputStream error:" + e.getMessage());
            return null;
        }
    }
