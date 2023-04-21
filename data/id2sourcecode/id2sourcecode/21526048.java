    private void allocateNewBlock() throws ContentStoreException {
        RandomAccessFile out = null;
        boolean exception_thrown = false;
        try {
            out = new RandomAccessFile(key_file, "rws");
            for (int i = 0; i < MAX_SPINS; i++) {
                FileLock l = out.getChannel().tryLock();
                if (l == null) {
                    try {
                        Thread.sleep(SPIN_TIMEOUT);
                    } catch (InterruptedException e) {
                    }
                    continue;
                } else {
                    try {
                        int old_key;
                        int new_key;
                        String content = null;
                        if (out.length() == 0) {
                            old_key = 0;
                            new_key = old_key + KEY_BLOCK_SIZE;
                        } else {
                            try {
                                content = out.readUTF();
                                old_key = Integer.parseInt(content);
                                new_key = old_key + KEY_BLOCK_SIZE;
                            } catch (NumberFormatException e) {
                                if (content.length() > 100) content = content.substring(0, 100) + "...";
                                throw new ContentStoreException("Content store key file corrupted. Contained: '" + content + "'");
                            }
                        }
                        out.seek(0);
                        out.writeUTF(Integer.toString(new_key));
                        end_of_key_block = new_key;
                        last_key = old_key;
                        return;
                    } finally {
                        try {
                            l.release();
                        } catch (Throwable t) {
                            throw new ContentStoreException("Could not release key file lock.", t);
                        }
                    }
                }
            }
            throw new ContentStoreException("Block allocation timed out.");
        } catch (ContentStoreException e) {
            exception_thrown = true;
            throw e;
        } catch (Throwable t) {
            exception_thrown = true;
            throw new ContentStoreException(t);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    if (!exception_thrown) throw new ContentStoreException("Problems occurred when closing content store.", e);
                }
            }
        }
    }
