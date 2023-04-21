    protected void identifyRecordPositionInOriginalFile(FileInputStream pFis, String pStartingTag, String pEndingTag) throws Exception {
        int numBytesToRead = 10000;
        FileChannel fc = pFis.getChannel();
        long initialPosition = fc.position();
        long aproximateOffset = initialPosition - numBytesToRead;
        pStartingTag += getEntityCode().toString();
        String block = DataFileUtils.readStringAt(fc, aproximateOffset, numBytesToRead);
        long start = block.indexOf(pStartingTag);
        if (start < 0) {
            numBytesToRead = numBytesToRead * 4;
            aproximateOffset = initialPosition - numBytesToRead;
            block = DataFileUtils.readStringAt(fc, aproximateOffset, numBytesToRead);
            start = block.indexOf(pStartingTag);
            if (start < 0) {
                throw new IllegalStateException("Not able to acquire the start of the record.");
            }
        }
        long end = block.indexOf(pEndingTag, (int) start);
        if (start > end) {
            numBytesToRead = numBytesToRead * 5;
            block = DataFileUtils.readStringAt(fc, aproximateOffset, numBytesToRead);
            end = block.indexOf(pEndingTag, (int) start + numBytesToRead / 5);
            if (start > end) {
                System.out.println("EntityCode: " + getEntityCode());
                numBytesToRead = numBytesToRead * 10;
                block = DataFileUtils.readStringAt(fc, aproximateOffset, numBytesToRead);
                end = block.indexOf(pEndingTag, (int) start + numBytesToRead / 10);
                if (start > end) {
                    System.out.println("Block length: " + block.length());
                    System.out.println("EntityCode: " + getEntityCode());
                    throw new IllegalStateException("Not able to acquire the end of the record.");
                }
            }
        }
        start += aproximateOffset;
        end += aproximateOffset + pEndingTag.length();
        setOriginalRecordStartOffset(start);
        setOriginalRecordSize(end - start);
    }
