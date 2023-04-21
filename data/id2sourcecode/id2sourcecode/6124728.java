    public void copyPublish(int[] docIds, String selfChannelPath, String[] targetChannelPaths) throws Exception {
        for (int i = 0; i < targetChannelPaths.length; i++) {
            new DocumentsDAO().copyPublish(docIds, selfChannelPath, targetChannelPaths[i]);
            refreshChannelPage(targetChannelPaths[i]);
        }
    }
