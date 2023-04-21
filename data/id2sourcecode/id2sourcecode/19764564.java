        public boolean onData(INonBlockingConnection con) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            if (fc == null) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                raf = new RandomAccessFile(file, "rw");
                fc = raf.getChannel();
            }
            try {
                int available = con.available();
                if ((available <= 0) || (remaining == 0)) {
                    return true;
                }
                if (available < remaining) {
                    con.transferTo(fc, available);
                    remaining = remaining - available;
                } else {
                    con.transferTo(fc, remaining);
                    fc.close();
                    raf.close();
                    System.out.println("SimpleFileServer file uploaded " + file.getAbsolutePath() + " (size=" + size + ")");
                    remaining = 0;
                    con.setHandler(orgHandler);
                }
            } catch (IOException ioe) {
                fc.close();
                file.delete();
                throw ioe;
            }
            return true;
        }
