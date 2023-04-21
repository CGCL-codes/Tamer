    private ErrorsTracer() {
        mapping = new HashMap<AmhmErrors, String>();
        mapping.put(AmhmErrors.FILENOTFOUND, "File has not been found");
        mapping.put(AmhmErrors.MARSHALLINGFAILED, "An error occured while trying to marshall file.");
        mapping.put(AmhmErrors.IOException, "An error occured while trying to read/write file.");
    }
