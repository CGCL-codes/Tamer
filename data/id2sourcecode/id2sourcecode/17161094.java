    private InputStream getVoiceInputStream(String userid, String pwd, String txtPhone, String vtxt, String Svmode, String vfbtye, String svrno, String str_time, String end_time) throws Exception {
        URLConnection conn = null;
        InputStream is = null;
        try {
            byte[] buffer1 = null;
            if (Svmode == "3") {
                String vPath = vfbtye;
                if (vPath == "") {
                    String xx = "��ѡ�������ļ�����ʽΪ.WAV ��С��Ҫ���� 2M";
                    return null;
                } else {
                    int i = vPath.lastIndexOf(".");
                    String newext = vPath.substring(i + 1).toLowerCase();
                    if (!newext.equals("wav")) {
                        String xx = "�����ļ���ʽ����ȷ";
                        return null;
                    }
                }
            } else {
                buffer1 = new byte[0];
            }
            String soap = getVoiceSend(userid, pwd, txtPhone, vtxt, Svmode, buffer1, svrno, str_time, end_time);
            if (soap == null) {
                return null;
            }
            try {
                URL url = new URL("http://service2.winic.org/Service.asmx");
                conn = url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
                conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                conn.setRequestProperty("HOST", "service2.winic.org");
                conn.setRequestProperty("SOAPAction", "\"http://tempuri.org/SendVoice\"");
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
