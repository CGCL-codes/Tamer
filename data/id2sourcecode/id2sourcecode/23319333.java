    public void publish(File sourceDir, File outputDir, File outputBookFile) throws Exception {
        if (isVerboseEnabled()) {
            verbose("Publishing from \"" + sourceDir + " to \"" + outputDir + "\" as book \"" + outputBookFile + "\" ...");
        }
        if (isDebugEnabled()) {
            debug("sourceDir: " + sourceDir);
            debug("outputDir: " + outputDir);
            debug("outputBookFile: " + outputBookFile);
            debug("progressMonitor: " + getConfig().getProgressMonitor());
        }
        File tempDir = FileHelper.getSystemTempDir();
        if (isDebugEnabled()) {
            debug("tempDir: " + tempDir);
        }
        OpenOfficeDocConverter openOfficeDocConverter = new OpenOfficeDocConverter(getConfig().getServerContext());
        openOfficeDocConverter.setDebugEnabled(isDebugEnabled());
        openOfficeDocConverter.setVerboseEnabled(isVerboseEnabled());
        openOfficeDocConverter.setProgressMonitor(getConfig().getProgressMonitor());
        openOfficeDocConverter.convertDocuments(sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF);
        TocBuilder tocBuilder = null;
        if (getConfig().isBuildTocEnabled()) {
            tocBuilder = new TocBuilder();
            if (isDebugEnabled()) {
                debug("tocBuilder: " + tocBuilder);
            }
        }
        PdfBookBuilderConfig pdfConfig = new PdfBookBuilderConfig();
        PdfPageEvent pdfPageEventListener = new PdfPageEventLogger();
        File pdfSourceDir = outputDir;
        PdfBookBuilder pdfBookBuilder = new PdfBookBuilder();
        pdfConfig.setPageSize(getConfig().getPageSize());
        pdfConfig.setVerboseEnabled(getConfig().isVerboseEnabled());
        pdfConfig.setMetaTitle(getConfig().getMetaTitle());
        pdfConfig.setMetaAuthor(getConfig().getMetaAuthor());
        pdfConfig.setProgressMonitor(getConfig().getProgressMonitor());
        pdfConfig.setTocRowChangeListener(tocBuilder);
        pdfConfig.setPdfPageEventListener(pdfPageEventListener);
        pdfBookBuilder.setConfig(pdfConfig);
        pdfBookBuilder.buildBook(pdfSourceDir, outputBookFile);
        if (getConfig().isBuildTocEnabled()) {
            Toc toc = tocBuilder.getToc();
            if (isVerboseEnabled()) {
                verbose("Output PDF Table Of Contents contains " + toc.getTocRowCount() + " entries");
            }
            if (isDebugEnabled()) {
                debug("Output PDF Table Of Contents is " + toc);
                TocTracer.traceToc(toc);
            }
            File tocTemplateFile = getTocTemplateFile();
            debug("tocTemplateFile: " + tocTemplateFile);
            File tocOutputFile = new File(tempDir, getTempTocFileName());
            debug("tocOutputFile: " + tocOutputFile);
            buildTocDoc(tocTemplateFile, toc, tocOutputFile);
            File tocSourceFile = tocOutputFile;
            openOfficeDocConverter.convertDocument(tocSourceFile, tempDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF);
            if (isVerboseEnabled()) {
                verbose("Merging TOC with book");
            }
            String firstPdfName = FileNameHelper.rewriteFileNameSuffix(tocSourceFile, FileExtensionConstants.PDF_EXTENSION);
            File firstPdf = new File(tempDir, firstPdfName);
            File secondPdf = outputBookFile;
            String concatName = FileNameHelper.rewriteFileNameSuffix(outputBookFile, "-plus-toc", FileExtensionConstants.PDF_EXTENSION);
            File concatPdf = new File(outputBookFile.getParent(), concatName);
            concatPdf(firstPdf, secondPdf, concatPdf);
            if (concatPdf.exists()) {
                FileUtils.copyFile(concatPdf, outputBookFile);
                FileUtils.deleteQuietly(concatPdf);
            }
        }
    }
