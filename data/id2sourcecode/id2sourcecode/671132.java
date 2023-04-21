    public static void testSyb(String[] args) throws Exception {
        String path = "D:\\Programs\\Microsoft SQL Server\\MSSQL.1\\MSSQL\\Data\\pdd_log.ldf";
        String path1 = "\\\\.\\D:";
        File f = new File(path1);
        RandomAccessFile _deviceAccessFile = new RandomAccessFile(f, "r");
        FileChannel _deviceAccessChannel = _deviceAccessFile.getChannel();
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(1024);
        _deviceAccessChannel.read(bb);
        System.out.println(bb);
    }
