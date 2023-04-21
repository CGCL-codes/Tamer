    @SuppressWarnings("unchecked")
    protected final void action(Action ruleAction, NodeRef actionedUponNodeRef, ContentReader reader, Map<String, Object> options) throws AlfrescoRuntimeException {
        PDDocument pdf = null;
        InputStream is = null;
        File tempDir = null;
        ContentWriter writer = null;
        try {
            int splitFrequency = 0;
            String splitFrequencyString = options.get(PARAM_SPLIT_AT_PAGE).toString();
            if (!splitFrequencyString.equals("")) {
                splitFrequency = new Integer(splitFrequencyString);
            }
            is = reader.getContentInputStream();
            pdf = PDDocument.load(is);
            Splitter splitter = new Splitter();
            splitter.setSplitAtPage(splitFrequency - 1);
            List pdfs = splitter.split(pdf);
            int page = 1;
            File alfTempDir = TempFileProvider.getTempDir();
            tempDir = new File(alfTempDir.getPath() + File.separatorChar + actionedUponNodeRef.getId());
            tempDir.mkdir();
            PDDocument firstPDF = (PDDocument) pdfs.remove(0);
            int pagesInFirstPDF = firstPDF.getNumberOfPages();
            String lastPage = "";
            String pg = "_pg";
            if (pagesInFirstPDF > 1) {
                pg = "_pgs";
                lastPage = "-" + pagesInFirstPDF;
            }
            String fileNameSansExt = getFilenameSansExt(actionedUponNodeRef, FILE_EXTENSION);
            firstPDF.save(tempDir + "" + File.separatorChar + fileNameSansExt + pg + page + lastPage + FILE_EXTENSION);
            if (firstPDF != null) {
                try {
                    firstPDF.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            PDDocument secondPDF = null;
            Iterator its = pdfs.iterator();
            int pagesInSecondPDF = 0;
            while (its.hasNext()) {
                if (secondPDF != null) {
                    PDDocument splitpdf = (PDDocument) its.next();
                    int pagesInThisPDF = splitpdf.getNumberOfPages();
                    pagesInSecondPDF = pagesInSecondPDF + pagesInThisPDF;
                    PDFMergerUtility merger = new PDFMergerUtility();
                    merger.appendDocument(secondPDF, splitpdf);
                    merger.mergeDocuments();
                    if (splitpdf != null) {
                        try {
                            splitpdf.close();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    secondPDF = (PDDocument) its.next();
                    pagesInSecondPDF = secondPDF.getNumberOfPages();
                }
            }
            if (pagesInSecondPDF > 1) {
                pg = "_pgs";
                lastPage = "-" + (pagesInSecondPDF + pagesInFirstPDF);
            } else {
                pg = "_pg";
                lastPage = "";
            }
            secondPDF.save(tempDir + "" + File.separatorChar + fileNameSansExt + pg + splitFrequency + lastPage + FILE_EXTENSION);
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
