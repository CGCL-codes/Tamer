    public static MetaThumbData createImageThumb(Display display, IFile imagefile, MetaThumbData meta, boolean highQuality) {
        if (imagefile == null) {
            return meta;
        }
        final IPath location = imagefile.getLocation();
        if (location == null) {
            return meta;
        }
        File file = location.toFile();
        if (!file.exists()) {
            return meta;
        }
        if (getMinimalThumbSize() > file.length() && !(IO.isPsdImage(imagefile.getName()))) {
            setThumbSizeToMeta(meta, file);
            return meta;
        }
        File savePath = ThumbFileUtils.getDefaultThumbPath(imagefile);
        meta.thumbPath = savePath.getAbsolutePath();
        if (savePath == null) {
            setThumbSizeToMeta(meta, file);
            return meta;
        }
        File explorerPath = ThumbFileUtils.getExplorerThumbPath(imagefile);
        if (explorerPath.exists()) {
            try {
                FileUtils.copyFile(explorerPath, savePath);
                if (meta.exifDate == null) {
                    meta.exifDate = ExifDate.getExifDateFromLastModified(file);
                }
                return meta;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (highQuality) {
            Image largeThumb = loadLargeThumbImage(file.getAbsolutePath(), savePath.getAbsolutePath(), display);
            if (largeThumb != null) {
                int width = ThumbIO.DEFAULT_SIZE_X;
                int height = ThumbIO.DEFAULT_SIZE_Y;
                Rectangle bounds = largeThumb.getBounds();
                if (bounds.height > bounds.width) {
                    width = ThumbIO.DEFAULT_SIZE_Y;
                    height = ThumbIO.DEFAULT_SIZE_X;
                }
                Image thumb = ImageConversion.createQualityThumbImage(largeThumb, width, height, SWT.HIGH);
                if (meta != null) {
                    meta.width = thumb.getBounds().width;
                    meta.height = thumb.getBounds().height;
                    MetaThumbData findThumbData = SanExif.findThumbData(file.getAbsolutePath(), false);
                    if (findThumbData != null) {
                        meta.exifDate = findThumbData.exifDate;
                    }
                }
                largeThumb.dispose();
                if (thumb != null) {
                    IO.saveImageSWT(thumb.getImageData(), savePath.getAbsolutePath(), SWT.IMAGE_JPEG, 85);
                    thumb.dispose();
                }
            } else {
                createImageThumb(display, file, savePath, meta, false);
            }
        } else {
            createImageThumb(display, file, savePath, meta, false);
        }
        if (meta != null && meta.exifDate == null) {
            meta.exifDate = ExifDate.getExifDateFromLastModified(file);
        }
        return meta;
    }
