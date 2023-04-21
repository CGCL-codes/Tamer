        public DownloadAndUpdateThread(File tempDir, UpdateFilesForDelete filesForDelete, UpdateOverwriteFiles overwriteFiles, UpdateParsers updateParsers, UpdateFeeds updateFeeds, UpdateShows updateShows) {
            this.tempDir = tempDir;
            this.filesForDelete = filesForDelete;
            this.overwriteFiles = overwriteFiles;
            this.updateParsers = updateParsers;
            this.updateFeeds = updateFeeds;
            this.updateShows = updateShows;
        }
