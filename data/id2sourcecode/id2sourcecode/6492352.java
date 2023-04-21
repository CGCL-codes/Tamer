    public boolean read(OutputStream destination, long index, int filenumber) throws IOException {
        if (index < 1) throw new IndexOutOfBoundsException();
        if (filenumber < 0 || filenumber > 99999999) throw new IndexOutOfBoundsException();
        if (filenumber != lastFile || index <= lastIndex) open(filenumber);
        lastIndex = index;
        while (true) {
            readManagementData();
            if (c_index == index) break;
            long remaining = c_size;
            if (remaining < 0) throw new IOException();
            while (remaining > 0) {
                int read = (int) Math.min(BUFLEN, remaining);
                read = input.read(bytes, 0, read);
                if (read < 0) return false;
                remaining -= read;
            }
        }
        long length = c_size;
        while (length > 0) {
            int read = (int) Math.min(BUFLEN, length);
            read = input.read(bytes, 0, read);
            if (read == -1) {
                input.close();
                open(++filenumber);
                readManagementData();
                if (c_index != 0) throw new IOException();
                if (c_size != length) throw new IOException();
                read = 0;
            }
            destination.write(bytes, 0, read);
            length -= read;
        }
        return true;
    }
