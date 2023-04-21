    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.error("reasterizeServlet started");
        String url = request.getParameter("url");
        if (url == null) throw new RuntimeException("url parameter is not defined (null)"); else if (url.indexOf("svg") == -1) throw new RuntimeException("url parameter not pointing to svg file");
        logger.error("reasterizeServlet init");
        Rasterizer myRasterizer = new Rasterizer();
        myRasterizer.setSource(url);
        myRasterizer.setResult(request, response);
        String file = myRasterizer.getFileOut();
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
            while (ChartRegistry.SINGLETON.getChart(file).length == 0 && System.currentTimeMillis() - t0 < 10 * 1000) {
                Thread.sleep(100);
            }
            if (ChartRegistry.SINGLETON.getChart(file).length == 0) {
                throw new RuntimeException("Image did not get generated");
            }
            logger.info("Image " + file + " produced in [" + (System.currentTimeMillis() - t0) + "]");
            input = new ByteArrayInputStream(ChartRegistry.SINGLETON.getChart(file));
            int contentLength = input.available();
            response.reset();
            response.setContentLength(contentLength);
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
