    public static DicomObject dicomResource(String resourceName) {
        URL url = cl.getResource(resourceName);
        if (url == null) throw new IllegalArgumentException("Unknown resource " + resourceName);
        DicomInputStream dis;
        try {
            dis = new DicomInputStream(url.openStream());
            return dis.readDicomObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Problem reading DICOM input stream:" + e);
        }
    }
