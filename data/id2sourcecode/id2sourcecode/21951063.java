    private static void _zipFiles(ZipOutputStream out, Directory src, String path, ProgressObserver observer) throws IOException {
        File[] list = src.getFiles();
        String tmpPath = path.equals("") ? "" : path + "/";
        for (int i = 0; i < list.length; i++) {
            String filename = tmpPath + list[i].getName();
            if (filename.endsWith(".DS_Store")) {
                continue;
            }
            ZipEntry entry = new ZipEntry(Tools.toSafeString(filename));
            entry.setTime(list[i].getLastModified().getTimeInMilliseconds());
            entry.setSize(list[i].getSize());
            out.putNextEntry(entry);
            Streams.copy(list[i].getInputStream(), out, observer);
            out.closeEntry();
        }
        out.flush();
        Directory[] dirs = src.getDirectories();
        for (int i = 0; i < dirs.length; i++) {
            _zipFiles(out, dirs[i], path + "/" + dirs[i].getName(), observer);
        }
    }
