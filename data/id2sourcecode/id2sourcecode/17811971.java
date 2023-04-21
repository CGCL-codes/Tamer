    public boolean collect() {
        boolean result = false;
        result = this.checkReadiness();
        if (result) {
            long numOfByte = 0;
            FileChannel inCh = myInStream.getChannel();
            int fileSize = 0;
            try {
                fileSize = (int) inCh.size();
            } catch (IOException e) {
                myLogger.log(Level.FINER, "File size exc");
            }
            if (fileSize == 0) return false;
            ByteBuffer buffer = ByteBuffer.allocate(fileSize);
            try {
                numOfByte = inCh.read(buffer);
                buffer.flip();
            } catch (IOException e) {
                myLogger.log(Level.FINER, "Exception when reading from buffer :" + e.getMessage());
            }
            buffer.position(0);
            myLogger.log(Level.FINEST, "size >>" + numOfByte + " >> " + fileSize);
            StringBuffer k;
            try {
                k = new StringBuffer();
            } catch (NegativeArraySizeException e) {
                myLogger.log(Level.FINER, "StringBuffer allocation error. Exception: " + e.getMessage());
                return false;
            }
            char kl;
            boolean put = false;
            String tag = new String("");
            while (buffer.remaining() > 0) {
                kl = (char) buffer.get();
                if (kl == '<') put = true;
                if (put) k.append(kl);
                if (kl == '>') {
                    put = false;
                    raw_tags.add(k.toString());
                    clearBuffer(k);
                }
            }
            if (put) raw_tags.add(k.toString());
            if (!raw_tags.isEmpty()) {
                collect_result();
            }
        } else {
            return result;
        }
        myLogger.log(Level.FINEST, "u list size:" + urls.size() + "; img list size: " + imgs.size());
        return result;
    }
