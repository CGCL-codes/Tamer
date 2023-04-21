    public void print(Doc doc, PrintRequestAttributeSet attributes) throws PrintException {
        Object data;
        DocFlavor docflavor;
        String docflavorClassName;
        Image image = null;
        int x = 0;
        int y = 0;
        int width;
        int height;
        int iWidth;
        int iHeight;
        int newWidth;
        int newHeight;
        float scaleX;
        float scaleY;
        synchronized (this) {
            if (action) {
                throw new PrintException("printing is in action");
            }
            action = true;
        }
        try {
            docflavor = doc.getDocFlavor();
            try {
                data = doc.getPrintData();
            } catch (IOException ioexception) {
                throw new PrintException("no data for print: " + ioexception.toString());
            }
            if (docflavor == null) {
                throw new PrintException("flavor is null");
            }
            if (!begetPrintService.isDocFlavorSupported(docflavor)) {
                throw new PrintException("invalid flavor :" + docflavor.toString());
            }
            docflavorClassName = docflavor.getRepresentationClassName();
            if (docflavor.equals(DocFlavor.INPUT_STREAM.GIF) || docflavor.equals(DocFlavor.BYTE_ARRAY.GIF) || docflavor.equals(DocFlavor.INPUT_STREAM.JPEG) || docflavor.equals(DocFlavor.BYTE_ARRAY.JPEG) || docflavor.equals(DocFlavor.INPUT_STREAM.PNG) || docflavor.equals(DocFlavor.BYTE_ARRAY.PNG)) {
                try {
                    image = readImage(doc.getStreamForBytes());
                } catch (IOException ioe) {
                    throw new PrintException(ioe);
                }
            } else if (docflavor.equals(DocFlavor.URL.GIF) || docflavor.equals(DocFlavor.URL.JPEG) || docflavor.equals(DocFlavor.URL.PNG)) {
                URL url = (URL) data;
                try {
                    image = readImage(url.openStream());
                } catch (IOException ioe) {
                    throw new PrintException(ioe);
                }
            } else if (docflavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                Printable printable = (Printable) data;
                print(printable, null);
            } else if (docflavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) {
                Pageable pageable = (Pageable) data;
                print(null, pageable);
            } else {
                throw new PrintException("Wrong DocFlavor class: " + docflavorClassName);
            }
            if (image != null) {
                final PageFormat format = new PageFormat();
                final Paper p = format.getPaper();
                MediaSize size = null;
                if (attributes != null) {
                    if (attributes.containsKey(MediaSize.class)) {
                        size = (MediaSize) attributes.get(MediaSize.class);
                    } else if (attributes.containsKey(MediaSizeName.class)) {
                        MediaSizeName name = (MediaSizeName) attributes.get(MediaSizeName.class);
                        size = MediaSize.getMediaSizeForName(name);
                    } else {
                        size = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4);
                    }
                } else {
                    size = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4);
                }
                width = (int) (size.getX(MediaSize.INCH) * 72.0);
                height = (int) (size.getY(MediaSize.INCH) * 72.0);
                if (attributes != null) {
                    if (attributes.containsValue(OrientationRequested.LANDSCAPE)) {
                        int temp = width;
                        width = height;
                        height = temp;
                    }
                }
                iWidth = image.getWidth(null);
                iHeight = image.getHeight(null);
                x = (width - iWidth) / 2;
                y = (height - iHeight) / 2;
                p.setSize(width, height);
                p.setImageableArea(x, y, iWidth, iHeight);
                Graphics2D2PS graphics = new Graphics2D2PS(outstream, format);
                graphics.startPage(1);
                if (x < 0 || y < 0) {
                    scaleX = (float) image.getWidth(null) / (float) width;
                    scaleY = (float) image.getHeight(null) / (float) height;
                    newWidth = width;
                    newHeight = height;
                    if (scaleX > scaleY) {
                        newWidth = (int) ((float) iWidth / scaleX);
                        newHeight = (int) ((float) iHeight / scaleX);
                        x = 0;
                        y = (height - newHeight) / 2;
                    } else {
                        newWidth = (int) ((float) iWidth / scaleY);
                        newHeight = (int) ((float) iHeight / scaleY);
                        y = 0;
                        x = (width - newWidth) / 2;
                    }
                    graphics.drawImage(image, x, y, newWidth, newHeight, null);
                } else {
                    graphics.drawImage(image, x, y, null);
                }
                graphics.endOfPage(1);
                graphics.finish();
            }
        } finally {
            synchronized (this) {
                action = false;
            }
        }
    }
