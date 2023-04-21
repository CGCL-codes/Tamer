    private void run() throws IOException {
        File destination = new File(DEFAULT_OUTPUTFILE_NAME);
        if (destination.exists()) destination.delete();
        ByteBuffer tempBuffer = ByteBuffer.allocate(16 * 1024);
        RandomAccessFile file = new RandomAccessFile(destination, "rw");
        MergeOutputAdapterWriteableByteChannel output = new MergeOutputAdapterWriteableByteChannel(tempBuffer, file.getChannel());
        try {
            RandomElement markov = new RandomShuffle(new Ranecu(new Date()), new Ranmar(new Date()), BLOCK_SIZE);
            final int HOW_MANY_BLOCKS = inputSize / BLOCK_SIZE;
            for (int i = 0; i < HOW_MANY_BLOCKS; i++) {
                for (int j = 0; j < BLOCK_SIZE; j++) {
                    int random = markov.choose(low, high);
                    output.put((long) random);
                }
            }
            final int REMAINDER = inputSize % BLOCK_SIZE;
            RandomElement markovRemainder = new RandomShuffle(new Ranecu(new Date()), new Ranmar(new Date()), REMAINDER);
            for (int i = 0; i < REMAINDER; i++) {
                int random = markovRemainder.choose(low, high);
                output.put((long) random);
            }
        } finally {
            if (null != output) output.close();
            if (null != file) file.close();
        }
    }
