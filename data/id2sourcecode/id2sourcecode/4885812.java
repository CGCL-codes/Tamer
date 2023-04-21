        public byte[] readBytes(int length) throws IOException {
            if (sock == null || !sock.isConnected()) {
                log.error("++++ attempting to read from closed socket");
                throw new IOException("++++ attempting to read from closed socket");
            }
            byte[] result = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (recIndex >= length) {
                bos.write(recBuf, 0, length);
                byte[] newBuf = new byte[recBufSize];
                if (recIndex > length) System.arraycopy(recBuf, length, newBuf, 0, recIndex - length);
                recBuf = newBuf;
                recIndex = recIndex - length;
            } else {
                int totalread = length;
                if (recIndex > 0) {
                    totalread = totalread - recIndex;
                    bos.write(recBuf, 0, recIndex);
                    recBuf = new byte[recBufSize];
                    recIndex = 0;
                }
                int readCount = 0;
                while (totalread > 0) {
                    if ((readCount = in.read(recBuf)) > 0) {
                        if (totalread > readCount) {
                            bos.write(recBuf, 0, readCount);
                            recBuf = new byte[recBufSize];
                            recIndex = 0;
                        } else {
                            bos.write(recBuf, 0, totalread);
                            byte[] newBuf = new byte[recBufSize];
                            System.arraycopy(recBuf, totalread, newBuf, 0, readCount - totalread);
                            recBuf = newBuf;
                            recIndex = readCount - totalread;
                        }
                        totalread = totalread - readCount;
                    }
                }
            }
            result = bos.toByteArray();
            if (result == null || (result != null && result.length <= 0 && recIndex <= 0)) {
                throw new IOException("++++ Stream appears to be dead, so closing it down");
            }
            aliveTimeStamp = System.currentTimeMillis();
            return result;
        }
