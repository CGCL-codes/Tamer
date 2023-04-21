    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gedcomFilePath = request.getRequestURI();
        String servletPath = request.getServletPath();
        gedcomFilePath = gedcomFilePath.substring(gedcomFilePath.indexOf(servletPath) + servletPath.length());
        LOG.finest("The GEDCCOM file path is: " + gedcomFilePath);
        if (gedcomFilePath.indexOf("/../") != -1 || gedcomFilePath.indexOf("\\..\\") != -1 || gedcomFilePath.endsWith("/..") || gedcomFilePath.endsWith("\\..")) {
            throw new ServletException("The parent directory path notation '/../' is not allowed in the path :-).");
        }
        if (ServletFileUpload.isMultipartContent(request)) {
            if ("/upload".equals(gedcomFilePath)) {
                SSOSubject authenticated = (SSOSubject) request.getAttribute(SSOSubject.SSOSUBJECT_KEY);
                if (authenticated == null) {
                    throw new ServletException("The request does not have an " + SSOSubject.SSOSUBJECT_KEY + " attribute, this is not allowed!!!");
                }
                LOG.finest("Uploading GEDCOM file...");
                try {
                    String filenames = parseRequest(request, authenticated);
                    String tabIndex = request.getParameter("tabIndex");
                    LOG.finest("The tabIndex from the request is: " + tabIndex);
                    if (tabIndex == null) {
                        tabIndex = "5";
                    }
                    response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/successfulUpload.html?tabIndex=" + tabIndex + "&gcsPath=uploads" + URLEncoder.encode(File.separator + authenticated.getScreenName(), "UTF-8") + "&successfulUpload=" + URLEncoder.encode(filenames, "UTF-8")));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                LOG.finest("The upload action path is not /upload -> '" + gedcomFilePath + "'!!!");
                throw new ServletException("An attempt to upload a GEDCOM file to a path other than '/upload' is being made, security does not allow this!!!");
            }
        } else {
            File gedcomFile = new File(GLinkURL.centralSiteRoot + gedcomFilePath);
            if (gedcomFile.isDirectory() && gedcomFile.exists()) {
                LOG.finest("Displaying GEDCOM Central Site directory layout for: " + gedcomFile);
                FilenameFilter filter = new FilenameFilter() {

                    public boolean accept(File dir, String filename) {
                        return (new File(dir, filename).isDirectory() || isValidFilename(filename));
                    }
                };
                List<String> filenames = Arrays.asList(gedcomFile.list(filter));
                GCSContentView content = new GCSContentView();
                String requestURL = request.getRequestURL().toString();
                if (!requestURL.endsWith("/")) {
                    requestURL += "/";
                }
                long lastModified = 0;
                for (String filename : filenames) {
                    File contentFile = new File(gedcomFile, filename);
                    if (filename.length() == 2 && contentFile.isDirectory()) {
                        File[] contentFiles = contentFile.listFiles();
                        for (File subContentFile : contentFiles) {
                            if (subContentFile.getName().length() == 2 && subContentFile.isDirectory()) {
                                File[] subFiles = subContentFile.listFiles();
                                for (File subFile : subFiles) {
                                    if (isValidFilename(subFile.getName())) {
                                        boolean validHash = GLinkURL.justFileHash(subFile.getName()).equals(contentFile.getName() + File.separator + subContentFile.getName());
                                        if (subFile.isFile() && validHash) {
                                            LOG.finest("1) Adding file: " + subFile.getAbsolutePath());
                                            GCSContent gcsContent = new GCSContent();
                                            if (lastModified < subFile.lastModified()) {
                                                lastModified = subFile.lastModified();
                                            }
                                            gcsContent.setIsFile(subFile.isFile());
                                            gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + subFile.getName());
                                            gcsContent.setLinkURL(requestURL + subFile.getName());
                                            gcsContent.setLinkContent(subFile.getName());
                                            content.addContent(gcsContent);
                                        } else {
                                            if ((subContentFile.isDirectory() || isValidFilename(subContentFile.getName())) && validHash) {
                                                LOG.finest("2) Adding file: " + subContentFile.getAbsolutePath());
                                                GCSContent gcsContent = new GCSContent();
                                                if (lastModified < subContentFile.lastModified()) {
                                                    lastModified = subContentFile.lastModified();
                                                }
                                                gcsContent.setIsFile(subContentFile.isFile());
                                                gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + subContentFile.getName());
                                                gcsContent.setLinkURL(requestURL + subContentFile.getName());
                                                gcsContent.setLinkContent(subContentFile.getName());
                                                content.addContent(gcsContent);
                                            }
                                        }
                                    }
                                }
                            } else {
                                LOG.finest("The full path to the file: " + subContentFile.getAbsolutePath());
                                LOG.finest("File hash: " + GLinkURL.justFileHash(subContentFile.getName()));
                                LOG.finest("Two parent dirs: " + contentFile.getParentFile().getName() + File.separator + contentFile.getName());
                                if ((contentFile.isDirectory() || isValidFilename(contentFile.getName())) && !GLinkURL.justFileHash(subContentFile.getName()).equals(contentFile.getParentFile().getName() + File.separator + contentFile.getName()) && isValidFilename(subContentFile.getName())) {
                                    LOG.finest("3) Adding file: " + contentFile.getAbsolutePath());
                                    GCSContent gcsContent = new GCSContent();
                                    if (lastModified < contentFile.lastModified()) {
                                        lastModified = contentFile.lastModified();
                                    }
                                    gcsContent.setIsFile(contentFile.isFile());
                                    gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + contentFile.getName());
                                    gcsContent.setLinkURL(requestURL + contentFile.getName());
                                    gcsContent.setLinkContent(contentFile.getName());
                                    content.addContent(gcsContent);
                                }
                            }
                        }
                    } else if (!contentFile.isFile() || !GLinkURL.justFileHash(contentFile.getName()).equals(contentFile.getParentFile().getParentFile().getName() + File.separator + contentFile.getParentFile().getName())) {
                        LOG.finest("4) Adding file: " + contentFile.getAbsolutePath());
                        GCSContent gcsContent = new GCSContent();
                        if (lastModified < contentFile.lastModified()) {
                            lastModified = contentFile.lastModified();
                        }
                        gcsContent.setIsFile(contentFile.isFile());
                        gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + filename);
                        gcsContent.setLinkURL(requestURL + filename);
                        gcsContent.setLinkContent(filename);
                        content.addContent(gcsContent);
                    }
                }
                sendJSONResponse(response, content, lastModified);
            } else {
                if (gedcomFile.exists() && gedcomFile.isFile()) {
                    LOG.finest("FOUND file: " + gedcomFile);
                    if (request.getParameter("plaintext") != null) {
                        LOG.finest("Sending GEDCOM file as plain text: " + gedcomFile);
                        response.setContentType("text/plain");
                    } else {
                        LOG.finest("Forcing download of GEDCOM file: " + gedcomFile);
                        response.setContentType("application/force-download");
                    }
                    OutputStream out = response.getOutputStream();
                    InputStream in = new BufferedInputStream(new FileInputStream(gedcomFile));
                    Streams.copy(in, out, false);
                    out.flush();
                    out.close();
                    in.close();
                } else {
                    LOG.finest("Could not find: " + gedcomFile);
                    throw new ServletException("The requested GEDCOM file path '" + gedcomFilePath + "' is not valid because the GEDCOM file '" + gedcomFile + "' cannot be resolved!!!");
                }
            }
        }
    }
