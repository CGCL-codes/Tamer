    public void closeItem() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        this.source = evalAttribute(SOURCE);
        this.from = getIntegerAttribute(FROM, -1, true, null);
        this.to = getIntegerAttribute(TO, -1, true, null);
        float xOffset = getDimensionAttribute(X, 0.0F, true, null);
        float yOffset = getDimensionAttribute(Y, 0.0F, true, null);
        int page = 1;
        List<OutlineItem> outlineItems = null;
        boolean clearPageHandlers = false;
        try {
            PdfReader reader = null;
            if (source instanceof byte[]) {
                reader = new PdfReader((byte[]) source);
            } else if (source instanceof DocumentHolder) {
                clearPageHandlers = true;
                DocumentHolder docHolder = (DocumentHolder) source;
                reader = new PdfReader(docHolder.getDocumentStream());
                outlineItems = docHolder.getOutlines();
            } else if (source instanceof File) {
                reader = new PdfReader(new FileInputStream((File) source));
            } else {
                reader = new PdfReader(documentHandler.loadResource(source.toString()));
            }
            PdfWriter writer = (PdfWriter) documentHandler.getDocumentWriter();
            int numPages = reader.getNumberOfPages();
            if (from > numPages) {
                throw new IOException("First page number (from) for imported document is out of range: " + from);
            }
            if (to < 0) {
                to = numPages;
            } else if (to > numPages) {
                throw new IOException("Last page number (to) for imported document is out of range: " + to);
            }
            if (from > to) {
                throw new IOException("Illegal page range: " + from + " to " + to + ".");
            }
            int firstPage = 1;
            if (from > 0) {
                firstPage = from;
            }
            page = firstPage;
            Document doc = documentHandler.getDocument();
            Map<String, PdfOutline> outlines = new HashMap<String, PdfOutline>();
            while (true) {
                int rotation = reader.getPageRotation(page);
                PdfImportedPage importedPage = writer.getImportedPage(reader, page);
                Rectangle bbox = importedPage.getBoundingBox();
                if (rotation == 90 || rotation == 270) {
                    bbox = bbox.rotate();
                }
                doc.setPageSize(bbox);
                doc.newPage();
                float curXOffset = xOffset;
                float curYOffset = yOffset;
                if (clearPageHandlers) {
                    documentHandler.clearPageEventHandlers();
                }
                if (outlineItems != null) {
                    int numOutlineItems = outlineItems.size();
                    for (int i = 0; i < numOutlineItems; i++) {
                        OutlineItem outlineItem = (OutlineItem) outlineItems.get(i);
                        if (outlineItem.getPage() == page) {
                            PdfOutline parentOutline = null;
                            String parent = outlineItem.getParent();
                            if (parent == null) {
                                parentOutline = writer.getRootOutline();
                            } else {
                                parentOutline = (PdfOutline) outlines.get(parent);
                            }
                            float x = outlineItem.getX();
                            float y = outlineItem.getY();
                            if (documentHandler.isDestinationsSupported()) {
                                PdfDestination destination = null;
                                if (x > 0.0F) {
                                    destination = new PdfDestination(PdfDestination.XYZ, x, y, 0.0F);
                                } else {
                                    destination = new PdfDestination(PdfDestination.FITH, y);
                                }
                                PdfContentByte directContent = writer.getDirectContent();
                                directContent.localDestination(outlineItem.getName(), destination);
                            }
                            PdfOutline outline = new PdfOutline(parentOutline, new PdfDestination(PdfDestination.FITH, y), outlineItem.getTitle());
                            outlines.put(outlineItem.getName(), outline);
                        }
                    }
                }
                PdfContentByte directContent = writer.getDirectContent();
                if (rotation == 90 || rotation == 270) {
                    directContent.addTemplate(importedPage, 0, -1f, 1f, 0, curXOffset, bbox.getHeight() + curYOffset);
                } else {
                    directContent.addTemplate(importedPage, curXOffset, curYOffset);
                }
                ++page;
                if (to > 0) {
                    if (page > to) {
                        break;
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            if (page == from) {
                throw new DocumentHandlerException(locator(), "The page #" + page + " could not be loaded from document " + source + "'.", ex);
            }
        } catch (IOException ex) {
            throw new DocumentHandlerException(locator(), "Unable to load document from location '" + source + "': " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new DocumentHandlerException(locator(), "The page #" + page + " could not be written to the document.", ex);
        }
    }
