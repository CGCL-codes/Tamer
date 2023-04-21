    private void downloadFile(HttpServletRequest req, HttpServletResponse resp, String username, String password) {
        try {
            Utils.log(this.getClass(), "Download: Preparing download...");
            File cachePath = new File(Constants.CACHE.PATH);
            if (!cachePath.exists()) {
                if (!cachePath.mkdir()) {
                    throw new ErrorCodeException(Constants.ERROR.MKDIRFAIL);
                }
            }
            String files = req.getParameter(Constants.PARAM_FILES);
            if (null == files) {
                throw new ErrorCodeException(Constants.ERROR.BADPARAMS);
            }
            Utils.log(this.getClass(), "Calling files: " + files);
            String cacheFileName = Utils.makeUniqueRandomName();
            OutputStream out = Support.getFileOutputStream(cachePath, cacheFileName);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new CheckedOutputStream(out, new CRC32())));
            zos.setEncoding(Constants.ENCODING.LOCAL_FS);
            String[] filenames = files.split(Constants.SPLITTER.FILE);
            for (String filename : filenames) {
                try {
                    filename = Utils.makeArchiveAbsName(Utils.normalFileName(filename));
                    Utils.log(this.getClass(), "Add a file to archive file - filename=" + filename);
                    File file = new File(filename);
                    String archiveName = new String(file.getName().getBytes("iso-8859-1"), "utf-8");
                    zos.putNextEntry(new ZipEntry(archiveName));
                    filename = new String((file.getParent() == null ? "" : file.getParent()).getBytes("iso-8859-1"), "utf-8") + "/" + file.getName();
                    fs.retrieveFile(username, password, filename, zos);
                } catch (Exception e) {
                    Utils.log(this.getClass(), "A file compress exception found - filename=" + filename);
                    e.printStackTrace();
                } finally {
                    zos.closeEntry();
                }
            }
            zos.close();
            String packName = "package_" + System.currentTimeMillis() + ".zip";
            resp.addHeader("Content-Disposition", "attachment;filename=" + packName);
            resp.setContentType("application/octet-stream;name=" + packName);
            resp.setCharacterEncoding("utf-8");
            InputStream in = Support.getFileInputStream(cachePath, cacheFileName);
            OutputStream os = resp.getOutputStream();
            try {
                Util.copyStream(in, os);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
                os.close();
            }
            File cf = new File(cachePath, cacheFileName);
            if (cf.exists() && !cf.delete()) {
                cf.deleteOnExit();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            error(req, resp, Constants.ERROR.ACCESSDENIED);
        } catch (ErrorCodeException e) {
            e.printStackTrace();
            error(req, resp, e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            error(req, resp, Constants.ERROR.IOERROR);
        }
    }
