    @PerfTest("New File I/O nondirect -> String,  manual split of java.lang.String")
    public static void nioManual() throws IOException {
        int state = 0;
        final FileChannel fileChannel = new FileInputStream(testFileName).getChannel();
        final ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(buffer);
        try {
            final String file = new String(buffer.array(), 0);
            for (String line : lineSplit.split(file)) {
                line = line.trim();
                final int indexOfSpace = line.indexOf(' ');
                final String word = indexOfSpace > 0 ? line.substring(0, indexOfSpace) : line;
                if ("arch".equals(word)) {
                    state += 1;
                } else if ("endmsg".equals(word)) {
                    state += 2;
                } else if ("end".equals(word)) {
                    state += 3;
                } else if ("msg".equals(word)) {
                    state += 4;
                } else if ("x".equals(word)) {
                    state += 5;
                } else if ("y".equals(word)) {
                    state += 6;
                } else if ("type".equals(word)) {
                    state += 7;
                } else if ("direction".equals(word)) {
                    state += 8;
                } else if ("face".equals(word)) {
                    state += 9;
                }
            }
        } finally {
            fileChannel.close();
        }
        sum += state;
    }
