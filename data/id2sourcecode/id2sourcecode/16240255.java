    public static void connectStreams(InputStream in, OutputStream out) throws IOException {
        byte readBuffer[] = new byte[BUFFER_SIZE];
        int amount = 0;
        while (amount >= 0) {
            amount = in.read(readBuffer, 0, BUFFER_SIZE);
            if (amount == -1) {
                break;
            }
            out.write(readBuffer, 0, amount);
        }
        in.close();
        out.close();
    }
