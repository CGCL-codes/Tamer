    protected void printTestPageCommand(String uuid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String testPageName = ServerLabels.get("server.test_page_name");
        if (Is.emptyString(testPageName)) {
            testPageName = "TestPage.pdf";
        }
        if (RemotePrintServiceLookup.isMobile(uuid)) {
            testPageName = "MobileTestPage.pdf";
        }
        String testPage = "net/sf/wubiq/reports/" + testPageName;
        String printServiceName = request.getParameter(ParameterKeys.PRINT_SERVICE_NAME);
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(testPage);
        PrintService printService = PrintServiceUtils.findPrinter(printServiceName, uuid);
        response.setContentType("text/html");
        if (printService != null) {
            PrintRequestAttributeSet requestAttributes = new HashPrintRequestAttributeSet();
            requestAttributes.add(new JobName("Test page", Locale.getDefault()));
            requestAttributes.add(MediaSizeName.NA_LETTER);
            requestAttributes.add(new Copies(1));
            PrinterJobManager.initializePrinterJobManager();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            Pageable pageable;
            try {
                pageable = PdfUtils.INSTANCE.pdfToPageable(input);
                synchronized (pageable) {
                    printerJob.setPageable(pageable);
                    try {
                        printerJob.setPrintService(printService);
                        printerJob.print(requestAttributes);
                    } catch (PrinterException e) {
                        LOG.error(e.getMessage(), e);
                        throw new ServletException(e);
                    } finally {
                        if (pageable != null && pageable instanceof PDDocument) {
                            ((PDDocument) pageable).close();
                        }
                    }
                }
            } catch (PrintException e) {
                throw new ServletException(e);
            }
            input.close();
            response.getWriter().print(ServerLabels.get("server.test_page_sent", printServiceName));
        } else {
            response.setContentType("application/pdf");
            OutputStream output = response.getOutputStream();
            while (input.available() > 0) {
                output.write(input.read());
            }
        }
    }
