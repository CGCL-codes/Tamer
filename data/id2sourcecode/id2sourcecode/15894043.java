    public void entry(String path) {
        if ('/' != path.charAt(path.length() - 1)) {
            path += "/";
        }
        LinkedList<JarEntry> list = new LinkedList<JarEntry>();
        for (int i = path.lastIndexOf('/'); 0 < i; i = path.lastIndexOf('/', i - 1)) {
            path = path.substring(0, i + 1);
            if (this.existsDirectories.add(path)) {
                JarEntry entry = new JarEntry(path);
                entry.setMethod(ZipEntry.STORED);
                entry.setSize(0);
                entry.setCrc(0);
                list.addLast(entry);
            } else {
                break;
            }
        }
        try {
            for (JarEntry je : list) {
                this.stream.putNextEntry(je);
            }
        } catch (Exception e) {
            handle(e);
        }
    }
