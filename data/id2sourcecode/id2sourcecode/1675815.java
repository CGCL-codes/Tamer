    public Integer execute(Arguments args) {
        int result = -1;
        String source = (String) ArgumentUtil.getArgumentValue(args, "source");
        String target = (String) ArgumentUtil.getArgumentValue(args, "target");
        ActionType action = getActionType((String) ArgumentUtil.getArgumentValue(args, "type"));
        if (action == null || "".equals(target) || "".equals(source)) return new Integer(result);
        File filesource = new File(source);
        if (!filesource.exists()) return new Integer(result);
        File filetarget = new File(target);
        try {
            switch(action) {
                case Copy:
                    if (filesource.isDirectory()) FileUtils.copyDirectory(filesource, filetarget, true); else FileUtils.copyFile(filesource, filetarget, true);
                    break;
                case Move:
                    if (filesource.isDirectory()) FileUtils.moveDirectoryToDirectory(filesource, filetarget, true); else FileUtils.moveFile(filesource, filetarget);
                    break;
                case Delete:
                    if (filesource.isDirectory()) FileUtils.deleteDirectory(filesource); else FileUtils.deleteQuietly(filesource);
                    break;
                case Rename:
                    filesource.renameTo(filetarget);
                    break;
            }
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Integer(result);
    }
