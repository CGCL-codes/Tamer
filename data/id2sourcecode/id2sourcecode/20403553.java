    public void actionPerformed(ActionEvent e) {
        String menuString = e.getActionCommand();
        if (menuString == ChessTree.messages.getString("fileNewGame")) {
            int response = JOptionPane.showOptionDialog(mainFrame, ChessTree.messages.getString("fileNewGameConfirm"), ChessTree.messages.getString("fileNewGame"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (response == JOptionPane.OK_OPTION) {
                BoardManager.getBoardManager().resetBoard();
                ChainManager.getChainManager().resetCM();
            }
            return;
        }
        if (menuString == ChessTree.messages.getString("fileExit")) {
            int response = JOptionPane.showOptionDialog(mainFrame, ChessTree.messages.getString("fileExitConfirm"), ChessTree.messages.getString("fileExit"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (response == JOptionPane.OK_OPTION) {
                try {
                    ChessTree.ourProps.store();
                } catch (IOException e1) {
                    ChessTree.logger.severe("Exception saving config file! " + e1.toString());
                }
                System.exit(0);
            }
            return;
        }
        if (menuString == ChessTree.messages.getString("fileSave")) {
            File fileName = null;
            try {
                JFileChooser file = new JFileChooser();
                file.setMultiSelectionEnabled(false);
                ExampleFileFilter filter = new ExampleFileFilter();
                filter.addExtension("cas");
                filter.setDescription(ChessTree.messages.getString("extDescr"));
                file.setFileFilter(filter);
                int returnVal = file.showOpenDialog(CTWindowManager.getMainWindow());
                if (returnVal != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                String name = file.getSelectedFile().getAbsolutePath();
                if (!name.endsWith(".cas")) {
                    name += ".cas";
                }
                fileName = new File(name);
                ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(fileName));
                ZipEntry entry = new ZipEntry("data");
                zip.putNextEntry(entry);
                ObjectOutput s = new ObjectOutputStream(zip);
                s.writeObject(ChainManager.getChainManager().getLinkTreeRoot());
                s.writeObject(ChainManager.getChainManager().getBranchTreeRoot());
                CTGameInfoWindow info = CTWindowManager.getInfoWindow();
                s.writeObject(info.getWhiteName());
                s.writeObject(info.getBlackName());
                s.writeObject(info.getLocationText());
                s.writeObject(info.getDate());
                s.writeObject(info.getComments());
                zip.closeEntry();
                s.close();
                zip.close();
                CTWindowManager.getStatusBar().setMessage(ChessTree.messages.getString("fileSaved"));
            } catch (Exception e1) {
                CTWindowManager.getStatusBar().setMessage(ChessTree.messages.getString("fileErrorSaving"));
                ChessTree.logger.warning("Exception while saving file: " + e1.toString());
                if (fileName != null) {
                    fileName.delete();
                }
            }
            return;
        }
        if (menuString == ChessTree.messages.getString("fileLoad")) {
            try {
                JFileChooser file = new JFileChooser();
                file.setMultiSelectionEnabled(false);
                ExampleFileFilter filter = new ExampleFileFilter();
                filter.addExtension("cas");
                filter.setDescription(ChessTree.messages.getString("extDescr"));
                file.setFileFilter(filter);
                int returnVal = file.showOpenDialog(CTWindowManager.getMainWindow());
                if (returnVal != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                FileInputStream f = new FileInputStream(file.getSelectedFile());
                ZipInputStream zis = new ZipInputStream(f);
                ZipEntry entry = zis.getNextEntry();
                if (!entry.getName().equals("data")) {
                    throw new CTEInvalidFileFormat();
                }
                ObjectInput s = new ObjectInputStream(zis);
                ChainLink chainTree = (ChainLink) s.readObject();
                BranchLink branchTree = (BranchLink) s.readObject();
                ChainManager cm = ChainManager.getChainManager();
                cm.loadGame(chainTree, branchTree);
                BoardManager.getBoardManager().loadGame(cm.getCurrentLastLink());
                CTGameInfoWindow info = CTWindowManager.getInfoWindow();
                info.setWhiteName(s.readObject().toString());
                info.setBlackName(s.readObject().toString());
                info.setLocation(s.readObject().toString());
                info.setDate(s.readObject().toString());
                info.setComments(s.readObject().toString());
                CTWindowManager.getMoveWindow().updateNames();
                CTWindowManager.getStatusBar().setMessage(ChessTree.messages.getString("fileLoaded"));
            } catch (Exception e1) {
                CTWindowManager.getStatusBar().setMessage(ChessTree.messages.getString("fileErrorLoading"));
                ChessTree.logger.warning("Exception while loading file: " + e1.toString());
            }
            return;
        }
    }
