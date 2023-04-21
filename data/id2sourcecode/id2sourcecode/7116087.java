    public void drop(DropTargetDropEvent e) {
        if (fileUploadInProgress) {
            informUploadInProgress();
            return;
        }
        this.setBorder(null);
        Transferable t = e.getTransferable();
        xferFileList = new ArrayList();
        xferFileListNames = new ArrayList();
        String topDir = "";
        changeIcon(AnimGif);
        boolean loadDirectory = true;
        String strLoadDirectory = topframe.getParameter("loadDirectory");
        if ("no".equalsIgnoreCase(strLoadDirectory)) loadDirectory = false;
        try {
            List files = new ArrayList();
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            } else if ((null != uriListFlavor) && t.isDataFlavorSupported(uriListFlavor)) {
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                String data = (String) t.getTransferData(uriListFlavor);
                files = textURIListToFileList(data);
            } else {
                e.rejectDrop();
                return;
            }
            for (int i = 0; i < files.size(); i++) {
                File f = (File) files.get(i);
                String strFileName = f.getName();
                topDir = f.getParent();
                if (topDir == null) topDir = "/";
                if (f.isDirectory()) {
                    if (!loadDirectory) {
                        errorMsgOnDirectoryLoad();
                        changeIcon(StaticGif);
                        return;
                    }
                    traverseDir(f);
                } else {
                    if (strFileName.endsWith("~")) continue;
                    xferFileList.add(f);
                    xferFileListNames.add(strFileName);
                }
            }
            e.dropComplete(true);
            Iterator fileIter = xferFileList.iterator();
            if (xferFileList.size() > 0) {
                fileLoadingInProgress();
                PostFiles poster = new PostFiles(topframe, topframe.getParameter("fileReceiverUrl"), xferFileList, topDir);
                poster.start();
            } else {
                informNoFilesCopied();
                changeIcon(StaticGif);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            e.dropComplete(false);
        }
    }
