    private void flushBuffer() {
        int newlineAdjust = overflowLastChar == NEW_LINE_CHAR ? 0 : -1;
        int totalMessageSize = overflow ? (MESSAGE_BUFFER_SIZE + OVERFLOW_SIZE + newlineAdjust) : bufferIndex;
        if (threadIdFlag) {
            VM.strings.writeThreadId(buffer, totalMessageSize);
        } else {
            VM.strings.write(buffer, totalMessageSize);
        }
        threadIdFlag = false;
        overflow = false;
        overflowLastChar = '\0';
        bufferIndex = 0;
    }
