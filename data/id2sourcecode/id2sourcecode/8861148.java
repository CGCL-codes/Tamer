    private void setDwgVersion() throws IOException {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        long channelSize = fileChannel.size();
        bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, channelSize);
        byte[] versionBytes = { bb.get(0), bb.get(1), bb.get(2), bb.get(3), bb.get(4), bb.get(5) };
        ByteBuffer versionByteBuffer = ByteBuffer.wrap(versionBytes);
        String[] bs = new String[versionByteBuffer.capacity()];
        String versionString = "";
        for (int i = 0; i < versionByteBuffer.capacity(); i++) {
            bs[i] = new String(new byte[] { (byte) (versionByteBuffer.get(i)) });
            versionString = versionString + bs[i];
        }
        String version = (String) acadVersions.get(versionString);
        if (version == null) version = "Unknown Dwg format";
        this.dwgVersion = version;
    }
