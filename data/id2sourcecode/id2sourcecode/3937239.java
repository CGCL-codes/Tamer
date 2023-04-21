    private InputStream getImageStream(File f) {
        if (COPY.equals(importMode)) {
            return createFileInputStream(f);
        }
        if (f.length() < ThumbIO.getMinimalThumbSize()) {
            return createFileInputStream(f);
        }
        final Image img = IO.loadImage(f.getAbsolutePath(), Display.getCurrent(), false, true);
        if (img == null) {
            return null;
        }
        final Rectangle bounds = img.getBounds();
        Image thumb = null;
        Point thumbSize = ThumbIO.getThumbSize(bounds.width, bounds.height);
        if (thumbSize == null) {
            img.dispose();
            return createFileInputStream(f);
        }
        if (SMALL.equals(importMode)) {
            if (bounds.width / 4 < thumbSize.x && bounds.height / 4 < thumbSize.y) {
                thumb = ImageConversion.createQualityThumbImage(img, thumbSize.x, thumbSize.y, SWT.HIGH);
            } else {
                thumb = ImageConversion.createQualityResizedImage(img, bounds.width / 4, bounds.height / 4, SWT.HIGH);
            }
        } else if (MEDIUM.equals(importMode)) {
            thumb = ImageConversion.createQualityResizedImage(img, bounds.width / 2, bounds.height / 2, SWT.HIGH);
        } else {
            thumb = ImageConversion.createQualityThumbImage(img, thumbSize.x, thumbSize.y, SWT.HIGH);
        }
        if (thumb == null) {
            img.dispose();
            return null;
        }
        ByteArrayOutputStream outstream = ImageConversion.saveToOutputStream(thumb.getImageData(), SWT.IMAGE_JPEG, 85);
        img.dispose();
        thumb.dispose();
        if (outstream == null) {
            return null;
        }
        return new ByteArrayInputStream(outstream.toByteArray());
    }
