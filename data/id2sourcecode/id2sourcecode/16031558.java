    public void testFileChannelTransfer() throws IOException {
        File entryFile = helper.getTestCaseFile(this);
        {
            FileWriter writer = new FileWriter(entryFile);
            String[] junk = new String[] { "cows jumping over the moon ", "quick foxes and lazy dogs ", "cdos, cmos, sivs, and cdss ", "print code instead of money ", "this time it's different ", "the next one will be even better " };
            Random rnd = new Random(0);
            for (int count = 500; count-- > 0; ) {
                int i = rnd.nextInt(junk.length);
                writer.write(junk[i]);
                if (count % 3 == 0) writer.write('\n');
            }
            writer.close();
        }
        FileChannel src = new FileInputStream(entryFile).getChannel();
        File copy = helper.getTestCaseFile(this + "-copy");
        FileChannel copyChannel = new RandomAccessFile(copy, "rw").getChannel();
        copyChannel = new WorkAroundFileChannel(copyChannel);
        long pos = 0;
        while (true) {
            long amtTransfered = copyChannel.transferFrom(src, pos, 100000);
            if (amtTransfered == 0) break;
            pos += amtTransfered;
        }
        copyChannel.close();
        src.close();
    }
