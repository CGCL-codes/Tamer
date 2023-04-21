    public static int openWrite(String fname) {
        File file = new File(fname);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel();
            count++;
            content.set(count, fos);
            return count;
        } catch (IOException x) {
        }
        return -1;
    }
