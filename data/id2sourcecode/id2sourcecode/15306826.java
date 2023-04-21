    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        long fileId = ParamUtils.getLongParameter(request, "fileId", 0);
        String service = request.getParameter("service");
        if (StringUtils.isEmpty(service)) {
            service = "file.FileService";
        }
        try {
            if (!StringUtils.isEmpty(path) || fileId > 0) {
                String fileName = "download";
                String ext = "";
                String mimeType = null;
                if (fileId > 0) {
                    IFileService fileService = (IFileService) BeanFactory.getBean(service);
                    IFile file = fileService.loadFile(fileId);
                    path = file.getPhycialPath();
                    if (file.getFileType() != null) {
                        ext = file.getFileType().getExt();
                        mimeType = file.getFileType().getMimeType();
                    }
                    fileName = file.getName();
                } else {
                    path = new String(Base64.decode(path));
                }
                File file = new File(path);
                if (!file.exists()) {
                    path = ApplicationConfig.getInstance().getUploadDirectory() + path;
                    file = new File(path);
                    if (!file.exists()) {
                        path = this.rootPath + path;
                    }
                }
                log.debug(path);
                if (StringUtils.isEmpty(ext)) {
                    ext = path.substring(path.lastIndexOf(".") + 1);
                }
                if (isImageFile(ext)) {
                    response.setContentType("image/" + ext);
                } else {
                    if (StringUtils.isEmpty(mimeType)) {
                        mimeType = "application/octet-stream";
                    }
                    response.setContentType(mimeType);
                }
                if (isImageFile(ext)) {
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName + "." + ext);
                } else {
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName + "." + ext);
                }
                InputStream is = new FileInputStream(file);
                OutputStream os = response.getOutputStream();
                int readCount = 0;
                byte[] buff = new byte[1024];
                while ((readCount = is.read(buff)) != -1) {
                    os.write(buff, 0, readCount);
                }
                is.close();
                os.flush();
            } else {
                String targetClassName = request.getParameter("TargetClass");
                DownObject downObj = (DownObject) (Class.forName(targetClassName).newInstance());
                downObj.setRequest(request);
                String fileName = downObj.getName();
                fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
                response.setContentType(downObj.getContentType());
                if ((DownObject.DOWN_SYS_SAVE).equals(downObj.getDownMode())) {
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                } else {
                    response.addHeader("Content-Disposition", "filename=" + fileName);
                }
                OutputStream os = response.getOutputStream();
                downObj.createOutStream(os, request);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error(this, e);
        }
    }
