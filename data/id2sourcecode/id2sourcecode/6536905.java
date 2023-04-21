    public void run() {
        try {
            JProgressBarGlassPane glassPane = new JProgressBarGlassPane();
            glassPane.setSize(frame.getContentPane().getSize());
            glassPane.getProgressBar().setMinimum(0);
            setGlassPane(glassPane);
            glassPane.setString(Messages.getString("PicasaWebAlbumDownloaderControler.loading.feed"));
            glassPane.getProgressBar().setIndeterminate(true);
            RSSFeedReader reader = new RSSFeedReader();
            RSS rss = reader.load(new URL(frame.getURLFeed()));
            glassPane.setString(Messages.getString("PicasaWebAlbumDownloaderControler.downloading"));
            glassPane.getProgressBar().setStringPainted(true);
            glassPane.getProgressBar().setIndeterminate(false);
            glassPane.getProgressBar().setMaximum(rss.getChannel().getItems().size() + 1);
            glassPane.getProgressBar().setValue(0);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                List<Exception> errors = new ArrayList<Exception>();
                for (Item item : rss.getChannel().getItems()) {
                    glassPane.getProgressBar().setString(glassPane.getProgressBar().getValue() + "/" + rss.getChannel().getItems().size());
                    glassPane.getProgressBar().setValue(glassPane.getProgressBar().getValue() + 1);
                    Enclosure enclosure = item.getEnclosure();
                    if (enclosure != null && enclosure.getType().equalsIgnoreCase("image/jpeg")) {
                        try {
                            download(enclosure.getUrl(), fileChooser.getSelectedFile() + File.separator + item.getTitle());
                        } catch (Exception e) {
                            e.printStackTrace();
                            errors.add(e);
                        }
                    }
                }
                removeGlassPane();
                if (errors.size() > 0) displayErrors(errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        } finally {
            frame.setUrlFeed("");
        }
    }
