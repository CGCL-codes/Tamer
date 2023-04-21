    protected void service(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        try {
            String servletPath = aRequest.getServletPath() + aRequest.getPathInfo();
            String extension = StringUtils.right(servletPath, servletPath.length() - servletPath.lastIndexOf(".") - 1);
            InputStream inputStream;
            URL resource = SimpleResourceManager.class.getResource(servletPath);
            if (resource != null) {
                URLConnection urlConnection = resource.openConnection();
                int length = urlConnection.getContentLength();
                aResponse.setContentType(getMimeType(extension));
                aResponse.setContentLength(length);
                inputStream = urlConnection.getInputStream();
            } else inputStream = new ByteArrayInputStream((byte[]) GuiaFramework.getApplicationContext().get(aRequest.getContextPath() + servletPath));
            if (inputStream == null) aResponse.sendError(404); else {
                bis = new BufferedInputStream(inputStream);
                bos = new BufferedOutputStream(outputStream = aResponse.getOutputStream());
                GuiaHelper.copyStreams(bis, bos, 4096);
            }
        } catch (Exception e) {
            LogFactory.getLog(SimpleResourceManager.class).warn("Cannot load resource", e);
            aResponse.sendError(404);
        } finally {
            if (bis != null) bis.close();
            if (bos != null) bos.close();
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
