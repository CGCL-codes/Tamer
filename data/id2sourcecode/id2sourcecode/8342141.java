    protected int execute(final Interpretable interpretable) {
        Directory target = interpretable.getContext().getPwd();
        if (hasInvalidPath(interpretable)) {
            interpretable.getTerminalAccessor().writeLine(COMMAND + ": No directory-name given");
            return -1;
        }
        String path = interpretable.getArguments()[0];
        PathResolvingUtil pathResolver = new PathResolvingUtil(interpretable);
        String newFileName;
        if (pathResolver.hasDirNameInPath(path)) {
            pathResolver.resolveParentDirAndItemNameUnderIt(target, path);
            target = pathResolver.getTarget();
            newFileName = pathResolver.getNewItemName();
            if (target == null) {
                interpretable.getTerminalAccessor().writeLine(COMMAND + ": Directory requested in path `" + path + "` does not exist");
                return -1;
            }
        } else {
            newFileName = path;
        }
        try {
            target.addNewDir(newFileName);
        } catch (Directory.NameClashException e) {
            interpretable.getTerminalAccessor().writeLine(COMMAND + ": " + newFileName + ": Directory already exists");
            return -1;
        }
        return 0;
    }
