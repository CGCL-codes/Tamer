    public static void copyFile(File source, File target) throws IOException {
        RandomAccessFile input = new RandomAccessFile(source, "r");
        RandomAccessFile output = new RandomAccessFile(target, "rw");
        try {
            byte[] buf = new byte[65536];
            long len = input.length();
            output.setLength(len);
            int bytesRead;
            while ((bytesRead = input.read(buf, 0, buf.length)) > 0) output.write(buf, 0, bytesRead);
        } catch (IOException e) {
            try {
                input.close();
            } catch (Exception e1) {
            }
            ;
            try {
                output.close();
            } catch (Exception e1) {
            }
            ;
            return;
        }
        try {
            input.close();
        } catch (Exception e) {
        }
        ;
        output.close();
    }
