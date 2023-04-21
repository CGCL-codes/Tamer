    public static boolean CheckForUpdatedRebaseDBFile() {
        Date officalDate = new Date();
        try {
            URL url = new URL(urlAddress);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            officalDate = (Date) df.parse(conn.getHeaderField("Last-modified").substring(5, 16));
        } catch (Exception e) {
        }
        File file = new File(relativeDataFileLocation);
        if (file.lastModified() == 0L) return true;
        Date modifiedTime = new Date(file.lastModified());
        return officalDate.compareTo(modifiedTime) > 0;
    }
