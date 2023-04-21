    public static boolean delete(File file) throws IOException {
        boolean rtn = false;
        try {
            rtn = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file.exists()) {
                throw new IOException(LINE_SEP + "    A file '" + file.getPath() + "' can not deleted." + LINE_SEP + "    If you use :" + LINE_SEP + "       'a) FileChUtil.USE_MAPMODE_READ_ONLY'" + LINE_SEP + "       'b) FileChUtil.USE_MAPMODE_READ_WRITE'" + LINE_SEP + "    for read/write, then you have to use :" + LINE_SEP + "        1) set NULL to buffer" + LINE_SEP + "        2) use 'FileChUtil.deleteAfterGC()' for file deleting." + LINE_SEP + "    OR use :" + LINE_SEP + "        FileChUtil.USE_DIRECT_ALLOCATED_BUFFER (USE_ALLOCATED_BUFFER)" + LINE_SEP + "    for read/write." + LINE_SEP + "    and check target channel closed." + LINE_SEP + LINE_SEP + "    If your development/deployment platform is Win32," + LINE_SEP + "    see also bug information:" + LINE_SEP + "    http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4715154" + LINE_SEP + "-----------------------------------------------------------------------");
            }
        }
        return rtn;
    }
