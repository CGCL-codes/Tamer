        final long transferFromByteChannel(final ReadableByteChannel in, final long maxCount, final boolean forceBlock) throws IOException {
            CheckArg.notNull(in, "in");
            CheckArg.maxCount(maxCount);
            if (!isOpen()) throw new ClosedChannelException();
            if (maxCount == 0) return 0;
            long count = 0;
            lock();
            try {
                while (isOpen()) {
                    FileChannel fileChannel = this.fileChannel;
                    if (fileChannel == null) throw new AsynchronousCloseException();
                    final long maxStep = (maxCount < 0) ? Long.MAX_VALUE : Math.min(Long.MAX_VALUE, maxCount);
                    final long writePos = this.writePos;
                    final long step = fileChannel.transferFrom(in, writePos, maxStep);
                    if (step > 0) {
                        this.writePos = writePos + step;
                        count += step;
                        this.notEmpty.signal();
                    }
                    fileChannel = null;
                    if (((maxCount < 0) || (count < maxCount)) && (forceBlock || this.blocking)) {
                        Thread.yield();
                        continue;
                    } else {
                        break;
                    }
                }
            } catch (final ClosedChannelException ex) {
                throw handleClosedFile(ex);
            } finally {
                unlock();
            }
            return count;
        }
