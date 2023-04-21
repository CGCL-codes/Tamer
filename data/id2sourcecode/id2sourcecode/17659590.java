    private void feedLiveCacheFromFileCache(int line) throws IOException {
        this.liveCache.clear();
        final Map.Entry<Integer, Long> page0Entry = this.fileCacheIndex.floorEntry(line);
        this.liveCacheLineStart = this.liveCacheLineEnd = page0Entry.getKey();
        final FileChannel fileCacheChannel = this.fileCache.getChannel();
        final Map.Entry<Integer, Long> page1Entry = this.fileCacheIndex.higherEntry(page0Entry.getKey());
        final long page0Start = page0Entry.getValue();
        final long page0End = (page1Entry != null ? page1Entry.getValue() : fileCacheChannel.size());
        final ByteBuffer readBuffer = ByteBuffer.allocate((int) (page0End - page0Start));
        safeRead(fileCacheChannel, page0Start, readBuffer);
        readBuffer.flip();
        final TextDataBuffer lineBuffer = new TextDataBuffer();
        final int liveCacheSize = readBuffer.getInt();
        for (int lineIndex = 0; lineIndex < liveCacheSize; lineIndex++) {
            lineBuffer.clear();
            final int lineSize = readBuffer.getInt();
            for (int elementIndex = 0; elementIndex < lineSize; elementIndex++) {
                final int textSize = readBuffer.getInt();
                final byte[] textBytes = new byte[textSize];
                readBuffer.get(textBytes);
                final String text = new String(textBytes);
                final byte typeValue = readBuffer.get();
                TextDataType type;
                switch(typeValue) {
                    case 0:
                        type = TextDataType.VALUE;
                        break;
                    case 1:
                        type = TextDataType.SYMBOL;
                        break;
                    case 2:
                        type = TextDataType.KEYWORD;
                        break;
                    case 3:
                        type = TextDataType.OPERATOR;
                        break;
                    case 4:
                        type = TextDataType.LABEL;
                        break;
                    case 5:
                        type = TextDataType.COMMENT;
                        break;
                    case 6:
                        type = TextDataType.ERROR;
                        break;
                    default:
                        throw new RuntimeException("Unexpected text data type: " + typeValue);
                }
                lineBuffer.append(text, type);
            }
            this.liveCache.add(lineBuffer.toLine());
        }
        this.liveCacheLineEnd = this.liveCacheLineStart + this.liveCache.size();
    }
