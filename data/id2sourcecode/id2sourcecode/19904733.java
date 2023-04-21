    public static void createZip(String zipName, InputStream cic) {
        Hashtable fileList = createFileList(false);
        File zipFile = new File(zipName);
        if (zipFile.exists()) {
            Object[] options = { "Overwrite", "Cancel" };
            int answer = JOptionPane.showOptionDialog(null, "a File named " + zipName + " already exists, overwrite it ?", "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (answer != 0) return;
        }
        try {
            if (zipFile.exists()) zipFile.delete();
            RandomAccessFile rf = new RandomAccessFile(zipName, "rw");
            SeekableOutputStream os = new SeekableOutputStream(rf);
            int prio = Thread.currentThread().getPriority();
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            writeZip(os, fileList, null, null, false);
            Thread.currentThread().setPriority(prio);
            os.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "can not open file " + zipName + " : " + e, "failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
