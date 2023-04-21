    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfReader reader;
        Rectangle rect;
        try {
            reader = new PdfReader(MyProperties.getOdd());
            oddPage = writer.getImportedPage(reader, 1);
            reader = new PdfReader(MyProperties.getEven());
            evenPage = writer.getImportedPage(reader, 1);
            bf = MyFonts.getBaseFont(false, false);
            rect = Dimensions.getDimension(false, Dimension.BODY);
            pagenumberOdd[0] = rect.getRight() + 35;
            pagenumberOdd[1] = rect.getBottom() - 17;
            rect = Dimensions.getDimension(true, Dimension.BODY);
            pagenumberEven[0] = rect.getLeft() - 35;
            pagenumberEven[1] = rect.getBottom() - 17;
            tabOdd = new PdfArray();
            tabEven = new PdfArray();
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }
