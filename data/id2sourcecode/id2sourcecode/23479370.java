    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        this.currentPagePosition = document.getPageSize().getHeight();
        if (useTemplate) {
            try {
                PdfContentByte cb = writer.getDirectContentUnder();
                PdfImportedPage templatePage = writer.getImportedPage(reader, 1);
                if (landscape) {
                    cb.addTemplate(templatePage, 0, -1, 1, 0, 0, document.getPageSize().getHeight());
                } else {
                    cb.addTemplate(templatePage, 1, 0, 0, 1, 0, 0);
                }
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
        if (printPageNumbers) {
            PdfContentByte cb = writer.getDirectContent();
            int currentPageNumber = writer.getCurrentPageNumber();
            int textAlignment;
            int horizontalTextPosition = 0;
            if (currentPageNumber % 2 == 0) {
                textAlignment = Element.ALIGN_LEFT;
                horizontalTextPosition = (int) document.left() + 20;
            } else {
                textAlignment = Element.ALIGN_RIGHT;
                horizontalTextPosition = (int) document.right() - 20;
            }
            try {
                cb.beginText();
                cb.setFontAndSize(BaseFont.createFont("Helvetica", BaseFont.WINANSI, false), 12.0f);
                cb.showTextAligned(textAlignment, "" + currentPageNumber, horizontalTextPosition, document.bottom() - 17, 0);
                cb.endText();
            } catch (IOException e) {
            } catch (DocumentException e) {
            }
        }
    }
