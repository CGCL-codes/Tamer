    private void sendDeltas() throws IOException {
        int len = fin.read(buf);
        if (len == -1) {
            if (count > 0) {
                try {
                    matcher.doFinal();
                } catch (ListenerException le) {
                }
            }
            if (options.verbose > 2) logger.info("sending file_sum");
            file.sum = file_sum.digest();
            outBuffer.putInt(0);
            outBuffer.put(file.sum);
            outBuffer.flush();
            fin.close();
            state = SENDER_RECEIVE_INDEX;
            return;
        }
        stats.total_size += len;
        file_sum.update(buf, 0, len);
        if (count > 0) {
            try {
                matcher.update(buf, 0, len);
            } catch (ListenerException le) {
            }
        } else {
            outBuffer.putInt(len);
            outBuffer.put(buf, 0, len);
        }
    }
