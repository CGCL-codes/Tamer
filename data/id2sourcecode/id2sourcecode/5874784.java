    protected void writeImage(final org.w3c.dom.Document document, final File destFile) {
        Thread thread = new Thread() {

            private boolean pdfDocOpen = true;

            @Override
            public void run() {
                monitor.start();
                monitor.setProgress(0);
                UserAgentAdapter userAgent = null;
                GVTBuilder builder = null;
                BridgeContext ctx = null;
                RootGraphicsNode gvtRoot = null;
                try {
                    userAgent = new UserAgentAdapter();
                    ctx = new BridgeContext(userAgent, null, new DocumentLoader(userAgent));
                    builder = new GVTBuilder();
                    ctx.setDynamicState(BridgeContext.STATIC);
                    monitor.setProgress(5);
                    if (monitor.isCancelled()) {
                        monitor.stop();
                        ctx.dispose();
                        return;
                    }
                    GraphicsNode gvt = builder.build(ctx, document);
                    monitor.setProgress(40);
                    if (monitor.isCancelled()) {
                        monitor.stop();
                        ctx.dispose();
                        return;
                    }
                    if (gvt != null) {
                        gvtRoot = gvt.getRoot();
                        final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
                        com.lowagie.text.Rectangle rect = pageSize;
                        rect = isPortrait ? rect : rect.rotate();
                        final com.lowagie.text.Document pdfDoc = new com.lowagie.text.Document(rect, margins.left, margins.right, margins.top, margins.bottom) {

                            @Override
                            public void open() {
                                super.open();
                                synchronized (PDFExport.this) {
                                    pdfDocOpen = true;
                                }
                            }

                            @Override
                            public void close() {
                                try {
                                    super.close();
                                } catch (Exception ex) {
                                }
                                synchronized (PDFExport.this) {
                                    pdfDocOpen = false;
                                }
                            }
                        };
                        pdfDoc.addTitle(title);
                        pdfDoc.addAuthor(author);
                        pdfDoc.addSubject(subject);
                        pdfDoc.addKeywords(keywords);
                        pdfDoc.addCreator(creator);
                        PdfWriter writer = PdfWriter.getInstance(pdfDoc, out);
                        pdfDoc.open();
                        PdfContentByte cb = writer.getDirectContent();
                        final PdfTemplate tp = cb.createTemplate(rect.width(), rect.height());
                        tp.setWidth(rect.width());
                        tp.setHeight(rect.height());
                        Graphics2D g2 = tp.createGraphics(rect.width(), rect.height(), new DefaultFontMapper());
                        SVGDocumentImageCreator.handleGraphicsConfiguration(g2);
                        Thread writerStateThread = new Thread() {

                            @Override
                            public void run() {
                                while (pdfDocOpen) {
                                    if (monitor.isCancelled()) {
                                        synchronized (PDFExport.this) {
                                            pdfDocOpen = false;
                                        }
                                        try {
                                            tp.reset();
                                            pdfDoc.close();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        monitor.stop();
                                        break;
                                    }
                                    try {
                                        sleep(500);
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        };
                        writerStateThread.start();
                        monitor.setIndeterminate(true);
                        gvtRoot.paint(g2);
                        g2.dispose();
                        cb.addTemplate(tp, 0, 0);
                        pdfDoc.close();
                        out.flush();
                        out.close();
                        monitor.stop();
                    }
                    ctx.dispose();
                } catch (Exception ex) {
                    handleExportFailure();
                    if (ctx != null) {
                        ctx.dispose();
                    }
                }
            }
        };
        thread.start();
    }
