        final long transferToByteChannel(final WritableByteChannel out, final long maxCount, final boolean forceBlock) throws IOException {
            CheckArg.notNull(out, "out");
            CheckArg.maxCount(maxCount);
            if (!isOpen()) throw new ClosedChannelException();
            if (maxCount == 0) return 0;
            lock();
            try {
                long count = 0;
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel != null) {
                        final long writePos = this.sink.writeCount();
                        if (count != maxCount) {
                            final long readPos = this.readPosition;
                            long step = 0;
                            if (readPosition < writePos) {
                                step = fileChannel.transferTo(readPos, Math.min((maxCount < 0) ? Long.MAX_VALUE : maxCount, writePos - readPos), out);
                            }
                            fileChannel = null;
                            if (step > 0) {
                                count += step;
                                this.readPosition = readPos + step;
                            } else {
                                if (!this.sink.isOpen()) {
                                    closeFile();
                                    break;
                                }
                            }
                        }
                        if (((maxCount < 0) || (count < maxCount)) && waitForMore(forceBlock)) continue; else break;
                    } else if (isOpen()) break; else throw new AsynchronousCloseException();
                }
                return count;
            } catch (final ClosedChannelException ex) {
                final FileChannel fileChannel = this.fileChannel;
                if ((fileChannel == null) || !fileChannel.isOpen()) throw handleClosedFile(ex); else throw ex;
            } finally {
                unlock();
            }
        }
