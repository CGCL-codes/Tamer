    public void run() {
        Segment[] segs = download.getSegments();
        while (segs[segs.length - 1] == null) {
            Thread.yield();
        }
        try {
            long downloaded = tempFile.length();
            RandomAccessFile rw = new RandomAccessFile(tempFile, "rw");
            InputStream in = urlToInputStream(url);
            long limit = offset;
            if (index + 1 < segs.length) {
                limit = segs[index + 1].getOffset();
            } else {
                limit = download.getTotalSize();
            }
            rw.seek(downloaded);
            download.incrementSize(downloaded);
            long pos = offset + downloaded;
            long skipped = 0;
            while (skipped < pos) {
                skipped += in.skip(pos - skipped);
            }
            long time = System.currentTimeMillis();
            long oldPos = pos;
            final int CHUNK_SIZE = 16384;
            while (pos < limit) {
                byte buffer[];
                if (limit - pos > CHUNK_SIZE) {
                    buffer = new byte[CHUNK_SIZE];
                } else {
                    buffer = new byte[(int) (limit - pos)];
                }
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                rw.write(buffer, 0, read);
                pos += read;
                download.incrementSize(read);
                if (System.currentTimeMillis() - time >= 1000) {
                    speed = pos - oldPos;
                    oldPos = pos;
                    time = System.currentTimeMillis();
                }
                while (paused) {
                    Thread.yield();
                }
            }
            rw.close();
            in.close();
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
            finished = true;
            Mediator.post(new SegmentFinished(this));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
