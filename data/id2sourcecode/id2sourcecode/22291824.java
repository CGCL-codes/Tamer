    public void execute(String[] args) {
        Session session = Session.getInstance();
        String sessionID = session.getSessionID();
        if (sessionID == null) {
            System.out.println("ERROR: You must login.");
            return;
        }
        VfsService vfsService = VfsContext.getService();
        CommandLine cl = null;
        try {
            cl = new PosixParser().parse(_options, args);
        } catch (ParseException e) {
            printUsage(e.getMessage());
            return;
        }
        if (cl.getArgs() == null || cl.getArgs().length != 2) {
            printUsage("ERROR: require local_file_path and vfs_file_name");
            return;
        }
        File file = new File(cl.getArgs()[0]);
        if (!file.exists()) {
            System.out.println("ERROR: the file not found.");
            return;
        }
        if (!file.isFile()) {
            System.out.println("ERROR: the file not file.");
            return;
        }
        if (!file.canRead()) {
            System.out.println("ERROR: the file access denied.");
            return;
        }
        SystemInfo systemInfo = vfsService.getSystemInfo();
        String parentFileID = session.getCurrentDirectory().getID();
        String fileSessionID = null;
        try {
            VfsFile vfsFile = vfsService.createFile(sessionID, parentFileID, cl.getArgs()[1]);
            if (vfsService.isExistsName(sessionID, vfsFile.getID(), cl.getArgs()[1])) {
                System.out.println("ERROR: the vfs_file_name already used.");
                return;
            }
            fileSessionID = vfsService.createFileSession(sessionID, vfsFile.getID(), FileSession.Mode.WRITE);
            byte[] buf = new byte[systemInfo.getBytesPerWrite()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                int count = 0;
                while ((count = fis.read(buf)) != -1) vfsService.writeData(sessionID, fileSessionID, buf, count);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } finally {
                if (fis != null) try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (fileSessionID != null) try {
                vfsService.closeFileSession(sessionID, fileSessionID);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
