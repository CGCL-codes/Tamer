    public static void writeHistoryFile(String fileName) throws Exception {
        Method method = _readline.getMethod("writeHistoryFile", new Class[] { String.class });
        method.invoke(null, new Object[] { fileName });
    }
