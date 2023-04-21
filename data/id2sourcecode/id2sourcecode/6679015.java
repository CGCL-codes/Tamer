    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {
        if (saveTempData()) {
            for (String gamename : tempExePath.keySet()) {
                String ID = GameDatabase.getIDofGame(gamename);
                GameDatabase.setLocalExecutablePath(ID, tempExePath.get(gamename));
            }
            for (String gamename : tempInstallPath.keySet()) {
                String ID = GameDatabase.getIDofGame(gamename);
                GameDatabase.setLocalInstallPath(ID, tempInstallPath.get(gamename));
            }
            for (String gamename : tempParams.keySet()) {
                String ID = GameDatabase.getIDofGame(gamename);
                GameDatabase.setAdditionalParameters(ID, tempParams.get(gamename));
            }
            GameDatabase.saveLocalPaths();
            for (String gamename : tempExePath.keySet()) {
                ChannelPanel cp = TabOrganizer.getChannelPanel(gamename);
                if (cp != null) {
                    cp.setLaunchable(true);
                }
                String ID = GameDatabase.getIDofGame(gamename);
                GameDatabase.addIDToInstalledList(ID);
            }
            FrameOrganizer.getClientFrame().refreshInstalledGames();
            FrameOrganizer.closeManageGamesFrame();
        }
        Settings.setWineCommand(cmb_winEnv.getSelectedItem().toString());
        Settings.setDOSBoxExecutable(tf_dosboxExe.getText());
        Settings.setDOSBoxFullscreen(cb_dosboxFullscreen.isSelected());
    }
