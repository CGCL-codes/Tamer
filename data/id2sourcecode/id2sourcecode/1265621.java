    public HSSFWorkbook getWorkbookFromClassPath(String name) throws IOException {
        InputStream is = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(name);
            is = url.openStream();
            POIFSFileSystem fs = new POIFSFileSystem(is);
            return new HSSFWorkbook(fs);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
