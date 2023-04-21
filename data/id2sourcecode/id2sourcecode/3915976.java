    private void openPage(Dimension size, String title, Component component) throws IOException {
        if (size == null) size = component.getSize();
        resetClip(new Rectangle(0, 0, size.width, size.height));
        if (pageStream != null) {
            writeWarning("Page " + currentPage + " already open. " + "Call closePage() before starting a new one.");
            return;
        }
        BufferedImage thumbnail = null;
        if ((component != null) && isProperty(PDFGraphics2D.THUMBNAILS)) {
            thumbnail = ImageGraphics2D.generateThumbnail(component, getPropertyDimension(PDFGraphics2D.THUMBNAIL_SIZE));
        }
        currentPage++;
        if (title == null) title = "Page " + currentPage + " (untitled)";
        titles.add(title);
        PDFPage page = os.openPage("Page" + currentPage, "RootPage");
        page.setContents("PageContents" + currentPage);
        if (thumbnail != null) page.setThumb("Thumb" + currentPage);
        os.close(page);
        if (thumbnail != null) {
            PDFStream thumbnailStream = os.openStream("Thumb" + currentPage);
            thumbnailStream.image(thumbnail, Color.black, ImageConstants.ZLIB);
            os.close(thumbnailStream);
        }
        pageStream = os.openStream("PageContents" + currentPage, isProperty(COMPRESS) ? COMPRESS_FILTERS : NO_FILTERS);
        AffineTransform pageTrafo = new AffineTransform();
        pageTrafo.scale(1, -1);
        Dimension pageSize = PageConstants.getSize(getProperty(PAGE_SIZE), getProperty(ORIENTATION));
        Insets margins = PageConstants.getMargins(getPropertyInsets(PAGE_MARGINS), getProperty(ORIENTATION));
        pageTrafo.translate(margins.left, -(pageSize.getHeight() - margins.top));
        writeHeadline(pageTrafo);
        writeFootline(pageTrafo);
        double scaleFactor = Math.min(getWidth() / size.width, getHeight() / size.height);
        if ((scaleFactor < 1) || isProperty(FIT_TO_PAGE)) {
            pageTrafo.scale(scaleFactor, scaleFactor);
        } else {
            scaleFactor = 1;
        }
        double dx = (getWidth() - size.width * scaleFactor) / 2 / scaleFactor;
        double dy = (getHeight() - size.height * scaleFactor) / 2 / scaleFactor;
        pageTrafo.translate(dx, dy);
        writeTransform(pageTrafo);
        writeGraphicsSave();
        clipRect(0, 0, size.width, size.height);
        writeGraphicsSave();
        delayPaintQueue.setPageMatrix(pageTrafo);
        writeGraphicsState();
        writeBackground();
    }
