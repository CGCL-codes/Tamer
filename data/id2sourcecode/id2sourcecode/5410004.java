    private void diffFile(FileNode n, ByteBuffer buffer1, ByteBuffer buffer2) {
        final FileNode node = n;
        final String path = node.getFilePath();
        int status = JDirDiff.FILE_NOT_CHANGED;
        Runner.runOnEventThreadLater(new Runnable() {

            public void run() {
                try {
                    textModel.remove(0, textModel.getLength());
                    textModel.insertString(0, path, null);
                } catch (BadLocationException e) {
                    ExceptionHandler.handle(e);
                }
            }
        });
        File oldFile = new File(oldBase.getAbsolutePath(), path);
        File newFile = new File(newBase.getAbsolutePath(), path);
        if (!oldFile.exists() && !newFile.exists()) status = FileNode.ERROR_DIFFING_FILES; else if (oldFile.exists() && !newFile.exists()) status = FileNode.DELETED; else if (!oldFile.exists() && newFile.exists()) status = FileNode.ADDED; else if (oldFile.isDirectory() || newFile.isDirectory()) {
            if (oldFile.isDirectory() && !newFile.isDirectory()) status = FileNode.DIR_CHANGED_TO_FILE; else if (!oldFile.isDirectory() && newFile.isDirectory()) status = FileNode.FILE_CHANGED_TO_DIR;
        } else {
            try {
                FileInputStream in1 = new FileInputStream(oldFile);
                FileInputStream in2 = new FileInputStream(newFile);
                FileChannel channel1 = in1.getChannel();
                FileChannel channel2 = in2.getChannel();
                int bytesRead1 = 0;
                int bytesRead2 = 0;
                boolean fileIsSame = true;
                while (bytesRead1 != -1 && bytesRead2 != -1) {
                    bytesRead1 = channel1.read(buffer1);
                    bytesRead2 = channel2.read(buffer2);
                    if (!buffer1.equals(buffer2)) {
                        status = FileNode.CHANGED;
                        buffer1.rewind();
                        buffer2.rewind();
                        buffer1.put(empty);
                        buffer2.put(empty);
                        buffer1.rewind();
                        buffer2.rewind();
                        break;
                    } else {
                        buffer1.rewind();
                        buffer2.rewind();
                    }
                }
                channel1.close();
                channel2.close();
                in1.close();
                in2.close();
            } catch (IOException e) {
                status = FileNode.ERROR_DIFFING_FILES;
            }
        }
        log.finest("diffFile=" + path + "  status=" + node.getStatus());
        node.removeFromUnprocessedTree();
        final int statusResult = status;
        if (statusResult != JDirDiff.FILE_NOT_CHANGED || numProcessedFiles % 10 == 0) Runner.runOnEventThreadLater(new Runnable() {

            public void run() {
                if (statusResult != JDirDiff.FILE_NOT_CHANGED) {
                    node.setStatus(statusResult);
                    FileNode parent = (FileNode) node.getParent();
                    TreeExpansionEvent evt = new TreeExpansionEvent(this, new TreePath(parent.getPath()));
                    listener.treeExpanded(evt);
                }
                progressModel.setValue(numProcessedFiles);
            }
        });
        numProcessedFiles++;
    }
