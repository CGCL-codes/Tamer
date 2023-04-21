    @SuppressWarnings("unchecked")
    protected final void action(Action ruleAction, NodeRef actionedUponNodeRef, ContentReader reader, Map<String, Object> options) throws AlfrescoRuntimeException {
        PDDocument pdf = null;
        InputStream is = null;
        File tempDir = null;
        ContentWriter writer = null;
        try {
            int splitFrequency = 0;
            String splitFrequencyString = options.get(PARAM_SPLIT_FREQUENCY).toString();
            if (!splitFrequencyString.equals("")) {
                splitFrequency = new Integer(splitFrequencyString);
            }
            is = reader.getContentInputStream();
            pdf = PDDocument.load(is);
            Splitter splitter = new Splitter();
            if (splitFrequency > 0) {
                splitter.setSplitAtPage(splitFrequency);
            }
            List pdfs = splitter.split(pdf);
            Iterator it = pdfs.iterator();
            int page = 1;
            int endPage = 0;
            File alfTempDir = TempFileProvider.getTempDir();
            tempDir = new File(alfTempDir.getPath() + File.separatorChar + actionedUponNodeRef.getId());
            tempDir.mkdir();
            while (it.hasNext()) {
                String pagePlus = "";
                String pg = "_pg";
                PDDocument splitpdf = (PDDocument) it.next();
                int pagesInPDF = splitpdf.getNumberOfPages();
                if (splitFrequency > 0) {
                    endPage = endPage + pagesInPDF;
                    pagePlus = "-" + endPage;
                    pg = "_pgs";
                }
                String fileNameSansExt = getFilenameSansExt(actionedUponNodeRef, FILE_EXTENSION);
                splitpdf.save(tempDir + "" + File.separatorChar + fileNameSansExt + pg + page + pagePlus + FILE_EXTENSION);
                if (splitFrequency > 0) {
                    page = (page++) + pagesInPDF;
                } else {
                    page++;
                }
                if (splitpdf != null) {
                    try {
                        splitpdf.close();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
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
