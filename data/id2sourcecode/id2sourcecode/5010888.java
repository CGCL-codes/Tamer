    private synchronized byte[] readBlock(final int index) {
        byte[] retval = getBlock(index, false);
        if (retval == null) {
            int retry = 0;
            int start = index * BLOCKSIZE;
            int end = (index + 1) * BLOCKSIZE;
            InputStream input = this.input;
            for (; (input != null) && (buffer.size() < index); input = this.input) {
                if (getBlock(buffer.size(), true) == null) {
                    return null;
                }
            }
            while ((start < endOffset) && (start < end)) {
                if (input == null) {
                    if (rangeAccepted && (url != null)) {
                        try {
                            URLConnection connection = url.openConnection();
                            if (connection instanceof HttpURLConnection) {
                                connection.setRequestProperty("Range", "bytes=" + start + "-" + (end - 1));
                                connection.connect();
                                final int response = ((HttpURLConnection) connection).getResponseCode();
                                if (response == 206) {
                                    input = connection.getInputStream();
                                } else if ((response / 100 == 2) && (start == 0)) {
                                    this.input = input = connection.getInputStream();
                                    rangeAccepted = false;
                                } else if (end < currentSize) {
                                    connection = null;
                                    System.gc();
                                    try {
                                        Thread.sleep(200L);
                                    } catch (final InterruptedException ignored) {
                                    }
                                    continue;
                                } else {
                                    DjVuOptions.out.println("Server response " + response + " requested " + start + "," + end);
                                }
                            } else if ((start == 0) && (connection != null)) {
                                this.input = input = connection.getInputStream();
                                rangeAccepted = false;
                            }
                        } catch (final IOException exp) {
                            printStackTrace(exp);
                            if (input != null) {
                                try {
                                    input.close();
                                } catch (final Throwable ignored) {
                                }
                                input = null;
                            }
                            System.gc();
                            try {
                                Thread.sleep(200L);
                            } catch (final Throwable ignored) {
                            }
                            if (rangeAccepted && (++retry < 10)) {
                                continue;
                            }
                        }
                    }
                    if (input == null) {
                        end = start;
                        setEndOffset(end);
                        break;
                    }
                }
                if (retval == null) {
                    retval = new byte[BLOCKSIZE];
                }
                for (int size = end - start; size > 0; size = start - end) {
                    int offset = start % BLOCKSIZE;
                    int len = 0;
                    try {
                        len = input.read(retval, offset, size);
                    } catch (final Throwable exp) {
                        printStackTrace(exp);
                        if (rangeAccepted && (++retry < 10)) {
                            try {
                                input.close();
                            } catch (final Throwable ignored) {
                            }
                            input = null;
                            continue;
                        }
                        len = 0;
                    }
                    retry = 0;
                    if (len <= 0) {
                        try {
                            input.close();
                        } catch (final IOException ignored) {
                        }
                        input = null;
                        this.input = null;
                        end = start;
                        setEndOffset(end);
                        if (offset > 0) {
                            byte[] xretval = new byte[offset];
                            System.arraycopy(retval, 0, xretval, 0, offset);
                            retval = xretval;
                        } else {
                            retval = null;
                        }
                        break;
                    }
                    start += len;
                }
            }
            if (retval != null) {
                if (buffer.size() <= index) {
                    buffer.setSize(index + 1);
                }
                if (rangeAccepted && (index > 0)) {
                    buffer.setElementAt(createSoftReference(retval, retval), index);
                    cacheCreatedArray[cacheCreatedIndex++ % cacheCreatedArray.length] = retval;
                } else {
                    buffer.setElementAt(retval, index);
                }
                if (end > currentSize) {
                    currentSize = end;
                }
            }
        }
        return retval;
    }
