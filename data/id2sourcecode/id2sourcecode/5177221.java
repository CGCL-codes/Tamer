    @After("scopeClear() && target(strategy)")
    public void afterClear(final SimpleMovingAverageStrategy strategy) {
        if (lastStart == NOT_SET) {
            return;
        }
        final long diffMs = System.currentTimeMillis() - lastStart;
        final long diffSecs = diffMs / 1000;
        final int movingAverageLength = strategy.getMovingAverageLength();
        final String fileName = FILE_NAME + "_" + movingAverageLength + FILE_EXT;
        System.out.println("Finished for moving ave " + movingAverageLength + " in " + diffMs + "ms = " + diffSecs + "s");
        System.out.println("Writing to file " + fileName);
        try {
            FileOutputStream fos = null;
            FileChannel channel = null;
            try {
                fos = new FileOutputStream(fileName);
                channel = fos.getChannel();
                final ByteBuffer byteBuff = ByteBuffer.wrap(info.toString().getBytes());
                channel.write(byteBuff);
            } finally {
                if (channel != null) {
                    channel.close();
                } else if (fos != null) {
                    fos.close();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        info = new StringBuilder();
        lastStart = NOT_SET;
    }
