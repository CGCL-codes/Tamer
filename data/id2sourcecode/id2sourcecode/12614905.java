    public void execute() {
        if (cmd.equals("du") && args != null && args.length > 0) {
            long[] infos = new long[] { 0, 0, 0 };
            for (int i = 0; i < args.length; i++) {
                File file = new File(args[i]);
                fileOperator.calcDirInfo(file, infos);
            }
            fileManager.setTipInfo(infos[1] + " dirs," + infos[2] + " files, total size is " + StringUtil.formatSize(infos[0]));
            return;
        }
        if (cmd.equals("md5sum") && args != null && args.length > 0) {
            try {
                String md5sum = Digest.digest(args[0], "MD5");
                fileManager.setTipInfo("md5sum is: " + md5sum);
            } catch (Exception e) {
                fileManager.setTipInfo("md5sum can't calculate");
            }
            return;
        }
        if (cmd.equals("sha1sum") && args != null && args.length > 0) {
            try {
                String md5sum = Digest.digest(args[0], "SHA");
                fileManager.setTipInfo("sha1sum is: " + md5sum);
            } catch (Exception e) {
                fileManager.setTipInfo("sha1sum can't calculate");
            }
            return;
        }
        if (cmd.equals("tabnew")) {
            fileManager.tabnew(pwd, FileLister.FS_ROOT);
            return;
        }
        if (cmd.equals("tabclose")) {
            fileManager.tabclose();
        }
        if (cmd.equals("cd") && args != null && args.length > 0) {
            String newPath = FilenameUtils.concat(pwd, args[0]);
            if (newPath != null) if (newPath.endsWith(File.separator) && !newPath.equals(File.separator)) newPath = newPath.substring(0, newPath.length() - 1);
            fileLister.visit(newPath);
            return;
        }
        if (cmd.equals("sort")) {
            String[] options = new String[] { Messages.getString("MiscFileCommand.name"), Messages.getString("MiscFileCommand.lastModified"), Messages.getString("MiscFileCommand.size") };
            String result = new Util().openConfirmWindow(options, Messages.getString("MiscFileCommand.title"), Messages.getString("MiscFileCommand.tipInfo"), OptionShell.WARN);
            if (result == null) return;
            if (result.equals(Messages.getString("MiscFileCommand.size"))) {
                fileLister.sort("size");
            } else if (result.equals(Messages.getString("MiscFileCommand.lastModified"))) {
                fileLister.sort("date");
            } else if (result.equals(Messages.getString("MiscFileCommand.name"))) {
                fileLister.sort("name");
            }
            return;
        }
    }
