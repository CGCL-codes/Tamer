        public boolean onData(INonBlockingConnection con) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            con.markReadPosition();
            try {
                String cmd = con.readStringByDelimiter("\r\n");
                String name = con.readStringByDelimiter("\r\n");
                if (cmd.equalsIgnoreCase("put")) {
                    int size = con.readInt();
                    con.removeReadMark();
                    con.setHandler(new NetToFileStreamer(this, new File(dir + File.separator + name), size));
                } else if (cmd.equalsIgnoreCase("get")) {
                    con.removeReadMark();
                    File file = new File(dir + File.separator + name);
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    FileChannel fc = raf.getChannel();
                    con.write((int) file.length());
                    long size = con.transferFrom(fc);
                    fc.close();
                    raf.close();
                    System.out.println("SimpleFileServer file download " + file.getAbsolutePath() + " (size=" + size + ")");
                } else {
                    con.write("unsupported command\r\n");
                    con.close();
                }
                return true;
            } catch (BufferUnderflowException bue) {
                con.resetToReadMark();
                return true;
            }
        }
