    public InputStream createInputStream(FileInfo fi) throws IOException, MalformedURLException {
        InputStream is = null;
        boolean gzip = fi.fileName != null && (fi.fileName.endsWith(".gz") || fi.fileName.endsWith(".GZ"));
        if (fi.inputStream != null) is = fi.inputStream; else if (fi.url != null && !fi.url.equals("")) is = new URL(fi.url + fi.fileName).openStream(); else {
            if (fi.directory.length() > 0 && !fi.directory.endsWith(Prefs.separator)) fi.directory += Prefs.separator;
            File f = new File(fi.directory + fi.fileName);
            if (gzip) fi.compression = FileInfo.COMPRESSION_UNKNOWN;
            if (f == null || f.isDirectory() || !validateFileInfo(f, fi)) is = null; else is = new FileInputStream(f);
        }
        if (is != null) {
            if (fi.compression >= FileInfo.LZW) is = new RandomAccessStream(is); else if (gzip) is = new GZIPInputStream(is, 50000);
        }
        return is;
    }
