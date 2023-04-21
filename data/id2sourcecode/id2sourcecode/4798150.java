    public void run(final String arg) {
        String message = "--------------------------------------------\n" + serviceProviderInfo(ImageReaderSpi.class, false) + "--------------------------------------------\n" + serviceProviderInfo(ImageWriterSpi.class, false) + "--------------------------------------------\n";
        message += "Reader format names: ";
        final String[] readers = ImageIO.getReaderFormatNames();
        for (final String reader : readers) {
            message += reader + ", ";
        }
        message += "\n";
        message += "Reader format names: ";
        final String[] writers = ImageIO.getWriterFormatNames();
        for (final String writer : writers) {
            message += writer + ", ";
        }
        IJ.showMessage("ImageIO readers & writers", message);
    }
