    public static List<HSSFWorkbook> extractWorkbooks(Collection<String> paths) {
        List<HSSFWorkbook> workbooks = new ArrayList<HSSFWorkbook>();
        for (String path : paths) {
            if (path.endsWith(".xls")) {
                try {
                    URL url = new URL(path);
                    workbooks.add(new HSSFWorkbook(new POIFSFileSystem(url.openStream())));
                } catch (IOException e) {
                    log.warn("could not install the file: " + path, e);
                }
            }
        }
        return workbooks;
    }
