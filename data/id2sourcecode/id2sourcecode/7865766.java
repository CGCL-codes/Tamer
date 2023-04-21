    public void insertTemplate(String pdfTemplate, File control) throws IOException, DocumentException, JDOMException {
        PdfReader reader = new PdfReader(pdfTemplate);
        for (int currentPage = 1; currentPage <= reader.getNumberOfPages(); currentPage++) {
            PdfImportedPage page = writer.getImportedPage(reader, currentPage);
            writer.getDirectContent().addTemplate(page, 1, 0, 0, 1, 0, 0);
            if (pdfc.getPrintHelpLines()) overlayLines();
            if (pdfc.getPrintDetailedCoordinates()) overlayDetailedCoordinates();
            if (pdfc.getPrintSimpleCoordinates()) overlaySimpleCoordinates();
            setDefaultFont();
            ArrayList items = pdfc.getPrintItems();
            for (Iterator iter = items.iterator(); iter.hasNext(); ) {
                PrintItem e = (PrintItem) iter.next();
                if (e.getPageNumber() == currentPage) {
                    switch(e.getType()) {
                        case PrintItem.TEXT:
                            if (e.getFont().equals("default")) setDefaultFont(e.getSize()); else setFontAndSize(e.getFont(), e.getSize());
                            if (pdfc.getPrintPositioningHelper()) {
                                setColor(Color.red);
                                drawPositioningHelpLines(e.getX_coord(), e.getY_coord());
                            } else {
                                setColor(Color.black);
                            }
                            addText(e.getText(), e.getX_coord(), e.getY_coord());
                            break;
                        default:
                            break;
                    }
                }
            }
            if (currentPage != reader.getNumberOfPages()) newPage();
        }
    }
