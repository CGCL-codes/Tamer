    private void writeContent() throws IOException, DocumentException {
        addParagraph(buildSummary(), "systemmonitor.png");
        writeGraphs(collector.getCounterJRobins(), smallGraphs);
        final List<PdfCounterReport> pdfCounterReports = writeCounters();
        final List<PdfCounterRequestContextReport> pdfCounterRequestContextReports = new ArrayList<PdfCounterRequestContextReport>();
        if (!collectorServer) {
            addParagraph(getI18nString("Requetes_en_cours"), "hourglass.png");
            pdfCounterRequestContextReports.addAll(writeCurrentRequests(javaInformationsList.get(0), pdfCounterReports));
        }
        add(new Phrase("\n", normalFont));
        addParagraph(getI18nString("Informations_systemes"), "systeminfo.png");
        new PdfJavaInformationsReport(javaInformationsList, document).toPdf();
        addParagraph(getI18nString("Threads"), "threads.png");
        writeThreads(false);
        PdfCounterReport pdfJobCounterReport = null;
        Counter rangeJobCounter = null;
        if (isJobEnabled()) {
            rangeJobCounter = collector.getRangeCounter(range, Counter.JOB_COUNTER_NAME);
            add(new Phrase("\n", normalFont));
            addParagraph(getI18nString("Jobs"), "jobs.png");
            writeJobs(rangeJobCounter, false);
            pdfJobCounterReport = writeCounter(rangeJobCounter);
        }
        if (isCacheEnabled()) {
            add(new Phrase("\n", normalFont));
            addParagraph(getI18nString("Caches"), "caches.png");
            writeCaches(false);
        }
        document.newPage();
        addParagraph(getI18nString("Statistiques_detaillees"), "systemmonitor.png");
        writeGraphs(collector.getOtherJRobins(), smallOtherGraphs);
        document.newPage();
        writeGraphDetails();
        writeCountersDetails(pdfCounterReports);
        if (!collectorServer) {
            addParagraph(getI18nString("Requetes_en_cours_detaillees"), "hourglass.png");
            writeCurrentRequestsDetails(pdfCounterRequestContextReports);
        }
        addParagraph(getI18nString("Informations_systemes_detaillees"), "systeminfo.png");
        new PdfJavaInformationsReport(javaInformationsList, document).writeInformationsDetails();
        addParagraph(getI18nString("Threads_detailles"), "threads.png");
        writeThreads(true);
        if (isJobEnabled()) {
            add(new Phrase("\n", normalFont));
            addParagraph(getI18nString("Jobs_detailles"), "jobs.png");
            writeJobs(rangeJobCounter, true);
            writeCounterDetails(pdfJobCounterReport);
        }
        if (isCacheEnabled()) {
            add(new Phrase("\n", normalFont));
            addParagraph(getI18nString("Caches_detailles"), "caches.png");
            writeCaches(true);
        }
        writeDurationAndOverhead();
    }
