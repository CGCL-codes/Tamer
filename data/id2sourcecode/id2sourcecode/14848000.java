    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            try {
                MultipartRequest parser = new ServletMultipartRequest((HttpServletRequest) request, MultipartRequest.MAX_READ_BYTES);
                log.debug(parser.getHtmlTable());
                String uploadFolder = parser.getURLParameter("uploadFolder");
                log.debug("doFilter():uploadFolder->" + uploadFolder);
                if (uploadFolder == null || uploadFolder.trim().equals("")) {
                    uploadFolder = getFilterConfig().getInitParameter("defaultUploadFolder");
                    if (uploadFolder == null || uploadFolder.trim().equals("")) {
                        uploadFolder = "/";
                    }
                    File uploadFolderDirectory = new File(uploadFolder);
                    if (!uploadFolderDirectory.exists()) {
                        uploadFolderDirectory.mkdirs();
                        log.info("doFilter:Template directory [" + uploadFolderDirectory + "], as specified in web.xml," + " was successfully created.");
                    }
                }
                Enumeration paramNames = parser.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = (String) paramNames.nextElement();
                    String[] values;
                    Collection col = new ArrayList();
                    Enumeration paramValues = parser.getURLParameters(paramName);
                    while (paramValues.hasMoreElements()) {
                        col.add(paramValues.nextElement());
                    }
                    values = (String[]) col.toArray(new String[0]);
                    if (values.length == 1) request.setAttribute(paramName, values[0]); else request.setAttribute(paramName, values);
                }
                for (Enumeration e = parser.getFileParameterNames(); e.hasMoreElements(); ) {
                    String name = (String) e.nextElement();
                    log.debug("paramName = " + name);
                    File file = new File(uploadFolder, parser.getFileSystemName(name));
                    InputStream in = parser.getFileContents(name);
                    if (in != null) {
                        BufferedInputStream input = new BufferedInputStream(in);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        int c;
                        while ((c = input.read()) != -1) fileOutputStream.write(c);
                        fileOutputStream.close();
                        input.close();
                    }
                    String canonicalPath = null;
                    if (file != null) canonicalPath = file.getCanonicalPath();
                    request.setAttribute(name, canonicalPath);
                }
            } catch (Exception e) {
                log.printStackTrace(e, LogManager.ERROR);
            }
        }
        chain.doFilter(request, response);
    }
