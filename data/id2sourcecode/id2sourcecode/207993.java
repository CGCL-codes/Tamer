    public void initialize(PluginInterface pluginInterface) throws PluginException {
        LoggerChannel loggerChannel = pluginInterface.getLogger().getChannel("foo");
        DownloadManager downloadManager = pluginInterface.getDownloadManager();
        TorrentManager torrentManager = pluginInterface.getTorrentManager();
        String url = "http://www.demonoid.com/files/download/HTTP/1738055/14132955";
        pluginInterface.addListener(new FooPluginListener(pluginInterface));
        try {
            TorrentDownloader torrentDownloader = torrentManager.getURLDownloader(new URL(url));
            Torrent torrent = torrentDownloader.download();
            Download download = downloadManager.addDownload(torrent);
            loggerChannel.logAlert(LoggerChannel.LT_INFORMATION, download.getName() + " \n added succesfully.");
        } catch (MalformedURLException e) {
            loggerChannel.logAlert("URL of the torrent was not right.", e);
        } catch (TorrentException e) {
            loggerChannel.logAlert("Torrent was not right.", e);
        } catch (DownloadException e) {
            loggerChannel.logAlert("Torrent could not be added.", e);
        }
    }
