    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String file = request.getPathInfo().substring(1);
        if (file == null) throw new RuntimeException("id parameter is not defined (null)");
        String contentType = URLConnection.guessContentTypeFromName(file);
        if (contentType == null || !contentType.startsWith("image")) {
            return;
        }
        InputStream input = null;
        OutputStream output = null;
        try {
            long ms = System.currentTimeMillis();
            while (ChartRegistry.SINGLETON.getChart(file) == null) {
                Thread.sleep(10);
            }
            logger.info("Image " + file + " registered in [" + (System.currentTimeMillis() - ms) + "]");
            long t0 = System.currentTimeMillis();
            while (ChartRegistry.SINGLETON.getChart(file).length == 0 && System.currentTimeMillis() - t0 < 60 * 1000) {
                Thread.sleep(100);
            }
            if (ChartRegistry.SINGLETON.getChart(file).length == 0) {
                throw new RuntimeException("Image did not get generated");
            }
            logger.info("Image " + file + " produced in " + (System.currentTimeMillis() - t0) + " ms");
            input = new ByteArrayInputStream(ChartRegistry.SINGLETON.getChart(file));
            int contentLength = input.available();
            response.reset();
            response.setContentLength(contentLength);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "inline; filename=\"" + file + "\"");
            output = new BufferedOutputStream(response.getOutputStream());
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            AutoRefreshMap.getAutoRefreshMap("chartRegistry").printLog();
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
