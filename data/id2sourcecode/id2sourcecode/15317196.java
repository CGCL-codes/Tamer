    @SuppressWarnings("unused")
    protected ModelAndView handleRequestAfterValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean zipEncodeFilenames = SPropsUtil.getBoolean("encode.zip.download.filenames", false);
        String zipFilenameEncoding = SPropsUtil.getString("encode.zip.download.filename.encoding", "cp437");
        String pathInfo = request.getPathInfo();
        String[] args = pathInfo.split(Constants.SLASH);
        if (args.length == WebUrlUtil.FILE_URL_ZIP_ARG_LENGTH && String.valueOf(args[WebUrlUtil.FILE_URL_FILE_ID]).equals("zip")) {
            try {
                Boolean singleByte = SPropsUtil.getBoolean("export.filename.8bitsinglebyte.only", true);
                DefinableEntity entity = getEntity(args[WebUrlUtil.FILE_URL_ENTITY_TYPE], Long.valueOf(args[WebUrlUtil.FILE_URL_ENTITY_ID]));
                Set<Attachment> attachments = entity.getAttachments();
                String fileName = getBinderModule().filename8BitSingleByteOnly(entity.getTitle() + ".zip", "files.zip", singleByte);
                response.setContentType(mimeTypes.getContentType(fileName));
                response.setHeader("Cache-Control", "private, max-age=0");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                InputStream fileStream = null;
                ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                zipOut.setEncoding(zipFilenameEncoding);
                Integer fileCounter = 1;
                for (Attachment attachment : attachments) {
                    if (attachment instanceof FileAttachment) {
                        String attExt = EntityIndexUtils.getFileExtension(((FileAttachment) attachment).getFileItem().getName());
                        String attName = ((FileAttachment) attachment).getFileItem().getName();
                        if (!zipEncodeFilenames) {
                            attName = getBinderModule().filename8BitSingleByteOnly(attName, "__file" + fileCounter.toString(), singleByte);
                        }
                        fileCounter++;
                        try {
                            if (entity.getEntityType().equals(EntityType.folderEntry)) {
                                fileStream = getFileModule().readFile(entity.getParentBinder(), entity, (FileAttachment) attachment);
                            } else if (entity.getEntityType().equals(EntityType.folder) || entity.getEntityType().equals(EntityType.workspace)) {
                                fileStream = getFileModule().readFile((Binder) entity, entity, (FileAttachment) attachment);
                            } else {
                                zipOut.finish();
                                return null;
                            }
                            zipOut.putNextEntry(new ZipEntry(attName));
                            FileUtil.copy(fileStream, zipOut);
                            zipOut.closeEntry();
                            fileStream.close();
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                }
                zipOut.finish();
                return null;
            } catch (Exception e) {
                response.getOutputStream().print(NLT.get("file.error.unknownFile"));
            }
            return null;
        } else if (args.length == WebUrlUtil.FILE_URL_ZIP_SINGLE_ARG_LENGTH && String.valueOf(args[WebUrlUtil.FILE_URL_FILE_ID]).equals("zip")) {
            String faId = args[WebUrlUtil.FILE_URL_ZIP_SINGLE_FILE_ID];
            try {
                Boolean singleByte = SPropsUtil.getBoolean("export.filename.8bitsinglebyte.only", true);
                DefinableEntity entity = getEntity(args[WebUrlUtil.FILE_URL_ENTITY_TYPE], Long.valueOf(args[WebUrlUtil.FILE_URL_ENTITY_ID]));
                Set<Attachment> attachments = entity.getAttachments();
                FileAttachment fileAtt = null;
                for (Attachment attachment : attachments) {
                    if (attachment instanceof FileAttachment) {
                        if (attachment.getId().equals(faId)) {
                            fileAtt = (FileAttachment) attachment;
                            break;
                        }
                        fileAtt = ((FileAttachment) attachment).findFileVersionById(faId);
                        if (fileAtt != null) break;
                    }
                }
                if (fileAtt != null) {
                    String fileName = EntityIndexUtils.getFileNameWithoutExtension(fileAtt.getFileItem().getName());
                    fileName = getBinderModule().filename8BitSingleByteOnly(fileName + ".zip", "download.zip", singleByte);
                    response.setContentType(mimeTypes.getContentType(fileName));
                    response.setHeader("Cache-Control", "private, max-age=0");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                    InputStream fileStream = null;
                    ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                    zipOut.setEncoding(zipFilenameEncoding);
                    String attExt = EntityIndexUtils.getFileExtension(fileAtt.getFileItem().getName());
                    String attName = fileAtt.getFileItem().getName();
                    if (!zipEncodeFilenames) {
                        attName = getBinderModule().filename8BitSingleByteOnly(attName, "file", singleByte);
                    }
                    try {
                        if (entity.getEntityType().equals(EntityType.folderEntry)) {
                            fileStream = getFileModule().readFile(entity.getParentBinder(), entity, fileAtt);
                        } else if (entity.getEntityType().equals(EntityType.folder) || entity.getEntityType().equals(EntityType.workspace)) {
                            fileStream = getFileModule().readFile((Binder) entity, entity, fileAtt);
                        } else {
                            zipOut.finish();
                            return null;
                        }
                        zipOut.putNextEntry(new ZipEntry(attName));
                        String loggerText = "Copying an unencrypted file of length ";
                        if (fileAtt.isEncrypted()) {
                            loggerText = "Copying an encrypted file of length ";
                        }
                        long startTime = System.nanoTime();
                        FileUtil.copy(fileStream, zipOut);
                        zipOut.closeEntry();
                        fileStream.close();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                    zipOut.finish();
                }
                return null;
            } catch (Exception e) {
                response.getOutputStream().print(NLT.get("file.error.unknownFile"));
            }
            return null;
        } else if (args.length < WebUrlUtil.FILE_URL_ARG_LENGTH) {
            return null;
        } else {
            try {
                DefinableEntity entity = getEntity(args[WebUrlUtil.FILE_URL_ENTITY_TYPE], Long.valueOf(args[WebUrlUtil.FILE_URL_ENTITY_ID]));
                FileAttachment fa = null;
                if (args.length > WebUrlUtil.FILE_URL_ARG_LENGTH && entity instanceof FolderEntry) {
                    fa = getAttachment((FolderEntry) entity, Arrays.asList(args).subList(WebUrlUtil.FILE_URL_NAME, args.length).toArray());
                    if (fa != null) {
                        entity = fa.getOwner().getEntity();
                    }
                } else {
                    fa = getAttachment(entity, args[WebUrlUtil.FILE_URL_NAME], args[WebUrlUtil.FILE_URL_VERSION], args[WebUrlUtil.FILE_URL_FILE_ID]);
                }
                if (fa != null) {
                    String shortFileName = FileUtil.getShortFileName(fa.getFileItem().getName());
                    String contentType = getFileTypeMap().getContentType(shortFileName);
                    response.setContentType(contentType);
                    boolean isHttps = request.getScheme().equalsIgnoreCase("https");
                    String cacheControl = "private, max-age=86400";
                    if (isHttps) {
                        response.setHeader("Pragma", "public");
                        cacheControl += ", proxy-revalidate, s-maxage=0";
                    }
                    response.setHeader("Cache-Control", cacheControl);
                    String attachment = "";
                    if (FileHelper.checkIfAttachment(contentType)) attachment = "attachment; ";
                    response.setHeader("Content-Disposition", attachment + "filename=\"" + FileHelper.encodeFileName(request, shortFileName) + "\"");
                    response.setHeader("Last-Modified", formatDate(fa.getModification().getDate()));
                    try {
                        Binder parent = getBinder(entity);
                        if (!fa.isEncrypted()) {
                            response.setHeader("Content-Length", String.valueOf(FileHelper.getLength(parent, entity, fa)));
                        }
                        getFileModule().readFile(parent, entity, fa, response.getOutputStream());
                        if (args[WebUrlUtil.FILE_URL_VERSION].equals(WebKeys.READ_FILE_LAST_VIEW)) {
                            getReportModule().addFileInfo(AuditType.download, fa);
                        }
                    } catch (Exception e) {
                        response.getOutputStream().print(NLT.get("file.error") + ": " + e.getLocalizedMessage());
                    }
                } else {
                    response.getOutputStream().print(NLT.get("file.error.unknownFile"));
                }
                try {
                    response.getOutputStream().flush();
                } catch (Exception ignore) {
                }
            } catch (Exception e) {
                response.getOutputStream().print(NLT.get("file.error.unknownFile"));
            }
        }
        return null;
    }
