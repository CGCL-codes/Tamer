    public TIFFImageReaderSpi() {
        super(PackageUtil.getVendor(), PackageUtil.getVersion(), names, suffixes, MIMETypes, readerClassName, STANDARD_INPUT_TYPE, writerSpiNames, false, TIFFStreamMetadata.nativeMetadataFormatName, "com.tomgibara.imageio.impl.tiff.TIFFStreamMetadataFormat", null, null, true, TIFFImageMetadata.nativeMetadataFormatName, "com.tomgibara.imageio.impl.tiff.TIFFImageMetadataFormat", null, null);
    }
