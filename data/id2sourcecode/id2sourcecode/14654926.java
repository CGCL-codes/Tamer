            public void run() {
                RandomAccessFile raf = null;
                try {
                    raf = reserveRAF();
                    long pos = position;
                    long rem = length;
                    while (!destroyed && rem > 0) {
                        int limit;
                        if (paused) {
                            Thread.sleep(250);
                            limit = 1;
                        } else {
                            if (max_bytes_per_sec > 0) {
                                limit = bytes_available;
                                if (limit <= 0) {
                                    Thread.sleep(25);
                                    continue;
                                }
                                limit = Math.min(BUFFER_SIZE, limit);
                            } else {
                                limit = BUFFER_SIZE;
                            }
                            limit = (int) Math.min(rem, limit);
                        }
                        int read_length = 0;
                        int buffer_start = 0;
                        byte[] buffer = null;
                        synchronized (TranscodePipe.this) {
                            int c_num = 0;
                            Iterator<bufferCache> it = buffer_cache.iterator();
                            while (it.hasNext()) {
                                bufferCache b = it.next();
                                long rel_offset = pos - b.offset;
                                if (rel_offset >= 0) {
                                    byte[] data = b.data;
                                    long avail = data.length - rel_offset;
                                    if (avail > 0) {
                                        read_length = (int) Math.min(avail, limit);
                                        buffer = data;
                                        buffer_start = (int) rel_offset;
                                        if (c_num > 0) {
                                            it.remove();
                                            buffer_cache.addFirst(b);
                                        }
                                        break;
                                    }
                                }
                                c_num++;
                            }
                            if (buffer == null) {
                                buffer = new byte[limit];
                                raf.seek(pos);
                                read_length = raf.read(buffer);
                                if (read_length != limit) {
                                    Debug.out("eh?");
                                    throw (new IOException("Inconsistent"));
                                }
                                bufferCache b = new bufferCache(pos, buffer);
                                buffer_cache.addFirst(b);
                                buffer_cache_size += limit;
                                while (buffer_cache_size > BUFFER_CACHE_SIZE) {
                                    b = buffer_cache.removeLast();
                                    buffer_cache_size -= b.data.length;
                                }
                            }
                        }
                        if (read_length <= 0) {
                            break;
                        }
                        rem -= read_length;
                        pos += read_length;
                        if (max_bytes_per_sec > 0) {
                            bytes_available -= read_length;
                        }
                        os.write(buffer, buffer_start, read_length);
                        write_speed.addValue(read_length);
                    }
                    os.flush();
                } catch (Throwable e) {
                    if (raf != null) {
                        try {
                            synchronized (TranscodePipe.this) {
                                raf.seek(0);
                                raf.read(new byte[1]);
                            }
                        } catch (Throwable f) {
                            reportError(e);
                        }
                    }
                } finally {
                    try {
                        os.close();
                    } catch (Throwable e) {
                    }
                    if (raf != null) {
                        releaseRAF(raf);
                    }
                }
            }
