    private static synchronized void initPlaylistXmlPath(ServletContext servletContext) {
        String tmporaryDirectoryPath = System.getProperty("java.io.tmpdir");
        File playlistXml = new File(tmporaryDirectoryPath + System.getProperty("file.separator") + "playlist.xml");
        if (!playlistXml.exists()) {
            try {
                File playlistTemplateXml = new File(servletContext.getRealPath("playlist.xml"));
                FileUtils.copyFile(playlistTemplateXml, playlistXml);
            } catch (IOException ex) {
                Logger.getLogger(MusicBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MusicBox.playlistXmlPath = playlistXml.getAbsolutePath();
    }
