    private void doProxy(ServentRequest request, ServentResponse response) throws IOException {
        String url = request.getParameter("url");
        String service = request.getParameter("service");
        if ((url != null) && (service != null)) {
            HttpURLConnection conn = null;
            try {
                URL codebaseUrl = new URL(url + "/" + service + "/CODEBASE");
                conn = (HttpURLConnection) codebaseUrl.openConnection();
                InputStream is = conn.getInputStream();
                response.setContentLength(conn.getContentLength());
                for (int i = 0; i < conn.getContentLength(); i++) {
                    response.getOutputStream().write((byte) is.read());
                }
                response.getOutputStream().close();
            } catch (MalformedURLException e) {
                response.sendError(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
        }
    }
