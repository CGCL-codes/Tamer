    public static boolean verifyTypeWriter(String readerName, Class typeClass, String order[], TupleFlowParameters parameters, ErrorHandler handler) {
        if (!parameters.writerExists(readerName, typeClass.getName(), order)) {
            handler.addError("No writer named '" + readerName + "' was found in this stage.");
            return false;
        }
        return true;
    }
