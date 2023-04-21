    @Override
    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            try {
                URL url = new URL("http://192.168.1.139/num/uareveice?sid=123&cid=123&verifycode=12345678abcdef&pin=bW89JnVhPU9wZXJhJnA9YWM1ZmE0NzBjNDIxMDJkNGQ0NWM4N2NiOWI4NjZjNTA=");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                conn.getInputStream().close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
