    public void help() {
        System.out.println("Conversion tool: reads JSON/XML text and writes to STDOUT re-formatted one.");
        System.out.println(" converter [-format=JSON|XML] [-indent=true|false] [-locale=locale] [-validate=<validation file or URL>] [-help] <input file or URL>");
        System.out.println("   By default reads STDIN and writes to STDOUT in indented JSON format.");
        System.out.println(" Supported formats are " + Arrays.asList(BigFactory.getInstance().getSupportedFormats()));
    }
