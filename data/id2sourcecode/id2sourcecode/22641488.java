    private void postXml() {
        try {
            URL url = new URL(mainPanel.hostPanel.txtHost.getText());
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(URLEncoder.encode(mainPanel.xmlRequest.getText(), "UTF-8"));
            writer.flush();
            writer.close();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            StringBuilder buf = new StringBuilder();
            char[] cbuf = new char[2048];
            int num;
            while (-1 != (num = reader.read(cbuf))) {
                buf.append(cbuf, 0, num);
            }
            String result = buf.toString();
            mainPanel.xmlResponse.setText(result);
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }
