    public void ResetDir(File df) {
        if (df.exists() && df.isDirectory()) {
            File[] ls = df.listFiles();
            for (int cnt = 0; cnt < ls.length; cnt++) {
                if (ls[cnt].isDirectory()) {
                    ResetDir(ls[cnt]);
                } else {
                    if (!ls[cnt].delete()) {
                        try {
                            RandomAccessFile raf = new RandomAccessFile(ls[cnt], "rw");
                            raf.getChannel().force(true);
                            raf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!ls[cnt].delete()) {
                            throw new RuntimeException("Failed to delete: " + ls[cnt].getPath());
                        }
                    }
                }
            }
            if (!df.delete()) {
                throw new RuntimeException("Could not delete directory: " + df.getPath());
            }
        }
    }
