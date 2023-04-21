    private static boolean contentEquals(FileNode nodeLeft, FileNode nodeRight, Ignore ignore) {
        File fileLeft;
        File fileRight;
        RandomAccessFile fLeft;
        RandomAccessFile fRight;
        FileChannel fcLeft;
        FileChannel fcRight;
        ByteBuffer bbLeft;
        ByteBuffer bbRight;
        boolean equals;
        fileLeft = nodeLeft.getFile();
        fileRight = nodeRight.getFile();
        fLeft = null;
        fRight = null;
        try {
            if (fileLeft.isDirectory() || fileRight.isDirectory()) {
                return true;
            }
            if (!ignore.ignore && fileLeft.length() != fileRight.length()) {
                return false;
            }
            if (!ignore.ignore || fileLeft.length() == fileRight.length()) {
                fLeft = new RandomAccessFile(fileLeft, "r");
                fRight = new RandomAccessFile(fileRight, "r");
                fcLeft = fLeft.getChannel();
                fcRight = fRight.getChannel();
                bbLeft = fcLeft.map(FileChannel.MapMode.READ_ONLY, 0, (int) fcLeft.size());
                bbRight = fcRight.map(FileChannel.MapMode.READ_ONLY, 0, (int) fcRight.size());
                equals = bbLeft.equals(bbRight);
                if (!ignore.ignore || equals) {
                    return equals;
                }
            }
            equals = contentEquals(nodeLeft.getDocument().getReader(), nodeRight.getDocument().getReader(), ignore);
            return equals;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (fLeft != null) {
                    fLeft.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (fRight != null) {
                    fRight.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
