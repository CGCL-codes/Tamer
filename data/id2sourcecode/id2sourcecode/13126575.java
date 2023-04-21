    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
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
        keysString = req.getParameter("resultKeys");
        if (req.getParameter("includeTIF") != null && !req.getParameter("includeTIF").equals("")) {
            includeTIF = req.getParameter("includeTIF");
        }
        if (req.getParameter("includeJPG") != null && !req.getParameter("includeJPG").equals("")) {
            includeJPG = req.getParameter("includeJPG");
        }
        if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
            archiveHelper.setMode(req.getParameter("mode"));
        }
        SecurityAdvisor secAdvisor = null;
        try {
            secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
            if (secAdvisor != null) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename=gnomexExperimentData.zip");
                response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                baseDirFlowCell = dh.getFlowCellDirectory(req.getServerName());
                baseDir = dh.getMicroarrayDirectoryForReading(req.getServerName());
                archiveHelper.setTempDir(dh.getPropertyDictionary(PropertyDictionary.TEMP_DIRECTORY));
                Map fileDescriptorMap = new HashMap();
                long compressedFileSizeTotal = getFileNamesToDownload(baseDir, baseDirFlowCell, keysString, fileDescriptorMap, includeTIF.equals("Y"), includeJPG.equals("Y"), dh.getPropertyDictionary(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG));
                ZipOutputStream zipOut = null;
                TarArchiveOutputStream tarOut = null;
                if (archiveHelper.isZipMode()) {
                    zipOut = new ZipOutputStream(response.getOutputStream());
                } else {
                    tarOut = new TarArchiveOutputStream(response.getOutputStream());
                }
                int totalArchiveSize = 0;
                for (Iterator i = fileDescriptorMap.keySet().iterator(); i.hasNext(); ) {
                    String requestNumber = (String) i.next();
                    Request request = findRequest(sess, requestNumber);
                    if (request == null) {
                        log.error("Unable to find request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    if (!secAdvisor.canRead(request)) {
                        log.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    List fileDescriptors = (List) fileDescriptorMap.get(requestNumber);
                    for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext(); ) {
                        FileDescriptor fd = (FileDescriptor) i1.next();
                        TransferLog xferLog = new TransferLog();
                        xferLog.setFileName(fd.getZipEntryName());
                        xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
                        xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
                        xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
                        xferLog.setPerformCompression("Y");
                        xferLog.setIdRequest(request.getIdRequest());
                        xferLog.setIdLab(request.getIdLab());
                        InputStream in = archiveHelper.getInputStreamToArchive(fd.getFileName(), fd.getZipEntryName());
                        if (archiveHelper.isZipMode()) {
                            zipOut.putNextEntry(new ZipEntry(archiveHelper.getArchiveEntryName()));
                        } else {
                            TarArchiveEntry entry = new TarArchiveEntry(archiveHelper.getArchiveEntryName());
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
                response.setContentLength(totalArchiveSize);
                if (archiveHelper.isZipMode()) {
                    zipOut.finish();
                    zipOut.flush();
                } else {
                    tarOut.finish();
                }
                sess.flush();
            } else {
                response.setStatus(999);
                System.out.println("DownloadResultsServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            System.out.println("DownloadResultsServlet: An exception occurred " + e.toString());
            e.printStackTrace();
        } finally {
            if (secAdvisor != null) {
                try {
                    secAdvisor.closeHibernateSession();
                } catch (Exception e) {
                }
            }
            archiveHelper.removeTemporaryFile();
        }
    }
