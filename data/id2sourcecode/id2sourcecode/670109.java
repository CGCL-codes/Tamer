    @PerfTest("Mapped File I/O nondirect -> charset, pattern split")
    public static void nioChar() throws IOException {
        int state = 0;
        final FileChannel fileChannel = new FileInputStream(testFileName).getChannel();
        final ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        final CharBuffer charBuffer = decoder.decode(buffer);
        try {
            for (String line : lineSplit.split(charBuffer)) {
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
