            public Boolean construct() {
                try {
                    Document document = new Document(new Rectangle(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight()));
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(lPDFFileLocation));
                    document.open();
                    PdfContentByte cb = writer.getDirectContent();
                    Graphics2D g2;
                    for (int i = 0; i < lSavableLines.size(); i++) {
                        if (lSavableLines.get(i).isSelected()) {
                            Savable lSavable = lSavableLines.get(i).getSavable();
                            if (lSavable.isSvg()) {
                                UserAgent userAgent = new UserAgentAdapter();
                                DocumentLoader loader = new DocumentLoader(userAgent);
                                BridgeContext ctx = new BridgeContext(userAgent, loader);
                                GVTBuilder builder = new GVTBuilder();
                                ctx.setDynamicState(BridgeContext.DYNAMIC);
                                PdfTemplate map = cb.createTemplate(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                g2 = map.createGraphics(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight(), new DefaultFontMapper());
                                GraphicsNode graphicsToPaint = builder.build(ctx, lSavableLines.get(i).getSavable().getSVG());
                                graphicsToPaint.paint(g2);
                                g2.dispose();
                                cb.addTemplate(map, 0, 0);
                                document.newPage();
                            } else if (lSavable.isChart()) {
                                g2 = cb.createGraphicsShapes(iInfoFeeder.getGraphableWidth(), iInfoFeeder.getGraphableHeight());
                                lSavableLines.get(i).getSavable().getContentPanel().paintAll(g2);
                                g2.dispose();
                                document.newPage();
                            } else if (lSavable.isText()) {
                                File lFile = new File(lCSVFileLocation);
                                if (lFile.exists() == false) {
                                    lFile.createNewFile();
                                }
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lFile)));
                                String lContent = lSavable.getText();
                                bw.write(lContent);
                                bw.flush();
                                bw.close();
                            }
                        }
                    }
                    document.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
