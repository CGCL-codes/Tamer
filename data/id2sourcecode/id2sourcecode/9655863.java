        @Override
        public void run() {
            byte[] buff = new byte[8192];
            while (true) {
                try {
                    int avail = is.read(buff);
                    synchronized (synchronizer) {
                        if (avail <= 0) {
                            isEOF = true;
                            synchronizer.notifyAll();
                            break;
                        }
                        int space_available = buffer.length - write_pos;
                        if (space_available < avail) {
                            int unread_size = write_pos - read_pos;
                            int need_space = unread_size + avail;
                            byte[] new_buffer = buffer;
                            if (need_space > buffer.length) {
                                int inc = need_space / 3;
                                inc = (inc < 256) ? 256 : inc;
                                inc = (inc > 8192) ? 8192 : inc;
                                new_buffer = new byte[need_space + inc];
                            }
                            if (unread_size > 0) System.arraycopy(buffer, read_pos, new_buffer, 0, unread_size);
                            buffer = new_buffer;
                            read_pos = 0;
                            write_pos = unread_size;
                        }
                        System.arraycopy(buff, 0, buffer, write_pos, avail);
                        write_pos += avail;
                        synchronizer.notifyAll();
                    }
                } catch (IOException e) {
                    synchronized (synchronizer) {
                        exception = e;
                        synchronizer.notifyAll();
                        break;
                    }
                }
            }
        }
