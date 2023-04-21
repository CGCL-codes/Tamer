        public long getChecksum(TransferredFile mfile) throws IOException {
            Long sum = checksums.get(mfile);
            if (sum == null) {
                RandomAccessFile raf = new RandomAccessFile(mfile.getRealFile(), "r");
                Checksummer summer = new ChecksummerImpl(raf.getChannel(), raf.length());
                fireEvent(new ChecksummingEvent(mfile, summer));
                sum = summer.compute();
            }
            return sum;
        }
