    public static void readFile(DataInputStream dis, File f, boolean append) throws IOException {
        int version = dis.read();
        if (version == CURRENT_FILE_VERSION) {
            long filelen = dis.readLong();
            if (filelen > MAXFILE) {
                throw new IOException("File size too large! " + filelen);
            }
            FileOutputStream fos = new FileOutputStream(f, append);
            long runningtotal = 0L;
            byte[] buffer = new byte[BUFFERLEN];
            int numread = 0;
            while (runningtotal < filelen && numread != -1) {
                long numtoread = Math.min((long) buffer.length, filelen - runningtotal);
                int inumtoread = (int) numtoread;
                numread = dis.read(buffer, 0, inumtoread);
                if (numread > 0) {
                    fos.write(buffer, 0, numread);
                    runningtotal += numread;
                }
            }
            if (runningtotal != filelen) {
                throw new IOException("Not enough data read!");
            }
            fos.close();
        } else {
            throw new IOException("Invalid version! " + version);
        }
    }
