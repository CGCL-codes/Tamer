    ScaledRAFileNIO(Database database, String name, boolean readOnly, int bufferLength) throws Throwable {
        long fileLength;
        if (bufferLength < 1 << 18) {
            bufferLength = 1 << 18;
        }
        try {
            file = new RandomAccessFile(name, readOnly ? "r" : "rw");
        } catch (Throwable e) {
            throw e;
        }
        try {
            fileLength = file.length();
        } catch (Throwable e) {
            file.close();
            throw e;
        }
        if (fileLength > ScaledRAFile.MAX_NIO_LENGTH) {
            file.close();
            throw new IOException("length exceeds nio limit");
        }
        if (bufferLength < fileLength) {
            bufferLength = (int) fileLength;
        }
        bufferLength = newNIOBufferSize(bufferLength);
        if (readOnly) {
            bufferLength = (int) fileLength;
        }
        if (fileLength < bufferLength) {
            try {
                file.seek(bufferLength - 1);
                file.writeByte(0);
                file.getFD().sync();
                file.close();
                file = new RandomAccessFile(name, readOnly ? "r" : "rw");
            } catch (Throwable e) {
                file.close();
                throw e;
            }
        }
        this.appLog = database.logger.appLog;
        this.readOnly = readOnly;
        this.bufferLength = bufferLength;
        this.channel = file.getChannel();
        try {
            buffer = channel.map(readOnly ? FileChannel.MapMode.READ_ONLY : FileChannel.MapMode.READ_WRITE, 0, bufferLength);
            Trace.printSystemOut("NIO file instance created. mode: " + readOnly);
            if (!readOnly) {
                long tempSize = bufferLength - fileLength;
                if (tempSize > 1 << 18) {
                    tempSize = 1 << 18;
                }
                byte[] temp = new byte[(int) tempSize];
                try {
                    long pos = fileLength;
                    for (; pos < bufferLength - tempSize; pos += tempSize) {
                        buffer.position((int) pos);
                        buffer.put(temp, 0, temp.length);
                    }
                    buffer.position((int) pos);
                    buffer.put(temp, 0, (int) (bufferLength - pos));
                    buffer.force();
                } catch (Throwable t) {
                    appLog.logContext(t, JVM_ERROR + " " + "length: " + bufferLength);
                }
                buffer.position(0);
            }
        } catch (Throwable e) {
            Trace.printSystemOut("NIO constructor failed:  " + bufferLength);
            buffer = null;
            channel = null;
            file.close();
            System.gc();
            throw e;
        }
    }
