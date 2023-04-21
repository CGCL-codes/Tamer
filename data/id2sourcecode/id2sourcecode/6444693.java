    public static boolean writeFile(String filePath, String content, boolean append) {
        try {
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file, append);
            OutputStreamWriter fwout = new OutputStreamWriter(out, JavaCenterHome.JCH_CHARSET);
            BufferedWriter bw = new BufferedWriter(fwout);
            FileLock fl = out.getChannel().tryLock();
            if (fl.isValid()) {
                bw.write(content);
                fl.release();
            }
            bw.flush();
            fwout.flush();
            out.flush();
            bw.close();
            fwout.close();
            out.close();
            bw = null;
            fwout = null;
            out = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
