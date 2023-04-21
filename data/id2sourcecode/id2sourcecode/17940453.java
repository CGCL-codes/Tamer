    public static void dirToZip(File dir, File zipTo) throws FileNotFoundException, IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipTo));
        Vector files = new Vector();
        listRec(dir, files);
        String basename = dir.getCanonicalPath();
        for (int i = 0; i < files.size(); i++) {
            File file = (File) files.get(i);
            String fullName = file.getCanonicalPath();
            String relName = fullName.substring(basename.length() + 1, fullName.length());
            if (File.separator.equals("\\")) {
                relName = StringUtil.switchAllChars(relName, '\\', '/');
            }
            ZipEntry entry = new ZipEntry(relName);
            out.putNextEntry(entry);
            if (!file.isDirectory()) {
                FileInputStream ins = new FileInputStream(file);
                pipeStreams(out, ins);
                ins.close();
            }
            out.closeEntry();
        }
        out.close();
    }
