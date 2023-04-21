    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String[]> parMap = request.getParameterMap();
        String method = parMap.get("m")[0];
        String server = parMap.get("s")[0];
        String id;
        if (parMap.containsKey("q")) {
            id = parMap.get("q")[0];
        } else {
            id = "";
        }
        Integer timeout;
        if (parMap.containsKey("t")) {
            timeout = Integer.valueOf(parMap.get("t")[0]);
        } else {
            timeout = 5;
        }
        String reqUrl = generateUrl(server, method, id);
        URL url = new URL(reqUrl);
        try {
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {
                    InputStream iStream = connect.getInputStream();
                    OutputStream oStream = response.getOutputStream();
                    byte[] data = new byte[4096];
                    int readLen = -1;
                    while ((readLen = iStream.read(data)) > 0) {
                        oStream.write(data, 0, readLen);
                    }
                    iStream.close();
                    oStream.close();
                } catch (IOException e) {
                    throw new ServletException("Error reading from DAS server: " + e.getMessage());
                }
            } else {
                response.sendError(connect.getResponseCode());
            }
        } catch (ConnectException c) {
            response.sendError(404);
        }
    }
