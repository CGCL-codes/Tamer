    public ImageWriterSpi(String vendorName, String version, String[] names, String[] suffixes, String[] MIMETypes, String writerClassName, Class[] outputTypes, String[] readerSpiNames, boolean supportsStandardStreamMetadataFormat, String nativeStreamMetadataFormatName, String nativeStreamMetadataFormatClassName, String[] extraStreamMetadataFormatNames, String[] extraStreamMetadataFormatClassNames, boolean supportsStandardImageMetadataFormat, String nativeImageMetadataFormatName, String nativeImageMetadataFormatClassName, String[] extraImageMetadataFormatNames, String[] extraImageMetadataFormatClassNames) {
        super(vendorName, version, names, suffixes, MIMETypes, writerClassName, supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName, nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames, extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat, nativeImageMetadataFormatName, nativeImageMetadataFormatClassName, extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
        if (writerClassName == null) throw new IllegalArgumentException("writerClassName is null");
        if (outputTypes == null || outputTypes.length == 0) throw new IllegalArgumentException("outputTypes may not be null or empty");
        this.outputTypes = outputTypes;
        this.readerSpiNames = readerSpiNames;
    }
