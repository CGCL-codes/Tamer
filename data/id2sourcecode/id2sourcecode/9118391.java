    public void open(File f) throws OpenDriverException {
        fileShp = f;
        try {
            fin = new FileInputStream(f);
            channel = fin.getChannel();
            bb = new BigByteBuffer2(channel, FileChannel.MapMode.READ_ONLY);
            finShx = new FileInputStream(SHP.getShxFile(f));
            channelShx = finShx.getChannel();
            long sizeShx = channelShx.size();
            bbShx = new BigByteBuffer2(channelShx, FileChannel.MapMode.READ_ONLY);
            bbShx.order(ByteOrder.BIG_ENDIAN);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundDriverException(getName(), e, f.getAbsolutePath());
        } catch (IOException e) {
            throw new OpenDriverException(getName(), e);
        }
    }
