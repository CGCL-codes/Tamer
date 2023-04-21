    public void add(DropBoxManager.Entry entry) {
        File temp = null;
        OutputStream output = null;
        final String tag = entry.getTag();
        try {
            int flags = entry.getFlags();
            if ((flags & DropBoxManager.IS_EMPTY) != 0) throw new IllegalArgumentException();
            init();
            if (!isTagEnabled(tag)) return;
            long max = trimToFit();
            long lastTrim = System.currentTimeMillis();
            byte[] buffer = new byte[mBlockSize];
            InputStream input = entry.getInputStream();
            int read = 0;
            while (read < buffer.length) {
                int n = input.read(buffer, read, buffer.length - read);
                if (n <= 0) break;
                read += n;
            }
            temp = new File(mDropBoxDir, "drop" + Thread.currentThread().getId() + ".tmp");
            output = new FileOutputStream(temp);
            if (read == buffer.length && ((flags & DropBoxManager.IS_GZIPPED) == 0)) {
                output = new GZIPOutputStream(output);
                flags = flags | DropBoxManager.IS_GZIPPED;
            }
            do {
                output.write(buffer, 0, read);
                long now = System.currentTimeMillis();
                if (now - lastTrim > 30 * 1000) {
                    max = trimToFit();
                    lastTrim = now;
                }
                read = input.read(buffer);
                if (read <= 0) {
                    output.close();
                    output = null;
                } else {
                    output.flush();
                }
                long len = temp.length();
                if (len > max) {
                    Slog.w(TAG, "Dropping: " + tag + " (" + temp.length() + " > " + max + " bytes)");
                    temp.delete();
                    temp = null;
                    break;
                }
            } while (read > 0);
            createEntry(temp, tag, flags);
            temp = null;
        } catch (IOException e) {
            Slog.e(TAG, "Can't write: " + tag, e);
        } finally {
            try {
                if (output != null) output.close();
            } catch (IOException e) {
            }
            entry.close();
            if (temp != null) temp.delete();
        }
    }
