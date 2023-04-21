        public void run() {
            Configuration c = Configuration.getInstance();
            List<String> directoryList = null;
            List<String> fileList = null;
            if (c.exists("org.dsgt.data." + this.profile + ".directories")) {
                directoryList = StringUtils.splitAsList(c.getString("org.dsgt.data." + this.profile + ".directories"), "|");
            }
            if (c.exists("org.dsgt.data." + this.profile + ".files")) {
                fileList = StringUtils.splitAsList(c.getString("org.dsgt.data." + this.profile + ".files"), "|");
            }
            File saveDirectory = null;
            if (c.exists("org.dsgt.data.saves.directory")) {
                String location = c.getString("org.dsgt.data.saves.directory");
                File f = new File(location);
                if (f.exists()) {
                    saveDirectory = f;
                }
            }
            File cardDirectory = null;
            if (c.exists("org.dsgt.data.card.directory")) {
                String location = c.getString("org.dsgt.data.card.directory");
                File f = new File(location);
                if (f.exists()) {
                    cardDirectory = f;
                }
            }
            if (directoryList != null && fileList != null && directoryList.size() > 0 && fileList.size() > 0 && saveDirectory != null && cardDirectory != null) {
                List<File> games = FileUtils.findFirstFileInstances(directoryList, fileList);
                long toBeTransferred = 0;
                long totalTransferred = 0;
                for (File game : games) {
                    toBeTransferred += game.length();
                }
                this.fireTransferStarted(new GameTransferStatusEvent(this, toBeTransferred, totalTransferred));
                File[] saveFiles = cardDirectory.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".sav");
                    }
                });
                for (File saveFile : saveFiles) {
                    File dest = new File(StringUtils.pathCombine(saveDirectory.getAbsolutePath(), saveFile.getName()));
                    if (dest.exists()) {
                        dest.delete();
                    }
                    saveFile.renameTo(dest);
                }
                File[] oldFiles = cardDirectory.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        String lowerName = name.toLowerCase();
                        return (lowerName.endsWith(".sav") || lowerName.endsWith(".nds")) && !lowerName.equals(HANDS_OFF_FILE);
                    }
                });
                for (File oldFile : oldFiles) {
                    oldFile.delete();
                }
                for (File game : games) {
                    String sourceFileName = game.getAbsolutePath();
                    String destFileName = StringUtils.pathCombine(cardDirectory.getAbsolutePath(), game.getName());
                    long gameSize = game.length();
                    this.fireTransferProgressed(new GameTransferStatusEvent(this, toBeTransferred, totalTransferred + gameSize / 2));
                    statusLabel.setText(String.format(GUIUtils.getString("statusLabel.text"), game.getName().substring(0, game.getName().lastIndexOf('.'))));
                    FileUtils.copyFile(sourceFileName, destFileName);
                    totalTransferred += gameSize;
                    this.fireTransferProgressed(new GameTransferStatusEvent(this, toBeTransferred, totalTransferred));
                    if (cancelTransferIssued) {
                        cancelTransferIssued = false;
                        this.fireTransferEnded(new GameTransferStatusEvent(this));
                        return;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                for (File game : games) {
                    String saveFileName = game.getName().substring(0, game.getName().lastIndexOf('.')) + ".SAV";
                    File saveFile = new File(StringUtils.pathCombine(saveDirectory.getAbsolutePath(), saveFileName));
                    if (saveFile.exists()) {
                        String sourceFileName = saveFile.getAbsolutePath();
                        String destFileName = StringUtils.pathCombine(cardDirectory.getAbsolutePath(), saveFile.getName());
                        FileUtils.copyFile(sourceFileName, destFileName);
                    }
                }
                this.fireTransferEnded(new GameTransferStatusEvent(this));
            } else {
                GameTransferStatusEvent statusEvent = null;
                if (directoryList == null || directoryList.size() == 0) {
                    statusEvent = new GameTransferStatusEvent(this, GameTransferStatusCode.SOURCE_DIRECTORIES_NOT_SELECTED);
                } else if (fileList == null || fileList.size() == 0) {
                    statusEvent = new GameTransferStatusEvent(this, GameTransferStatusCode.SOURCE_FILES_NOT_SELECTED);
                } else if (saveDirectory == null) {
                    statusEvent = new GameTransferStatusEvent(this, GameTransferStatusCode.SAVE_DIRECTORY_NOT_SELECTED);
                } else if (cardDirectory == null) {
                    statusEvent = new GameTransferStatusEvent(this, GameTransferStatusCode.CARD_DIRECTORY_NOT_SELECTED);
                }
                this.fireTransferFailed(statusEvent);
            }
        }
