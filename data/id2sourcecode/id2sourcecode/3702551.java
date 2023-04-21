    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("doPost");
        String fileName = null;
        String contentType;
        byte[] document = null;
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            if (!items.isEmpty()) {
                fileName = items.get(0).getName();
                document = items.get(0).get();
            }
        } catch (FileUploadException e) {
            throw new ServletException(e);
        }
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        contentType = supportedFileExtensions.get(extension);
        if (null == contentType) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            IOUtils.write(document, zipOutputStream);
            zipOutputStream.close();
            fileName = FilenameUtils.getBaseName(fileName) + ".zip";
            document = outputStream.toByteArray();
            contentType = "application/zip";
        }
        LOG.debug("File name: " + fileName);
        LOG.debug("Content Type: " + contentType);
        String signatureRequest = new String(Base64.encode(document));
        request.getSession().setAttribute(DOCUMENT_SESSION_ATTRIBUTE, document);
        request.getSession().setAttribute("SignatureRequest", signatureRequest);
        request.getSession().setAttribute("ContentType", contentType);
        response.sendRedirect(request.getContextPath() + this.postPage);
    }
