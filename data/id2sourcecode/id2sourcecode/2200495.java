    public static void doPNG(InputStream in, OutputStream out) throws IOException, RRDException {
        byte[] input = new byte[1024];
        int read;
        int size;
        int to_read;
        boolean end = false;
        while (!end) {
            read = blockingRead(in, input, 8);
            if (read != 8) {
                throw new RRDException("Could not read enough characters " + "for a PNG chunk header.  Wanted 8, " + "got " + read);
            }
            if (out != null) {
                out.write(input, 0, read);
            }
            size = (byte2int(input[0]) << 24) + (byte2int(input[1]) << 16) + (byte2int(input[2]) << 8) + byte2int(input[3]);
            if (input[4] == 'I' && input[5] == 'E' && input[6] == 'N' && input[7] == 'D') {
                end = true;
            }
            size += 4;
            while (size > 0) {
                to_read = (size > input.length) ? input.length : size;
                read = blockingRead(in, input, to_read);
                if (read != to_read) {
                    throw new RRDException("Could not read enough " + "characters for a PNG data " + "chunk.  Wanted " + to_read + ", got " + read);
                }
                if (out != null) {
                    out.write(input, 0, read);
                }
                size -= to_read;
            }
        }
        return;
    }
