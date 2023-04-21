        public MultiMMapIndexInput(RandomAccessFile raf, int maxBufSize) throws IOException {
            this.length = raf.length();
            this.maxBufSize = maxBufSize;
            if (maxBufSize <= 0) throw new IllegalArgumentException("Non positive maxBufSize: " + maxBufSize);
            if ((length / maxBufSize) > Integer.MAX_VALUE) throw new IllegalArgumentException("RandomAccessFile too big for maximum buffer size: " + raf.toString());
            int nrBuffers = (int) (length / maxBufSize);
            if ((nrBuffers * maxBufSize) < length) nrBuffers++;
            this.buffers = new ByteBuffer[nrBuffers];
            this.bufSizes = new int[nrBuffers];
            long bufferStart = 0;
            FileChannel rafc = raf.getChannel();
            for (int bufNr = 0; bufNr < nrBuffers; bufNr++) {
                int bufSize = (length > (bufferStart + maxBufSize)) ? maxBufSize : (int) (length - bufferStart);
                this.buffers[bufNr] = rafc.map(MapMode.READ_ONLY, bufferStart, bufSize);
                this.bufSizes[bufNr] = bufSize;
                bufferStart += bufSize;
            }
            seek(0L);
        }
