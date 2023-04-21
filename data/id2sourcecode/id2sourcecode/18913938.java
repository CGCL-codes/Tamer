    public static void main(String[] argv) throws Exception {
        Hashtable parahash = new Hashtable();
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.equals("--h")) {
                printUsage();
                System.exit(ERRORCODE_OK);
            }
            if (arg.startsWith("--")) parahash.put(arg.substring(2), argv[++i]); else {
                printUsage();
                System.exit(ERRORCODE_CMDERR);
            }
        }
        readParameters(parahash);
        ARCFileReader reader;
        if (verb == VERBCODE_IDX) {
            reader = new ARCFileReader(arcFile, false);
            reader.generateIndex();
            System.out.println("Indexing Complete");
        }
        if (verb == VERBCODE_GET) {
            reader = new ARCFileReader(arcFile, true);
            String mimeType = reader.getCdxInstance().getMimeTypeforIdentifier(id);
            MimeTypeMapper mapper = new MimeTypeMapper();
            File dir = new File(exportDir);
            if (!dir.exists()) dir.mkdir();
            String exportFile = new File(exportDir, exportFileName + mapper.getExtension(mimeType)).getAbsolutePath();
            reader.writeResource(id, exportFile);
            System.out.println("Exported file of type " + mimeType + " to " + exportFile);
        }
    }
