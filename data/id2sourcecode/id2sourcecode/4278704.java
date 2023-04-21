    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getInstance().getSession();
        User owner = baseService.getAuthenticatedUser(session, request, response);
        if (owner == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.flushBuffer();
            Logger.log("Unauthorized upload requested: " + request.getRemoteAddr());
            return;
        }
        List<FileItem> fileItems = Collections.emptyList();
        try {
            fileItems = getFileItems(request, owner);
        } catch (Throwable t) {
            Logger.log(t);
            return;
        }
        final FileUploadStatus status = BaseService.fileUploadStatusMap.get(owner);
        status.setStatus(FileUploadStatus.CREATING_FILE);
        Transaction tx = session.beginTransaction();
        try {
            for (final FileItem item : fileItems) {
                File fileObject = null;
                if (item.getContentType().contains("image")) {
                    fileObject = new Photo();
                } else {
                    fileObject = new File();
                }
                fileObject.setOwner(owner);
                String parentFolderId = request.getParameter("parentFolder");
                Folder parentFolder = null;
                if (parentFolderId != null && !"".equals(parentFolderId)) {
                    parentFolder = (Folder) session.load(Folder.class, new Long(parentFolderId));
                    if (!SecurityHelper.doesUserHavePermission(session, owner, parentFolder, PERM.WRITE)) {
                        Cookie cookie = new Cookie(item.getFieldName(), "");
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.flushBuffer();
                        Logger.log("Unauthorized upload requested: " + request.getRemoteAddr());
                        return;
                    }
                }
                fileObject.setParent(parentFolder);
                fileObject.setContentType(item.getContentType());
                String name = item.getName();
                if (name.lastIndexOf("/") >= 0) {
                    name = name.substring(name.lastIndexOf("/") + 1);
                }
                if (name.lastIndexOf("\\") >= 0) {
                    name = name.substring(name.lastIndexOf("\\") + 1);
                }
                fileObject.setName(name);
                fileObject.setDescription(name);
                session.save(fileObject);
                status.setStatus(FileUploadStatus.WRITING_FILE);
                java.io.File outputPath = new java.io.File(java.io.File.separatorChar + "tmp" + java.io.File.separatorChar + BaseSystem.getDomainName(request));
                java.io.File outputFile = new java.io.File(outputPath, fileObject.getId() + "_" + fileObject.getName());
                outputPath.mkdirs();
                item.write(outputFile);
                tx = session.beginTransaction();
                fileObject.setNameOnDisk(fileObject.getId() + "_" + fileObject.getName());
                fileObject.setSize(outputFile.length());
                session.save(fileObject);
                tx.commit();
                Logger.log("Wrote to file: " + outputFile.getCanonicalPath());
                status.setStatus(FileUploadStatus.WRITING_DATABASE);
                saveFileFromStream(fileObject, new FileInputStream(outputFile));
                try {
                    if (fileObject instanceof Photo) {
                        BufferedImage bi = javax.imageio.ImageIO.read(outputFile);
                        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
                        ImageIO.write(bi, "png", pngOut);
                        tx = session.beginTransaction();
                        Photo photo = (Photo) fileObject;
                        photo.setWidth(bi.getWidth());
                        photo.setHeight(bi.getHeight());
                        PhotoThumbnail thumbFile = new PhotoThumbnail();
                        thumbFile.setHidden(true);
                        thumbFile.setOwner(owner);
                        thumbFile.setParent(parentFolder);
                        thumbFile.setName(createFileName("", name, "_thumb"));
                        thumbFile.setDescription("Thumbnail for " + name);
                        session.save(thumbFile);
                        PhotoThumbnail previewFile = new PhotoThumbnail();
                        previewFile.setHidden(true);
                        previewFile.setOwner(owner);
                        previewFile.setParent(parentFolder);
                        previewFile.setName(createFileName("", name, "_preview"));
                        previewFile.setDescription("Preview image for " + name);
                        session.save(previewFile);
                        PhotoThumbnail slideFile = new PhotoThumbnail();
                        slideFile.setHidden(true);
                        slideFile.setOwner(owner);
                        slideFile.setParent(parentFolder);
                        slideFile.setName(createFileName("", name, "_slideshow"));
                        slideFile.setDescription("Slideshow image for " + name);
                        session.save(slideFile);
                        photo.setThumbnailImage(thumbFile);
                        photo.setSlideshowImage(slideFile);
                        photo.setPreviewImage(previewFile);
                        ByteArrayInputStream pngIn = new ByteArrayInputStream(pngOut.toByteArray());
                        ByteArrayOutputStream thumbOutStream = new ByteArrayOutputStream();
                        BufferedImage thumbbi = ImageIO.read(pngIn);
                        BufferedImage convertedThumbImage = createThumbnail(thumbbi, 128, 128, true);
                        thumbFile.setWidth(convertedThumbImage.getWidth());
                        thumbFile.setHeight(convertedThumbImage.getHeight());
                        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                        ImageWriteParam iwp = writer.getDefaultWriteParam();
                        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        iwp.setCompressionQuality(0.75f);
                        writer.setOutput(new MemoryCacheImageOutputStream(thumbOutStream));
                        IIOImage thumbimage = new IIOImage(convertedThumbImage, null, null);
                        writer.write(null, thumbimage, iwp);
                        pngIn.reset();
                        ByteArrayOutputStream slideOutStream = new ByteArrayOutputStream();
                        BufferedImage slidebi = ImageIO.read(pngIn);
                        BufferedImage convertedSlideImage = createThumbnail(slidebi, 640, 400, true);
                        slideFile.setWidth(convertedSlideImage.getWidth());
                        slideFile.setHeight(convertedSlideImage.getHeight());
                        writer.setOutput(new MemoryCacheImageOutputStream(slideOutStream));
                        IIOImage slideimage = new IIOImage(convertedSlideImage, null, null);
                        writer.write(null, slideimage, iwp);
                        pngIn.reset();
                        ByteArrayOutputStream previewOutStream = new ByteArrayOutputStream();
                        BufferedImage previewbi = ImageIO.read(pngIn);
                        BufferedImage convertedPreviewImage = createThumbnail(previewbi, 320, 200, true);
                        previewFile.setWidth(convertedPreviewImage.getWidth());
                        previewFile.setHeight(convertedPreviewImage.getHeight());
                        writer.setOutput(new MemoryCacheImageOutputStream(previewOutStream));
                        IIOImage previewimage = new IIOImage(convertedPreviewImage, null, null);
                        writer.write(null, previewimage, iwp);
                        thumbFile.setContentType("image/jpeg");
                        slideFile.setContentType("image/jpeg");
                        previewFile.setContentType("image/jpeg");
                        thumbFile.setSize(thumbOutStream.size());
                        slideFile.setSize(slideOutStream.size());
                        previewFile.setSize(previewOutStream.size());
                        session.save(thumbFile);
                        session.save(slideFile);
                        session.save(previewFile);
                        tx.commit();
                        tx = session.beginTransaction();
                        thumbFile.setNameOnDisk(createFileName(thumbFile.getId().toString() + "_", name, "_thumb"));
                        slideFile.setNameOnDisk(createFileName(slideFile.getId().toString() + "_", name, "_slide"));
                        previewFile.setNameOnDisk(createFileName(previewFile.getId().toString() + "_", name, "_preview"));
                        tx.commit();
                        saveFileFromStream(thumbFile, new ByteArrayInputStream(thumbOutStream.toByteArray()));
                        saveFileFromStream(slideFile, new ByteArrayInputStream(slideOutStream.toByteArray()));
                        saveFileFromStream(previewFile, new ByteArrayInputStream(previewOutStream.toByteArray()));
                    }
                    status.setStatus(FileUploadStatus.FINISHED);
                    Logger.log("Wrote to database: " + fileObject.getName());
                } catch (Throwable t) {
                    Logger.log(t);
                }
                Cookie cookie = new Cookie(item.getFieldName(), fileObject.getId().toString());
                cookie.setPath("/");
                cookie.setMaxAge(BaseService.COOKIE_TIMEOUT);
                response.addCookie(cookie);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getOutputStream().close();
                response.flushBuffer();
            }
        } catch (Throwable t) {
            Logger.log(t);
            try {
                tx.rollback();
            } catch (Exception e2) {
            }
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }
