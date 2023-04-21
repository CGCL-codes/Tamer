    public static int openRead(String fname) {
        File file = new File(fname);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                fis.getChannel();
                count++;
                content.set(count, fis);
                return count;
            } catch (IOException x) {
            }
        }
        return -1;
    }
