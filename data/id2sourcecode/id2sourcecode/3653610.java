    public static void main(String[] args) throws Exception {
        URL url = new URL("http://swingdance.ru/admin/objects/block_edit.html?object_id=137&block_id=399");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        String authStr = Base64Utils.toBase64("admin:aa4sdWeb".getBytes());
        System.out.println("Auth: " + authStr);
        connection.setRequestProperty("Authorization", "Basic " + authStr);
        final OutputStream os = connection.getOutputStream();
        addParameter(os, "block_template_id", "0");
        addParameter(os, "name", "content-ru");
        addParameter(os, "description", "");
        addParameter(os, "data_process_id", "0");
        addParameter(os, "data_type_id", "5");
        addParameter(os, "is_published", "1");
        addParameter(os, "btnUpdate", "true");
        addParameter(os, "data", "<schedule>������ ��� ����� ������!</schedule>");
        os.close();
        final InputStream is = connection.getInputStream();
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = is.read()) >= 0) {
            sb.append((char) ch);
        }
        System.out.println("response:\n" + sb.toString());
        is.close();
        connection.disconnect();
    }
