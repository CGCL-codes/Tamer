    @SuppressWarnings("unchecked")
    public List<String> getFileList() throws SftpException, IOException {
        List<String> files = new ArrayList<String>();
        Vector<LsEntry> entries = getChannel().ls(url.getPath());
        for (LsEntry entry : entries) {
            if (entry.getFilename().startsWith(".")) continue;
            files.add(entry.getFilename());
        }
        Collections.sort(files);
        return files;
    }
