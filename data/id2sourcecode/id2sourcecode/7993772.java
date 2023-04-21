        @Override
        public void selectionOnPress(CoolButtonSelectionEvent arg0) {
            Correspondence correspondence = groovesharkTrack;
            FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
            dialog.setText(Messages.getString("DownloadSongSelectionListener.SaveFileAs"));
            dialog.setFileName(String.format("%s - %s - %s", correspondence.getArtistName(), correspondence.getSongName(), correspondence.getAlbumName()));
            dialog.setFilterExtensions(new String[] { "*.mp3", "*.*" });
            final String filePath = dialog.open();
            if (StringUtils.isEmpty(filePath)) {
                return;
            }
            Job downloadSongJob = new Job(Messages.getString("IntegratedGroovesharkPlayer.SavingTrack"), new Image(Display.getDefault(), getClass().getResourceAsStream(GroofyConstants.PATH_16_SAVE))) {

                @Override
                public JobStatus run() {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        InputStream source = new ByteArrayInputStream(player.getMusicBytes());
                        byte buffer[] = new byte[1024];
                        int read;
                        do {
                            read = source.read(buffer);
                            if (read > 0) {
                                fileOutputStream.write(buffer, 0, read);
                            }
                        } while (read != -1);
                        fileOutputStream.close();
                        source.close();
                    } catch (IOException e) {
                        GroofyLogger.getInstance().logError(e.getLocalizedMessage());
                    }
                    return JobStatus.OK;
                }
            };
            downloadSongJob.schedule();
        }
