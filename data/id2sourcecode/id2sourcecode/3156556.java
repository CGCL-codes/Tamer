    public boolean sendSMS(String content) {
        try {
            URL url = new URL("http://sms.ceeg.cn/zshsms.asmx/SendSms_empp?Uname=newoa&Upwd=ceegnewoalogon&Mobile=13291281612&Content=" + content);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            reader.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
