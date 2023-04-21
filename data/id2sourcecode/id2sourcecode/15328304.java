    public ImageInfo getImageInfo(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        ImageContents imageContents = readImageContents(byteSource);
        if (imageContents == null) throw new ImageReadException("PSD: Couldn't read blocks");
        PSDHeaderInfo header = imageContents.header;
        if (header == null) throw new ImageReadException("PSD: Couldn't read Header");
        int Width = header.Columns;
        int Height = header.Rows;
        ArrayList Comments = new ArrayList();
        int BitsPerPixel = header.Depth * getChannelsPerMode(header.Mode);
        if (BitsPerPixel < 0) BitsPerPixel = 0;
        ImageFormat Format = ImageFormat.IMAGE_FORMAT_PSD;
        String FormatName = "Photoshop";
        String MimeType = "image/x-photoshop";
        int NumberOfImages = -1;
        boolean isProgressive = false;
        int PhysicalWidthDpi = 72;
        float PhysicalWidthInch = (float) ((double) Width / (double) PhysicalWidthDpi);
        int PhysicalHeightDpi = 72;
        float PhysicalHeightInch = (float) ((double) Height / (double) PhysicalHeightDpi);
        String FormatDetails = "Psd";
        boolean isTransparent = false;
        boolean usesPalette = header.Mode == COLOR_MODE_INDEXED;
        int ColorType = ImageInfo.COLOR_TYPE_UNKNOWN;
        String compressionAlgorithm;
        switch(imageContents.Compression) {
            case 0:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_NONE;
                break;
            case 1:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_PSD;
                break;
            default:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
        }
        ImageInfo result = new ImageInfo(FormatDetails, BitsPerPixel, Comments, Format, FormatName, Height, MimeType, NumberOfImages, PhysicalHeightDpi, PhysicalHeightInch, PhysicalWidthDpi, PhysicalWidthInch, Width, isProgressive, isTransparent, usesPalette, ColorType, compressionAlgorithm);
        return result;
    }
