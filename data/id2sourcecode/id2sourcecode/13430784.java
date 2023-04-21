    public File getFile(String url, String filepath, long revision, String username, String password) {
        setupLibrary();
        SVNRepository repository = null;
        File outfile = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);
            SVNNodeKind nodeKind = repository.checkPath(filepath, revision);
            if (nodeKind == SVNNodeKind.NONE || nodeKind == SVNNodeKind.DIR) {
                return null;
            }
            Map fileproperties = new HashMap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            repository.getFile(filepath, revision, fileproperties, baos);
            String mimeType = (String) fileproperties.get(SVNProperty.MIME_TYPE);
            boolean isTextType = SVNProperty.isTextMimeType(mimeType);
            if (isTextType) {
                int index = filepath.lastIndexOf(".");
                index = index < 0 ? 0 : index;
                if (index == 0) {
                    int slash_index = filepath.lastIndexOf("/");
                    if (slash_index > 0 && slash_index < filepath.length()) {
                        index = slash_index + 1;
                    }
                }
                String filename = filepath.substring(0, index) + "-" + (revision < 0L ? "HEAD" : String.valueOf(revision)) + filepath.substring(index);
                filename = System.getProperty("java.io.tmpdir") + "/" + filename;
                outfile = new File(filename);
                if (outfile.exists()) {
                    outfile.delete();
                }
                outfile.deleteOnExit();
                outfile.getParentFile().mkdirs();
                StringReader reader = new StringReader(baos.toString());
                BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
                FileUtilities.copy(reader, writer);
                writer.close();
                return outfile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            outfile = null;
        }
        return outfile;
    }
