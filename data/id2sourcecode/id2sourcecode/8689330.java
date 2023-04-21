    public InputStream send(Sms sms) throws IOException {
        urlConnection = (HttpURLConnection) new URL(SEND_SMS_URL).openConnection();
        urlConnection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        StringBuffer query = new StringBuffer();
        if (sms.isTranslitUsed()) {
            query.append("transliterate=checked&");
        }
        query.append("charcheck=йцукен&");
        query.append("lang=rus&");
        query.append("codekey=" + this.captchaID + "&");
        query.append("codevalue=" + sms.getCode() + "&");
        query.append("prefix=" + "7" + sms.getPrefix() + "&");
        query.append("addr=" + sms.getNumber() + "&");
        query.append("message=");
        if (sms.getSender() != null && sms.getSender().trim().length() != 0) {
            query.append(FROM + sms.getSender() + ".");
        }
        query.append(sms.getMessage() + "\r\n");
        logger.debug("POST Query : " + query.toString());
        out.write(query.toString());
        out.flush();
        out.close();
        return urlConnection.getInputStream();
    }
