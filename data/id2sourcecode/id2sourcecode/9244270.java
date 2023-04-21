    private void createPage4(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        header2(canvas, 4);
        box(canvas, LEFT_X, UPPER_Y, RIGHT_X, BOTTOM_Y);
        float centerX = LEFT_X + (RIGHT_X - LEFT_X) / 2;
        vline(canvas, centerX, UPPER_Y, BOTTOM_Y);
        page4Equipment(canvas);
        float y = page4Jewelry(canvas, UPPER_Y);
        float yBottom = page4CharImageAndSize(canvas);
        page4MagicItems(canvas, y, yBottom);
        footer(canvas);
    }
