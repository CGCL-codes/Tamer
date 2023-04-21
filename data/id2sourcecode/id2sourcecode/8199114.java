    public void open(File file, String prefix) throws OpenDriverException {
        File indexFile = new File(prefix + ".shx");
        try {
            indexFis = new FileInputStream(indexFile);
            indexChannel = indexFis.getChannel();
            fis = new FileInputStream(file);
            channel = fis.getChannel();
            ByteBuffer headBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 100);
            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
            extent = new Rectangle2D.Double(headBuffer.getDouble(36), headBuffer.getDouble(44), headBuffer.getDouble(52) - headBuffer.getDouble(36), headBuffer.getDouble(60) - headBuffer.getDouble(44));
            headBuffer = null;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundDriverException(file.getName(), e, indexFile.getAbsolutePath());
        } catch (IOException e) {
            throw new OpenDriverException(file.getName(), e);
        }
    }
