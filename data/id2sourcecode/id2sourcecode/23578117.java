    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        serverName = req.getServerName();
        if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
            if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress()) || req.getRemoteAddr().equals("127.0.0.1") || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
                log.debug("Requested from local host");
            } else {
                log.error("Accessing secure command over non-secure line from remote host is not allowed");
                response.setContentType("text/html");
                response.getOutputStream().println("<html><head><title>Error</title></head>");
                response.getOutputStream().println("<body><b>");
                response.getOutputStream().println("Secure connection is required. Prefix your request with 'https: " + "<br>");
                response.getOutputStream().println("</body>");
                response.getOutputStream().println("</html>");
                return;
            }
        }
        parser = (AnalysisFileDescriptorParser) req.getSession().getAttribute(CacheAnalysisFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
        if (parser == null) {
            log.error("Unable to get file descriptor parser from session");
            return;
        }
        if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
            archiveHelper.setMode(req.getParameter("mode"));
        }
        SecurityAdvisor secAdvisor = null;
        try {
            secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
            if (secAdvisor != null) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename=gnomexAnalysis.zip");
                response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                archiveHelper.setTempDir(dh.getPropertyDictionary(PropertyDictionary.TEMP_DIRECTORY));
                parser.parse();
                ZipOutputStream zipOut = null;
                TarArchiveOutputStream tarOut = null;
                if (archiveHelper.isZipMode()) {
                    zipOut = new ZipOutputStream(response.getOutputStream());
                } else {
                    tarOut = new TarArchiveOutputStream(response.getOutputStream());
                }
                int totalArchiveSize = 0;
                for (Iterator i = parser.getAnalysisNumbers().iterator(); i.hasNext(); ) {
                    String analysisNumber = (String) i.next();
                    Analysis analysis = null;
                    List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
                    if (analysisList.size() == 1) {
                        analysis = (Analysis) analysisList.get(0);
                    }
                    if (analysis == null) {
                        log.error("Unable to find analysis " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    if (!secAdvisor.canRead(analysis)) {
                        log.error("Insufficient permissions to read analysis " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    List fileDescriptors = parser.getFileDescriptors(analysisNumber);
                    for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext(); ) {
                        AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i1.next();
                        if (fd.getType().equals("dir")) {
                            continue;
                        }
                        TransferLog xferLog = new TransferLog();
                        xferLog.setFileName(fd.getZipEntryName());
                        xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
                        xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
                        xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
                        xferLog.setPerformCompression("Y");
                        xferLog.setIdAnalysis(analysis.getIdAnalysis());
                        xferLog.setIdLab(analysis.getIdLab());
                        if (!analysisNumber.equalsIgnoreCase(fd.getNumber())) {
                            log.error("Analysis number does not match directory for attempted download on " + fd.getFileName() + " for user " + req.getUserPrincipal().getName() + ".  Bypassing download.");
                            continue;
                        }
                        InputStream in = archiveHelper.getInputStreamToArchive(fd.getFileName(), fd.getZipEntryName());
                        if (archiveHelper.isZipMode()) {
                            zipOut.putNextEntry(new ZipEntry("bioinformatics-analysis-" + archiveHelper.getArchiveEntryName()));
                        } else {
                            TarArchiveEntry entry = new TarArchiveEntry("bioinformatics-analysis-" + archiveHelper.getArchiveEntryName());
                            entry.setSize(archiveHelper.getArchiveFileSize());
                            tarOut.putArchiveEntry(entry);
                        }
                        OutputStream out = null;
                        if (archiveHelper.isZipMode()) {
                            out = zipOut;
                        } else {
                            out = tarOut;
                        }
                        int size = archiveHelper.transferBytes(in, out);
                        totalArchiveSize += size;
                        xferLog.setFileSize(new BigDecimal(size));
                        xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
                        sess.save(xferLog);
                        if (archiveHelper.isZipMode()) {
                            zipOut.closeEntry();
                        } else {
                            tarOut.closeArchiveEntry();
                        }
                        archiveHelper.removeTemporaryFile();
                    }
                }
                sess.flush();
                if (archiveHelper.isZipMode()) {
                    zipOut.finish();
                    zipOut.flush();
                } else {
                    tarOut.close();
                    tarOut.flush();
                }
            } else {
                response.setStatus(999);
                System.out.println("DownloadAnalyisFileServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            System.out.println("DownloadAnalyisFileServlet: An exception occurred " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (secAdvisor != null) {
                    secAdvisor.closeHibernateSession();
                }
            } catch (Exception e) {
            }
            req.getSession().setAttribute(CacheAnalysisFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
            archiveHelper.removeTemporaryFile();
        }
    }
