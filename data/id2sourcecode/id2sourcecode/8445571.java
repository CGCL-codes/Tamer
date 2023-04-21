    protected final void action(Action ruleAction, NodeRef actionedUponNodeRef, NodeRef targetNodeRef, ContentReader reader, ContentReader targetContentReader, Map<String, Object> options) throws AlfrescoRuntimeException {
        PDDocument pdf = null;
        PDDocument pdfTarget = null;
        InputStream is = null;
        InputStream tis = null;
        File tempDir = null;
        ContentWriter writer = null;
        try {
            is = reader.getContentInputStream();
            tis = targetContentReader.getContentInputStream();
            pdf = PDDocument.load(is);
            pdfTarget = PDDocument.load(tis);
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.appendDocument(pdfTarget, pdf);
            merger.setDestinationFileName(options.get(PARAM_DESTINATION_NAME).toString());
            merger.mergeDocuments();
            File alfTempDir = TempFileProvider.getTempDir();
            tempDir = new File(alfTempDir.getPath() + File.separatorChar + actionedUponNodeRef.getId());
            tempDir.mkdir();
            String fileName = options.get(PARAM_DESTINATION_NAME).toString();
            pdfTarget.save(tempDir + "" + File.separatorChar + fileName + FILE_EXTENSION);
            for (File file : tempDir.listFiles()) {
                try {
                    if (file.isFile()) {
                        String filename = file.getName();
                        writer = getWriter(filename, (NodeRef) ruleAction.getParameterValue(PARAM_DESTINATION_FOLDER));
                        writer.setEncoding(reader.getEncoding());
                        writer.setMimetype(FILE_MIMETYPE);
                        writer.putContent(file);
                        file.delete();
                    }
                } catch (FileExistsException e) {
                    throw new AlfrescoRuntimeException("Failed to process file.", e);
                }
            }
        } catch (COSVisitorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdf != null) {
                try {
                    pdf.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (pdfTarget != null) {
                try {
                    pdfTarget.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (tempDir != null) {
                tempDir.delete();
            }
        }
        if (logger.isDebugEnabled()) {
        }
    }
