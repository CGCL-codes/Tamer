    public void doPostImpl(HttpServletRequest req, HttpServletResponse response) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            response.setStatus(405);
            return;
        }
        String s = req.getParameter("numFiles");
        String pk = req.getParameter("progressKey");
        UploadMonitor up = new UploadMonitor(req.getSession(true), Integer.parseInt(s), pk);
        String contextPath = req.getContextPath();
        String prefixPath = MAPPING_NAME;
        String uri = req.getRequestURI();
        String relFile = "";
        if (contextPath.length() + prefixPath.length() + 2 < uri.length()) {
            relFile = uri.substring(contextPath.length() + prefixPath.length() + 2);
            relFile = URLTools.decodeUrlToPath(relFile);
        }
        String absFile = new File(Config.getCurrentConfig().getAbsoluteRootFolderName(), relFile).getAbsolutePath();
        Folder fol = (Folder) Folder.getAlbum(null, absFile, null);
        ServletFileUpload upload = new ServletFileUpload();
        long readTotal = 0;
        long readItem = 0;
        int itemNum = 0;
        try {
            ActivityTracker.openServlet();
            FileItemIterator iter = upload.getItemIterator(req);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                } else {
                    if (!Utils.isStringEmpty(item.getName())) {
                        itemNum++;
                        ZipInputStream zis = null;
                        if (Utils.isZipFile(new File(item.getName()))) {
                            zis = new ZipInputStream(stream);
                        }
                        String outFileName = Utils.getFileNameFromUploadName(item.getName());
                        InputStream readFrom = stream;
                        File outf = Utils.getNewFile(new File(fol.getDir(), outFileName));
                        try {
                            while (true) {
                                ZipEntry ze = null;
                                long itemsize = -1;
                                if (zis != null) {
                                    ze = zis.getNextEntry();
                                    if (ze == null) break;
                                    outFileName = ze.getName();
                                    outf = new File(fol.getDir(), outFileName);
                                    if (ze.isDirectory()) {
                                        outf.mkdirs();
                                        Folder f = (Folder) Folder.getAlbum(null, outf.getAbsolutePath(), null);
                                        f.contentChanged();
                                        f.sync();
                                        continue;
                                    }
                                    outf = Utils.getNewFile(outf);
                                    itemsize = ze.getSize();
                                    readFrom = zis;
                                }
                                OutputStream os = new FileOutputStream(outf);
                                try {
                                    byte[] buffer = new byte[1024];
                                    int read;
                                    while (itemsize != 0 && (read = readFrom.read(buffer, 0, (int) ((itemsize < 0) ? buffer.length : Math.min(buffer.length, itemsize)))) >= 0) {
                                        if (read == 0) Thread.sleep(100); else {
                                            os.write(buffer, 0, read);
                                            readTotal += read;
                                            readItem += read;
                                            if (itemsize >= 0) itemsize -= read;
                                            up.update(readItem, readTotal, itemNum, item.getName(), (ze == null) ? null : ze.getName());
                                        }
                                    }
                                } catch (Exception e) {
                                    LogUtil.logWarn(logger, "", e);
                                } finally {
                                    os.close();
                                }
                                if (zis == null) break;
                            }
                        } catch (Exception e) {
                            LogUtil.logWarn(logger, "", e);
                        } finally {
                            if (zis != null) zis.close();
                        }
                        readItem = 0;
                        fol.contentChanged();
                        fol.sync();
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.logError(logger, "", e);
        } finally {
            ActivityTracker.closeServlet();
        }
        up.close();
        response.setStatus(200);
        response.setContentType("text/html");
        Writer w = response.getWriter();
        w.write("<html><head><title></title></head><body>Upload complete</body></html>");
    }
