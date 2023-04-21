    protected void pdfCurrentWindow(PlugInContext context) {
        JFileChooser fileChooser = GUIUtil.createJFileChooserWithOverwritePrompting();
        fileChooser.setDialogTitle("Save PDF");
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setMultiSelectionEnabled(false);
        GUIUtil.removeChoosableFileFilters(fileChooser);
        FileFilter fileFilter1 = GUIUtil.createFileFilter("PDF Files", new String[] { "pdf" });
        fileChooser.addChoosableFileFilter(fileFilter1);
        fileChooser.setFileFilter(fileFilter1);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yy_HHmm-ss");
        String dateStr = df.format(date);
        String suggestedFileName = context.getTask().getName() + "_" + dateStr + ".pdf";
        fileChooser.setSelectedFile(new File(suggestedFileName));
        if (JFileChooser.APPROVE_OPTION != fileChooser.showSaveDialog(context.getLayerViewPanel())) return;
        FileUtil.PutSaveDirectory(context.getWorkbenchContext(), fileChooser.getCurrentDirectory());
        String pdfFileName = fileChooser.getSelectedFile().getPath();
        if (!(pdfFileName.toLowerCase().endsWith(".pdf"))) pdfFileName = pdfFileName + ".pdf";
        pluginContext = context;
        printLayerables = new ArrayList(context.getLayerManager().getLayerables(Layerable.class));
        Collections.reverse(printLayerables);
        ArrayList oldStyleList = PrinterDriver.optimizeForVectors(printLayerables, removeThemeFills, removeBasicFills, changeLineWidth, (float) lineWidthMultiplier, removeTransparency);
        final Throwable[] throwable = new Throwable[] { null };
        printPanel = createLayerPanel(context.getLayerManager(), throwable);
        PDFDriver.disableDoubleBuffering(printPanel);
        PDFDriver pdfDriver = new PDFDriver(context, printPanel);
        ScaleBarRenderer.setEnabled(ScaleBarRenderer.isEnabled(context.getLayerViewPanel()), printPanel);
        NorthArrowRenderer.setEnabled(NorthArrowRenderer.isEnabled(context.getLayerViewPanel()), printPanel);
        pdfDriver.setTaskFrame((TaskFrame) context.getWorkbenchFrame().getActiveInternalFrame());
        pdfDriver.setPrintBorder(printBorder);
        pdfDriver.setPrintLayerables(printLayerables);
        windowEnvelope = pluginContext.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
        fence = pluginContext.getLayerViewPanel().getFence();
        try {
            try {
                Document document = new Document(new Rectangle((float) pdfPageWidth * INCH, (float) pdfPageHeight * INCH));
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
                writer.setCropBoxSize(new Rectangle(0, 0, ((float) pdfPageWidth * INCH), ((float) pdfPageHeight * INCH)));
                writer.setPdfVersion(PdfWriter.VERSION_1_5);
                writer.setViewerPreferences(PdfWriter.PageModeUseOC);
                document.open();
                DefaultFontMapper mapper = new DefaultFontMapper();
                FontFactory.registerDirectories();
                PageFormat pageFormat = new PageFormat();
                Paper paper = new Paper();
                double width = pdfPageWidth * INCH;
                double height = pdfPageHeight * INCH;
                paper.setSize(width, height);
                paper.setImageableArea(0, 0, width, height);
                pageFormat.setPaper(paper);
                double w = pageFormat.getImageableWidth();
                double h = pageFormat.getImageableHeight();
                PdfContentByte cb = writer.getDirectContent();
                PdfTemplate tp = cb.createTemplate((float) w, (float) h);
                Graphics2D g2 = tp.createGraphics((float) w, (float) h, mapper);
                tp.setWidth((float) w);
                tp.setHeight((float) h);
                pdfDriver.setCb(tp);
                pdfDriver.setWriter(writer);
                try {
                    initLayerViewPanel(pageFormat);
                    pdfDriver.setResolutionFactor(resolutionFactor);
                } catch (Exception e) {
                    String message = (e.getMessage() == null) ? e.toString() : e.getMessage();
                    System.err.println(message);
                }
                try {
                    pdfDriver.print(g2, pageFormat, 0);
                } catch (PrinterException e) {
                    String message = (e.getMessage() == null) ? e.toString() : e.getMessage();
                    System.err.println(message);
                }
                g2.dispose();
                tp.sanityCheck();
                cb.addTemplate(tp, 0, 0);
                cb.sanityCheck();
                document.close();
            } catch (DocumentException de) {
                System.err.println(de.getMessage());
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        } finally {
            if (oldStyleList != null) {
                boolean wasFiringEvents = printPanel.getLayerManager().isFiringEvents();
                printPanel.getLayerManager().setFiringEvents(false);
                int j = 0;
                for (Iterator i = printLayerables.iterator(); i.hasNext(); ) {
                    Object layerable = i.next();
                    if (layerable instanceof Layer) {
                        Layer layer = (Layer) layerable;
                        layer.setStyles((Collection) oldStyleList.get(j++));
                    }
                }
                printPanel.getLayerManager().setFiringEvents(wasFiringEvents);
            }
            if (printPanel != null) {
                PrinterDriver.enableDoubleBuffering(printPanel);
                printPanel.dispose();
                printPanel = null;
            }
            context.getWorkbenchFrame().setStatusMessage(FINISHED_MESSAGE);
        }
    }
