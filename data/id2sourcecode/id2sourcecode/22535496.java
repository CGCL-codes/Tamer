    public static boolean replace(String exeFile, String icoFile, CompilerListener l) {
        try {
            File ico = new File(icoFile);
            if (ico.length() != ICO_SIZE) return false;
            File exe = new File(exeFile);
            RandomAccessFile exeRaf = new RandomAccessFile(exe, "rw");
            RandomAccessFile icoRaf = new RandomAccessFile(ico, "r");
            exeRaf.seek(EXE_START);
            icoRaf.seek(ICO_START);
            byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = icoRaf.read(buffer)) != -1) {
                exeRaf.write(buffer, 0, read);
            }
            exeRaf.close();
            icoRaf.close();
        } catch (Exception e) {
            l.addError(e);
            return false;
        }
        return true;
    }
