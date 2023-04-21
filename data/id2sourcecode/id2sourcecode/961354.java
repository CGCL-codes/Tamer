    public void onPrintInvoices() {
        List<File> pdfList = new ArrayList<File>();
        for (int i = 0; i < m_ids.length; i++) {
            int C_Invoice_ID = m_ids[i];
            ReportEngine re = ReportEngine.get(EnvWeb.getCtx(), ReportEngine.INVOICE, C_Invoice_ID);
            pdfList.add(re.getPDF());
        }
        if (pdfList.size() > 1) {
            try {
                File outFile = File.createTempFile("PrintInvoices", ".pdf");
                Document document = null;
                PdfWriter copy = null;
                for (File f : pdfList) {
                    PdfReader reader = new PdfReader(f.getAbsolutePath());
                    if (document == null) {
                        document = new Document(reader.getPageSizeWithRotation(1));
                        copy = PdfWriter.getInstance(document, new FileOutputStream(outFile));
                        document.open();
                    }
                    PdfContentByte cb = copy.getDirectContent();
                    int pages = reader.getNumberOfPages();
                    for (int i = 1; i <= pages; i++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(reader, i);
                        cb.addTemplate(page, 0, 0);
                    }
                }
                document.close();
                Clients.showBusy(null, false);
                Window win = new SimplePDFViewer(this.getTitle(), new FileInputStream(outFile));
                SessionManager.getAppDesktop().showWindow(win, "center");
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        } else if (pdfList.size() > 0) {
            try {
                Window win = new SimplePDFViewer(this.getTitle(), new FileInputStream(pdfList.get(0)));
                SessionManager.getAppDesktop().showWindow(win, "center");
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }
