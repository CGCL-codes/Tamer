    public FtpletResult handleStor(Request request, Entry group, FtpSession session, FtpRequest ftpRequest) throws Exception {
        if (!getRepository().getAccessManager().canDoAction(request, group, Permission.ACTION_NEW)) {
            return handleError(session, ftpRequest, FtpReply.REPLY_450_REQUESTED_FILE_ACTION_NOT_TAKEN, "You do not have the access to store the file");
        }
        String name = ftpRequest.getArgument();
        if (name.trim().length() == 0) {
            return handleError(session, ftpRequest, "Bad file name");
        }
        File newFile = getRepository().getStorageManager().getTmpFile(request, name);
        FileOutputStream fos = getRepository().getStorageManager().getFileOutputStream(newFile);
        try {
            System.err.println("transferring from client");
            session.getDataConnection().openConnection().transferFromClient(session, fos);
            System.err.println("done");
        } finally {
            IOUtil.close(fos);
        }
        newFile = getRepository().getStorageManager().moveToStorage(request, newFile);
        System.err.println("making entry");
        Entry entry = getEntryManager().addFileEntry(request, newFile, group, name, request.getUser());
        System.err.println("closing session");
        session.write(new DefaultFtpReply(FtpReply.REPLY_257_PATHNAME_CREATED, "File created"));
        return FtpletResult.SKIP;
    }
