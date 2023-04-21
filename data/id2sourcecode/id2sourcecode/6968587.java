    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo().startsWith("/chart")) {
            preventCaching(request, response);
            String chartName = request.getPathInfo().substring(6);
            String fileExt = request.getPathInfo().substring(request.getPathInfo().length() - 3, request.getPathInfo().length());
            OutputStream out = response.getOutputStream();
            try {
                if (fileExt.equals("png")) {
                    ChartBean chartBean = (ChartBean) getSessionAttribute(request.getSession(), chartName);
                    if (chartBean == null) throw new ServletException("Chart bean: " + chartName + " not found");
                    ChartWriterPNG.writeChart(chartBean, out);
                } else if (fileExt.equals("jpg")) {
                    ChartBean chartBean = (ChartBean) getSessionAttribute(request.getSession(), chartName);
                    if (chartBean == null) throw new ServletException("Chart bean: " + chartName + " not found");
                    ChartWriterJPG.writeChart(chartBean, out);
                } else if (fileExt.equals("svg")) {
                    ChartBean chartBean = (ChartBean) getSessionAttribute(request.getSession(), chartName);
                    if (chartBean == null) throw new ServletException("Chart bean: " + chartName + " not found");
                    try {
                        ChartWriterSVG.writeChart(chartBean, new OutputStreamWriter(out));
                    } catch (Throwable e) {
                        throw new ServletException("Chart bean: " + chartName + " : error displaying", e);
                    }
                }
            } finally {
                out.flush();
                out.close();
            }
            return;
        }
        long browserDate = request.getDateHeader("If-Modified-Since");
        if (browserDate > 0) {
            browserDate = (browserDate / 1000) * 1000;
            if (browserDate == ((lastModified / 1000) * 1000)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }
        ClassLoader cl = this.getClass().getClassLoader();
        String uri = request.getRequestURI();
        if (uri.endsWith(".jsf") || (uri.indexOf("popup/popupFrame/html/closePopup") != -1)) {
            response.setContentType("text/html;");
        } else {
            Calendar expires = Calendar.getInstance();
            expires.add(Calendar.HOUR_OF_DAY, 12);
            response.setDateHeader("Expires", expires.getTimeInMillis());
            response.setDateHeader("Last-Modified", lastModified);
            response.setHeader("Cache-Control", "max-age=43200");
            response.setHeader("Pragma", "");
        }
        if (uri.endsWith(".css")) {
            response.setContentType("text/css;");
        } else if (uri.endsWith(".js")) {
            response.setContentType("text/javascript;");
        } else if (uri.endsWith(".gif")) {
            response.setContentType("image/gif;");
        }
        String path = uri.substring(uri.indexOf(Util.JENIA_RESOURCE_PREFIX) + Util.JENIA_RESOURCE_PREFIX.length() + 1);
        if (path.indexOf("popup/popupFrame/html/closePopup") != -1) {
            StringBuffer sb = new StringBuffer(path);
            sb.delete(path.lastIndexOf(".") + 1, path.length());
            sb.append("jsf");
            path = sb.toString();
        }
        InputStream is = cl.getResourceAsStream(path);
        if (is == null) return;
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is);
        int read = 0;
        read = bis.read(buffer);
        while (read != -1) {
            out.write(buffer, 0, read);
            read = bis.read(buffer);
        }
        bis.close();
        out.flush();
        out.close();
    }
