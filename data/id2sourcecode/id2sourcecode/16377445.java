    protected void btnSendActionPerformed(ActionEvent e) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        BasicHttpParams httpParams = new BasicHttpParams();
        for (int i = 0; i < table.getRowCount(); i++) {
            String field = (tableModel.getValueAt(i, 0) + "").trim();
            String value = (tableModel.getValueAt(i, 1) + "").trim();
            tableModel.setValueAt(field, i, 0);
            tableModel.setValueAt(value, i, 1);
            if (!field.equalsIgnoreCase("")) {
                formparams.add(new BasicNameValuePair(field, value));
                httpParams.setParameter(field, value);
            }
        }
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        txtUrl.setText(txtUrl.getText().trim());
        String uri = txtUrl.getText() + (txtUrl.getText().contains("?") ? "&" : "?") + URLEncodedUtils.format(formparams, "UTF-8");
        HttpUriRequest uriRequest[] = { new HttpGet(uri), new HttpPost(uri), new HttpPut(uri), new HttpDelete(uri), new HttpHead(uri), new HttpOptions(uri) };
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = uriRequest[cmbMethod.getSelectedIndex()];
        System.out.println(uri);
        if (request instanceof HttpPost) {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);
            request = httpPost;
        }
        if (request instanceof HttpPut) {
            HttpPut httpPut = new HttpPut();
            httpPut.setEntity(entity);
            request = httpPut;
        }
        String[] headers = txtHeaders.getText().split(";");
        for (String header : headers) {
            String[] h = header.split(":", 2);
            if (h.length == 2) {
                request.addHeader(h[0].trim(), h[1].trim());
            }
        }
        try {
            HttpResponse response = client.execute(request);
            byte[] data = EntityUtils.toByteArray(response.getEntity());
            txtrResponse.setText(new String(data));
            txtrStatus.setText("");
            for (Header header : response.getAllHeaders()) {
                String status = txtrStatus.getText() + header.getName() + ": " + header.getValue() + "\n";
                txtrStatus.setText(status);
            }
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
