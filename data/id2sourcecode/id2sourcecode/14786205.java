    public GIFImageReaderSpi() {
        super(vendorName, version, names, suffixes, MIMETypes, readerClassName, new Class[] { ImageInputStream.class, InputStream.class }, writerSpiNames, supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName, nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames, extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat, nativeImageMetadataFormatName, nativeImageMetadataFormatClassName, extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
    }
