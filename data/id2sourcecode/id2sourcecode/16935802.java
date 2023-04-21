    @Override
    public void run() {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPut put = new HttpPut(this._surl);
            InputStreamWrapper isw = new InputStreamWrapper(this, this._input_stream);
            InputStreamEntity ire = new InputStreamEntity(isw, -1);
            ire.setContentType("text/binary");
            put.setEntity(ire);
            HttpResponse response = client.execute(put);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) this._reply = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            this._exception = e;
        }
    }
