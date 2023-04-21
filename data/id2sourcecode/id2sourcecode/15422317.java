    public void runReportsToWeb(ArrayList reports, HttpSession session, Map model, Map parameters) {
        logger.info("runReportsToWeb(), concatenation of several PDF reports");
        byte[] bytesTemp;
        ArrayList master = new ArrayList();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = null;
        PdfCopy writer = null;
        int pageOffset = 0;
        int i = 0;
        Iterator it = reports.iterator();
        Report report = null;
        while (it.hasNext()) {
            try {
                bytesTemp = null;
                report = (Report) it.next();
                if (report.getReportDataSource().getDescription().equals("JR Empty Data Source")) {
                    bytesTemp = JasperRunManager.runReportToPdf(report.getJasperReport(), parameters, new JREmptyDataSource());
                } else {
                    bytesTemp = JasperRunManager.runReportToPdf(report.getJasperReport(), parameters, report.getReportDataSource().getConnection());
                }
                logger.info("runReportsToWeb(), got report " + i + ", id " + report.getId() + ", desc " + report.getDescription() + ", num bytes is " + bytesTemp.length);
                PdfReader reader = new PdfReader(bytesTemp);
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();
                List bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0) SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    master.addAll(bookmarks);
                }
                pageOffset += n;
                logger.info("runReportsToWeb(), there are " + n + " pages in report " + report.getJasperDesign().getName());
                if (i == 0) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, baos);
                    document.open();
                }
                PdfImportedPage page;
                for (int j = 0; j < n; ) {
                    ++j;
                    page = writer.getImportedPage(reader, j);
                    writer.addPage(page);
                    logger.info("runReportsToWeb(), processed page " + j);
                }
            } catch (Exception e) {
                logger.error("runReportsToWeb(), " + e);
                e.printStackTrace();
            }
            i++;
        }
        if (master.size() > 0) writer.setOutlines(master);
        document.close();
        byte[] bytes = baos.toByteArray();
        model.put("bytes", bytes);
    }
