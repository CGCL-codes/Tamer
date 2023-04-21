    public void parse(File file) throws BerkeleyMailFileParserException, BerkeleyEventHandlerException {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            int fileSize = (int) fileChannel.size();
            if (fileSize > 0) {
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
                CharSequence charBuffer = new BerkeleyCharSequence(mappedByteBuffer);
                Matcher messageStartMatcher = messageStartPattern.matcher(charBuffer);
                Matcher contentStartMatcher = contentStartPattern.matcher(charBuffer);
                messageStartMatcher.find();
                if (messageStartMatcher.start() != 0) {
                    throw new BerkeleyMailFileParserException("The parsed file is corrupt!");
                }
                eventHandler.messageStart(messageStartMatcher.start());
                eventHandler.headerStart(messageStartMatcher.end());
                contentStartMatcher.find(messageStartMatcher.end());
                eventHandler.contentStart(contentStartMatcher.end());
                while (messageStartMatcher.find()) {
                    eventHandler.messageEnd(messageStartMatcher.start());
                    eventHandler.messageStart(messageStartMatcher.start());
                    eventHandler.headerStart(messageStartMatcher.end());
                    contentStartMatcher.find(messageStartMatcher.end());
                    eventHandler.contentStart(contentStartMatcher.end());
                }
                eventHandler.messageEnd(fileSize);
            }
            fileChannel.close();
        } catch (IOException IOEx) {
            throw new BerkeleyMailFileParserException(IOEx);
        } catch (SecurityException SecEx) {
            throw new BerkeleyMailFileParserException(SecEx);
        }
    }
