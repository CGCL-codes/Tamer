    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiUrl = request.getParameter(API_URL);
        if (apiUrl == null) {
            response.setStatus(400);
            return;
        }
        HttpURLConnection connection = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod(request.getMethod());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Accept", request.getHeader("Accept"));
            connection.setRequestProperty("Accept-Encoding", request.getHeader("Accept-Encoding"));
            connection.setRequestProperty("Accept-Charset", request.getHeader("Accept-Charset"));
            input = request.getInputStream();
            output = connection.getOutputStream();
            int read = 0;
            byte[] data = new byte[8192];
            while ((read = input.read(data)) != -1) {
                output.write(data, 0, read);
            }
            output.close();
            input.close();
            connection.connect();
            response.setContentType(connection.getHeaderField("Content-Type"));
            response.setHeader("Content-Encoding", connection.getHeaderField("Content-Encoding"));
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setStatus(connection.getResponseCode());
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                response.setHeader("Location", connection.getHeaderField("Location"));
            } else {
                input = connection.getErrorStream();
            }
            if (request.getParameter("mode") != null && request.getParameter("mode").equalsIgnoreCase("compat")) {
                input = connection.getInputStream();
            }
            if (input != null) {
                output = response.getOutputStream();
                data = new byte[8192];
                while ((read = input.read(data)) != -1) {
                    output.write(data, 0, read);
                }
                output.close();
                input.close();
            }
            connection.disconnect();
        } catch (IOException e) {
            try {
                if (input != null) input.close();
            } catch (Exception e1) {
                log.error("Error closing stream!", e1);
            }
            try {
                if (output != null) output.close();
            } catch (Exception e1) {
                log.error("Error closing stream!", e1);
            }
            if (connection != null) connection.disconnect();
            log.error("Could not execute call!", e);
            response.setStatus(500);
        }
    }
