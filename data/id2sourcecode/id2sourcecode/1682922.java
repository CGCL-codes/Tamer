    @SuppressWarnings("unchecked")
    protected final void action(Action ruleAction, NodeRef actionedUponNodeRef, ContentReader reader, ContentReader insertReader, Map<String, Object> options) throws AlfrescoRuntimeException {
        PDDocument pdf = null;
        PDDocument insertContentPDF = null;
        InputStream is = null;
        InputStream cis = null;
        File tempDir = null;
        ContentWriter writer = null;
        try {
            int insertAt = new Integer(((String) options.get(PARAM_INSERT_AT_PAGE))).intValue();
            is = reader.getContentInputStream();
            cis = insertReader.getContentInputStream();
            pdf = PDDocument.load(is);
            insertContentPDF = PDDocument.load(cis);
            Splitter splitter = new Splitter();
            splitter.setSplitAtPage(insertAt - 1);
            List pdfs = splitter.split(pdf);
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.appendDocument((PDDocument) pdfs.get(0), insertContentPDF);
            merger.appendDocument((PDDocument) pdfs.get(0), (PDDocument) pdfs.get(1));
            merger.setDestinationFileName(options.get(PARAM_DESTINATION_NAME).toString());
            merger.mergeDocuments();
            File alfTempDir = TempFileProvider.getTempDir();
            tempDir = new File(alfTempDir.getPath() + File.separatorChar + actionedUponNodeRef.getId());
            tempDir.mkdir();
            String fileName = options.get(PARAM_DESTINATION_NAME).toString();
            PDDocument completePDF = (PDDocument) pdfs.get(0);
            completePDF.save(tempDir + "" + File.separatorChar + fileName + FILE_EXTENSION);
            if (completePDF != null) {
                try {
                    completePDF.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
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
