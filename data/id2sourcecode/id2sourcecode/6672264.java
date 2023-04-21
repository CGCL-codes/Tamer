    public static String SendSms(String mobile, String content, String x_eid, String x_uid, String x_pwd_md5, Integer x_gate_id) throws UnsupportedEncodingException {
        Integer x_ac = 10;
        HttpURLConnection httpconn = null;
        String result = "-20";
        String memo = content.length() < 70 ? content.trim() : content.trim().substring(0, 70);
        StringBuilder sb = new StringBuilder();
        sb.append("http://gateway.woxp.cn:6630/utf8/web_api/?x_eid=");
        sb.append(x_eid);
        sb.append("&x_uid=").append(x_uid);
        sb.append("&x_pwd_md5=").append(x_pwd_md5);
        sb.append("&x_ac=").append(x_ac);
        sb.append("&x_gate_id=").append(x_gate_id);
        sb.append("&x_target_no=").append(mobile);
        sb.append("&x_memo=").append(URLEncoder.encode(memo, "utf-8"));
        try {
            URL url = new URL(sb.toString());
            httpconn = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            result = rd.readLine();
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpconn != null) {
                httpconn.disconnect();
                httpconn = null;
            }
        }
        return result;
    }
