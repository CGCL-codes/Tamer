    public void stop(BundleContext context) throws Exception {
        plugin = null;
        store.close();
        HashMap headersMap = MessageIndex.getHEADERS_MAP();
        if (!headersMap.isEmpty()) {
            FileOutputStream fos = new FileOutputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_INDEX_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos, 1024));
            oos.writeObject(headersMap);
            oos.close();
        }
        Set readSet = FolderViewer.getReadSet();
        if (!readSet.isEmpty()) {
            FileOutputStream fos = new FileOutputStream(ConfigHelper.getDataHome() + "/data/mailbox/" + MAIL_READ_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos, 1024));
            oos.writeObject(readSet);
            oos.close();
        }
        super.stop(context);
    }
