        @Override
        public String toString() {
            return String.format("Mp3Stream(maxSize=%d, availableSize=%d, readPos=%d, writePos=%d)", mp3BufSize, mp3InputBufSize, mp3InputFileReadPos, mp3InputBufWritePos);
        }
