    public int fetchContentFrom(Workflow from, String uname) {
        int count = 0;
        List dataPaths = from.getDataset(from.getTemplate().getDefaultDsetName()).getAllDatabitPaths();
        for (Iterator it = dataPaths.iterator(); it.hasNext(); ) {
            String targetPath = (String) it.next();
            String dataPath = from.getTemplate().getDefaultDsetName() + "." + targetPath;
            Databit fromBit = from.getDatabit(dataPath);
            String fromContent = fromBit.getValue();
            if (this.containsDatabit(this.getTemplate().getDefaultDsetName() + "." + targetPath)) {
                Databit targetBit = this.getDatabit(this.getTemplate().getDefaultDsetName() + "." + targetPath);
                try {
                    if (!fromBit.getType().equals(targetBit.getType())) {
                        Logger.WARN("Incompatible Databits in path: " + dataPath + " " + fromBit.getType() + "/" + targetBit.getType());
                    }
                    if (!fromBit.getValue().equals(targetBit.getValue())) {
                        if (fromBit.getType().equals("fileref")) {
                            Logger.DEBUG("Going to copy file: " + fromBit.getValue());
                            String fileDir = SWAMP.getInstance().getProperty("ATTACHMENT_DIR");
                            String fs = System.getProperty("file.separator");
                            File fromFile = new File(fileDir + fs + fromBit.getId() + "-" + fromContent);
                            File toFile = new File(fileDir + fs + targetBit.getId() + "-" + fromContent);
                            FileUtils.copyFile(fromFile, toFile);
                        }
                        targetBit.setValue(fromContent, uname);
                        count++;
                        Logger.DEBUG("Copied Databit " + dataPath + "(" + fromContent + ") to Wf-" + this.getId());
                    }
                } catch (Exception e) {
                    Logger.ERROR("Databit copy failed: " + e.getMessage());
                }
            }
        }
        return count;
    }
