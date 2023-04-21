    public void getIcon(final String email) {
        new Thread() {

            @Override
            public void run() {
                String[] items = email.split("@");
                String[] domains = items[1].split("\\.");
                String url = String.format(ICON_SERVER, domains[0], items[0]);
                try {
                    HttpClient hc = new DefaultHttpClient();
                    HttpHead head = new HttpHead(url);
                    HttpResponse response = hc.execute(head);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                        return;
                    }
                    HttpGet get = new HttpGet(url);
                    response = hc.execute(get);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int read = 0;
                    InputStream content = response.getEntity().getContent();
                    while (read > -1) {
                        read = content.read(bytes, 0, 1024);
                        if (read > 0) {
                            bos.write(bytes, 0, read);
                        }
                    }
                    ;
                    service.getServiceResponse().respond(MrimServiceResponse.RES_SAVEIMAGEFILE, bos.toByteArray(), email, new String(email.hashCode() + ""));
                } catch (Exception e) {
                    service.log(url + "\n");
                    service.log(e);
                }
            }
        }.start();
    }
