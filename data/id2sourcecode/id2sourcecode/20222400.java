    private void saveToPod() {
        try {
            MessageBox msgBox = new MessageBox(sShell, SWT.OK);
            msgBox.setMessage("The file already exists! Do you want to overwrite it?");
            if (!ListPodDir.isDirectory()) {
                msgBox.setMessage("There is a problem with the player device! Please check the player device and restart the programm!");
                msgBox.open();
                return;
            }
            if (tabFolder.getSelectionIndex() == 0) {
                if (tableBigPlaylist.getItemCount() == 0) {
                    msgBox.setMessage("The playlist is empty!");
                    msgBox.open();
                    return;
                }
                try {
                    FileWriter outFile = new FileWriter(ListPodDir + "/" + combo.getText().toLowerCase() + Constantsv1.playlistFileEnd);
                    PrintWriter out = new PrintWriter(outFile);
                    for (int i = 0; i < tableBigPlaylist.getItemCount(); i++) {
                        out.print(Constantsv1.podPlaylistFolder + tableBigPlaylist.getItem(i).getText(1) + Constantsv1.SoundfileEnd + "\n");
                    }
                    out.close();
                    msgBox.setMessage("Export to player complete!");
                    msgBox.open();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (tabFolder.getSelectionIndex() == 1) if (tableDay01.getItemCount() != 0 || tableDay02.getItemCount() != 0 || tableDay03.getItemCount() != 0 || tableDay04.getItemCount() != 0 || tableDay05.getItemCount() != 0 || tableDay06.getItemCount() != 0 || tableDay07.getItemCount() != 0 || tableDay08.getItemCount() != 0 || tableDay09.getItemCount() != 0 || tableDay10.getItemCount() != 0 || tableDay11.getItemCount() != 0 || tableDay12.getItemCount() != 0 || tableDay13.getItemCount() != 0 || tableDay14.getItemCount() != 0 || tableDay15.getItemCount() != 0) {
                makePodList("playlist1", tableDay01);
                makePodList("playlist2", tableDay02);
                makePodList("playlist3", tableDay03);
                makePodList("playlist4", tableDay04);
                makePodList("playlist5", tableDay05);
                makePodList("playlist6", tableDay06);
                makePodList("playlist7", tableDay07);
                makePodList("playlist8", tableDay08);
                makePodList("playlist9", tableDay09);
                makePodList("playlist10", tableDay10);
                makePodList("playlist11", tableDay11);
                makePodList("playlist12", tableDay12);
                makePodList("playlist13", tableDay13);
                makePodList("playlist14", tableDay14);
                makePodList("playlist15", tableDay15);
                msgBox.setMessage("Export to player complete!");
                msgBox.open();
            } else {
                msgBox.setMessage("The playlists are empty!");
                msgBox.open();
                return;
            }
        } catch (Exception e) {
        }
    }
