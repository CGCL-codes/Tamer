    public void readFASTASizeUsingChannel() throws IOException {
        FileChannel fastaChannel = new FileInputStream(this).getChannel();
        int totalSeqCount = 0;
        long totalResiduesCount = 0;
        try {
            long fastaFileSize = this.length();
            long fastaFileReadOffset = 0L;
            final int bufferSize = 1024 * 1024;
            ByteBuffer fastaBuffer = ByteBuffer.allocateDirect(bufferSize);
            int fastaReadState = FASTAFileTokenizer.UNKNOWN;
            for (; fastaFileReadOffset < fastaFileSize; ) {
                long nBytes = fastaChannel.read(fastaBuffer);
                if (nBytes <= 0) {
                    fastaBuffer.limit(0);
                    break;
                } else {
                    fastaBuffer.flip();
                    fastaFileReadOffset += nBytes;
                }
                for (; ; ) {
                    if (!fastaBuffer.hasRemaining()) {
                        fastaBuffer.clear();
                        break;
                    }
                    int b = fastaBuffer.get();
                    if (b == '\r') {
                    } else if (b == '\n') {
                        if (fastaReadState == FASTAFileTokenizer.DEFLINE) {
                            fastaReadState = FASTAFileTokenizer.SEQUENCELINE;
                        }
                    } else if (b == '>') {
                        if (fastaReadState == FASTAFileTokenizer.UNKNOWN) {
                            fastaReadState = FASTAFileTokenizer.STARTDEFLINE;
                        } else if (fastaReadState == FASTAFileTokenizer.SEQUENCELINE) {
                            fastaReadState = FASTAFileTokenizer.STARTDEFLINE;
                        }
                        if (fastaReadState == FASTAFileTokenizer.STARTDEFLINE) {
                            fastaReadState = FASTAFileTokenizer.DEFLINE;
                            totalSeqCount++;
                        }
                    } else {
                        if (fastaReadState == FASTAFileTokenizer.SEQUENCELINE) {
                            totalResiduesCount++;
                        }
                    }
                }
            }
            size.setBases(totalResiduesCount);
            size.setEntries(totalSeqCount);
        } finally {
            fastaChannel.close();
        }
    }
