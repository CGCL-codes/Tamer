    private void writeThreads(boolean includeDetails) throws DocumentException, IOException {
        String eol = "";
        for (final JavaInformations javaInformations : javaInformationsList) {
            add(new Phrase(eol + I18N.getFormattedString("Threads_sur", javaInformations.getHost()) + ": ", boldFont));
            add(new Phrase(I18N.getFormattedString("thread_count", javaInformations.getThreadCount(), javaInformations.getPeakThreadCount(), javaInformations.getTotalStartedThreadCount()), normalFont));
            final PdfThreadInformationsReport pdfThreadInformationsReport = new PdfThreadInformationsReport(javaInformations.getThreadInformationsList(), javaInformations.isStackTraceEnabled(), pdfDocumentFactory, document);
            pdfThreadInformationsReport.writeDeadlocks();
            if (includeDetails) {
                pdfThreadInformationsReport.toPdf();
            }
            eol = "\n";
        }
    }
