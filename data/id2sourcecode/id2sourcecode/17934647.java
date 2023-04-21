    public void convert(String startWith, BundleReader reader, BundleWriter writer) {
        if (dir == null) {
            showArgumentsHelp(System.err);
            return;
        }
        if (project != null) {
            project = new File(userDir).getParent() + fileSep + project;
            dir = project + fileSep + "src" + fileSep + dir;
        }
        if (!new File(dir).isAbsolute()) dir = userDir + fileSep + "src" + fileSep + dir;
        if (!new File(dir).exists()) throw new IllegalStateException(dir + " does not exist");
        if (dest != null && !new File(dest).isAbsolute()) {
            dest = project == null ? userDir + fileSep + dest : project + fileSep + dest;
            File d = new File(dest);
            if (!d.exists()) d.mkdir();
        }
        Iterator files = getFiles(dir).iterator();
        int i = 0, j = 0;
        while (files.hasNext()) {
            try {
                file2file(files.next(), startWith, reader, writer);
                j++;
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            i++;
        }
        System.out.println(i + " classes found, " + j + " of them converted to properties");
    }
