    public CommitData doImport(CopyData cd) throws CommandInitializationException, SVNException {
        SVNKit.setupLibrary();
        File from = cd.getSourceFile();
        if (from == null || !from.exists()) {
            throw new CommandInitializationException("Source file/directory is invalid.");
        }
        if (from.isDirectory() && from.list().length == 0) {
            throw new CommandInitializationException("Source directory is empty, nothing to import.");
        }
        SVNURL to = cd.getDestinationURL();
        if (to == null) {
            throw new CommandInitializationException("Repository destination is invalid.");
        }
        if (cd.getOut() == null) {
            throw new CommandInitializationException("Invalid output stream.");
        }
        if (cd.getErr() == null) {
            cd.setErr(cd.getOut());
        }
        if (cd.getMessage() == null) {
            cd.setMessage("no message");
        }
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager clientManager = SVNClientManager.newInstance(options, SVNWCUtil.createDefaultAuthenticationManager(SVNPlugin.getSvnStorageDir(), cd.getUsername(), cd.getDecryptedPassword()));
        SVNCommitClient client = clientManager.getCommitClient();
        final List<String> result_paths = new ArrayList<String>();
        client.setEventHandler(new SVNCommandEventProcessor(cd.getOut(), cd.getErr(), false) {

            @Override
            public void handleEvent(SVNEvent event, double progress) {
                super.handleEvent(event, progress);
                if (event.getFile() != null) {
                    result_paths.add(event.getFile().toString());
                }
            }
        });
        SVNCommitInfo info = client.doImport(from, to, cd.getMessage(), null, false, false, SVNDepth.INFINITY);
        PrintStream out = cd.getOut();
        if (info != SVNCommitInfo.NULL) {
            out.println();
            out.println("Import complete, committed revision " + info.getNewRevision() + ".");
            out.flush();
        } else {
            out.println();
            if (info.getErrorMessage() != null) {
                out.println("Import failed:");
                out.println(info.getErrorMessage());
            } else {
                out.println("Import failed.");
            }
            out.flush();
        }
        out.close();
        CommitInfo ci = new CommitInfo();
        ci.setAuthor(info.getAuthor());
        ci.setDate(info.getDate());
        ci.setRevision(info.getNewRevision());
        ci.setException(info.getErrorMessage() == null ? "" : info.getErrorMessage().toString());
        CommitData commit_data = new CommitData();
        commit_data.setPaths(result_paths);
        commit_data.setInfo(ci);
        return commit_data;
    }
