    @Test
    public void testWriteIter() {
        System.out.println("   read/write using JAI iterators");
        WritableRectIter writeIter = RectIterFactory.createWritable(image, null);
        int i = 1;
        do {
            do {
                do {
                    writeIter.setSample(i);
                    i = (i % 31) + 1;
                } while (!writeIter.nextPixelDone());
                writeIter.startPixels();
            } while (!writeIter.nextLineDone());
            writeIter.startLines();
        } while (!writeIter.nextBandDone());
        RectIter readIter = RectIterFactory.create(image, null);
        i = 1;
        do {
            do {
                do {
                    assertTrue(readIter.getSample() == i);
                    i = (i % 31) + 1;
                } while (!readIter.nextPixelDone());
                readIter.startPixels();
            } while (!readIter.nextLineDone());
            readIter.startLines();
        } while (!readIter.nextBandDone());
    }
