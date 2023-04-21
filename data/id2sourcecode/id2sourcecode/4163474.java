        protected void writeTail(Request request) throws IOException {
            if (this.isOk()) {
                long length = this.length;
                if (0 < length) {
                    java.io.File file = this.file;
                    try {
                        FileChannel fc = new FileOutputStream(file).getChannel();
                        try {
                            ReadableByteChannel in = request.getChannel();
                            fc.transferFrom(in, 0L, length);
                        } finally {
                            fc.close();
                        }
                    } catch (IOException exc) {
                        this.setStatusNotFound();
                    }
                } else {
                    java.io.File file = this.file;
                    try {
                        FileChannel fc = new FileOutputStream(file).getChannel();
                        try {
                            ReadableByteChannel in = request.getChannel();
                            long pos = 0L, read;
                            while (0L < (read = fc.transferFrom(in, pos, 1024L))) {
                                pos += read;
                            }
                        } finally {
                            fc.close();
                        }
                    } catch (IOException exc) {
                        this.setStatusNotFound();
                    }
                }
            }
        }
