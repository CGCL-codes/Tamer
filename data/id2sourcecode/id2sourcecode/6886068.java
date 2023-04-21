    @SuppressWarnings("unchecked")
    private FileSystemItem[] getChildren(String path, Boolean includeFiles) throws IOException {
        SFTPv3Client channel = getChannel();
        Vector ls = channel.ls(path);
        Vector<FileSystemItem> res = new Vector<FileSystemItem>();
        for (Iterator iterator = ls.iterator(); iterator.hasNext(); ) {
            SFTPv3DirectoryEntry entry = (SFTPv3DirectoryEntry) iterator.next();
            String name = (String) entry.filename;
            String id = (path.equals("/") ? "" : path) + "/" + name;
            if (!name.equals("..") && !name.equals(".") && (includeFiles || entry.attributes.isDirectory())) {
                FileSystemItem item = new FileSystemItem(id, name, entry.attributes.isDirectory());
                res.add(item);
            }
        }
        FileSystemItem[] arr = res.toArray(new FileSystemItem[res.size()]);
        fileMS.sort(arr);
        return arr;
    }
