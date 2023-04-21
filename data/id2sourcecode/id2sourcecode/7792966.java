        private int read0(final ByteBuffer dst, final boolean forceBlock) throws IOException {
            final int initialDstPos = dst.position();
            final int dstLim = dst.limit();
            int dstPos = dst.position();
            long readPos = this.readPosition;
            try {
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel != null) {
                        final long writePos = this.sink.writeCount();
                        final int maxStep = (int) Math.min(dstLim - dstPos, writePos - readPos);
                        final int step;
                        if (maxStep > 0) {
                            dst.limit(dstPos + maxStep);
                            step = fileChannel.read(dst, readPos);
                            fileChannel = null;
                            if (step > 0) {
                                this.readPosition = readPos += step;
                                dstPos += step;
                            } else if (step < 0) {
                                throw new IOException("File truncated by another thread or process: " + this.file);
                            }
                        } else {
                            fileChannel = null;
                            step = 0;
                        }
                        if ((readPos >= writePos) && !this.sink.isOpen()) {
                            closeFile();
                            break;
                        } else if ((step < dstLim - dstPos) && waitForMore(forceBlock)) {
                            continue;
                        } else {
                            break;
                        }
                    } else if (isOpen()) break; else throw new AsynchronousCloseException();
                }
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            } finally {
                dst.limit(dstLim);
            }
            return dstPos - initialDstPos;
        }
