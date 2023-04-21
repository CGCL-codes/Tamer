    public static boolean isOOXML(URL url) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (false == "[Content_Types].xml".equals(zipEntry.getName())) {
                continue;
            }
            return true;
        }
        return false;
    }
