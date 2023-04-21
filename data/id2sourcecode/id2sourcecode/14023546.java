    protected DataOutputStream establishConnection() throws IOException {
        URL url = new URL(UPLOAD_URL);
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        lastConn = connection;
        StringBuffer dfsb = new SimpleDateFormat("M/dd/yyyy").format(new java.util.Date(), new StringBuffer(), new FieldPosition(0));
        String dateStr = dfsb.toString();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        String args = "username=" + URLEncoder.encode(username) + "&" + "passwd=" + URLEncoder.encode(password) + "&" + "readDisclaimer=agree&" + "cvt_to_ns=true&" + "trace_device=" + URLEncoder.encode(device) + "&" + "trace_descr=" + URLEncoder.encode(description) + "&" + "mailBack=on&" + "simple_output=true&" + "trace_date=" + URLEncoder.encode(dateStr) + "&" + "trace_data=";
        out.writeBytes(args);
        return out;
    }
