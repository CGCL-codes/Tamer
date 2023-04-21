        public BenchmarkChannelHandler(int bufferSize) {
            reader = new ChannelReader(true, bufferSize, bufferSize * 10);
            writer = new ChannelWriter(true);
            writer.addBufferSizeListener(this);
            reader.setNextForwarder(writer);
            start = System.currentTimeMillis();
            System.out.println(DATE_FORMAT.format(start) + " Starting test " + (++counter) + "...");
        }
