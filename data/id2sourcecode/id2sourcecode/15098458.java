    public static void main(String[] args) throws IOException {
        IsoFile isoFile = new IsoFile(new FileInputStream(args[0]).getChannel());
        Path p = new Path(isoFile);
        AppleDataBox data = (AppleDataBox) p.getPath("/moov/udta/meta/ilst/covr/data");
        String ext;
        if ((data.getFlags() & 0x1) == 0x1) {
            ext = "jpg";
        } else if ((data.getFlags() & 0x2) == 0x2) {
            ext = "png";
        } else {
            System.err.println("Unknown Image Type");
            ext = "unknown";
        }
        FileOutputStream fos = new FileOutputStream("image." + ext);
        fos.write(data.getData());
        fos.close();
    }
