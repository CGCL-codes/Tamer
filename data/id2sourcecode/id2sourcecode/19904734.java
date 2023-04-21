    public static void createClass(String className, String outdir, String newClassName, boolean notAll) {
        if (!outdir.endsWith(File.separator)) outdir = outdir + File.separator;
        File cf = new File(className);
        String outname = null;
        InputStream cis = null;
        Hashtable fileList = createFileList(true);
        if (cf.exists() && cf.canRead()) {
            if (newClassName == null) outname = outdir + cf.getName(); else outname = outdir + newClassName + ".class";
            try {
                cis = new FileInputStream(className);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "can not open file " + className + " : " + e, "failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File zipFile = new File(outname);
            if (zipFile.exists()) {
                Object[] options = { "Overwrite", "Cancel" };
                int answer = JOptionPane.showOptionDialog(null, "a File named " + outname + " already exists, overwrite it ?", "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (answer != 0) return;
            }
            try {
                if (zipFile.exists()) zipFile.delete();
                RandomAccessFile rf = new RandomAccessFile(outname, "rw");
                SeekableOutputStream os = new SeekableOutputStream(rf);
                int prio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                writeZip(os, fileList, cis, newClassName, notAll);
                Thread.currentThread().setPriority(prio);
                os.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "can not create file " + outname + " : " + e, "failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "can not open file " + className, "failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
