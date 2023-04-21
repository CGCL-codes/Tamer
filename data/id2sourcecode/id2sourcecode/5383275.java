    public void open() throws OpenExpansionFileException {
        initDBFBuffer();
        try {
            shpChannelWriter = (FileChannel) getWriteChannel("c:/pruebaVicente.shp");
            dbfChannelWriter = (FileChannel) getWriteChannel("c:/pruebaVicente.dbf");
            shxChannelWriter = (FileChannel) getWriteChannel("c:/pruebaVicente.shx");
            File file = new File("c:/pruebaVicente.dbf");
            if (file.canWrite()) {
                try {
                    raf = new RandomAccessFile(file, "rw");
                    mode = FileChannel.MapMode.READ_WRITE;
                } catch (FileNotFoundException e) {
                    raf = new RandomAccessFile(file, "r");
                    mode = FileChannel.MapMode.READ_ONLY;
                }
            } else {
                raf = new RandomAccessFile(file, "r");
                mode = FileChannel.MapMode.READ_ONLY;
            }
            readChannel = raf.getChannel();
            bbRead = new BigByteBuffer2(readChannel, FileChannel.MapMode.READ_ONLY);
            finShx = new FileInputStream(getShxFile(file));
            bbDbfRead = new BigByteBuffer2(readChannel, mode);
            readChannelShx = finShx.getChannel();
            bbShxRead = new BigByteBuffer2(readChannelShx, FileChannel.MapMode.READ_ONLY);
            bbShxRead.order(ByteOrder.BIG_ENDIAN);
            chars = Charset.forName("ISO-8859-1");
        } catch (IOException e1) {
            throw new OpenExpansionFileException("", e1);
        }
    }
