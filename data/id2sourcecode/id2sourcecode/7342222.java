        @Override
        public void run() {
            updateResetRecommendation();
            this.myRange.setMaximum(this.myFilesPicked.length - 1);
            UndoableMovingFiles undo = null;
            try {
                if (this.myToNode != null) {
                    undo = new UndoableMovingFiles(this.myToNode, MoveManager.this.myRDPmanager, this.iSetRepeatButtonToThisDirectory);
                    undo.setMoveNotCopy(this.iMoveNotCopy);
                    try {
                        undo.add(this.myFilesPicked, this.myRange);
                    } catch (org.gerhardb.lib.io.TargetFileExistsException ex) {
                        System.out.println("TargetFileExistsException Issue in MoveManager MoveOrCopyIt NODE");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (this.myToDir != null) {
                    undo = new UndoableMovingFiles(new File(this.myToDir), MoveManager.this.myRDPmanager);
                    undo.setMoveNotCopy(this.iMoveNotCopy);
                    try {
                        undo.add(this.myFilesPicked, this.myRange);
                    } catch (org.gerhardb.lib.io.TargetFileExistsException ex) {
                        System.out.println("TargetFileExistsException Issue in MoveManager MoveOrCopyIt DIRECTORY");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("MoveOrCopyIt: Move Pressed with empty directory: THIS SHOULD NEVER HAPPEN");
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            File[] potentialFailedFiles = new File[0];
            if (undo != null) {
                potentialFailedFiles = undo.getFailedFilesShowingMessage(this.myTopFrame);
                if (potentialFailedFiles.length > 0) {
                    System.out.println("MoveOrCopyIt: Number of files that failed: " + potentialFailedFiles.length);
                } else {
                    MoveManager.this.myRDPmanager.myPlugins.clearListSelections();
                }
            }
            final File[] failedFiles = potentialFailedFiles;
            this.myDialog.done();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (MoveOrCopyIt.this.iMoveNotCopy) {
                        MoveOrCopyIt.this.myTopFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        MoveManager.this.myRDPmanager.myTreeManager.myCoordinator.getFileList().clearSelection();
                        MoveManager.this.myRDPmanager.myPlugins.reloadScroller(MoveManager.this.myResetRecommendationFile, IScroll.KEEP_CACHE);
                        MoveManager.this.myRDPmanager.getIScrollDirTree().selectFiles(failedFiles);
                        MoveOrCopyIt.this.myTopFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    MoveManager.this.myRDPmanager.myTreeManager.myCoordinator.getScroller().requestFocus();
                }
            });
        }
