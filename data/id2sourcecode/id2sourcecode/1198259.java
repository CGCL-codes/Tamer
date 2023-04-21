    public ImportProcessResult process() {
        LOG.info("Importing backup file '" + this.backupFile.getAbsolutePath() + "'.");
        ImportProcessResult result = new ImportProcessResult();
        final File targetDirectory = new File(PreferencesDao.getInstance().getTemporaryFolder(), TARGET_DIRECTORY_FORMAT.format(new Date()));
        try {
            try {
                ZipUtil.unzip(this.backupFile, this.backupZipFile, targetDirectory);
            } catch (BusinessException e) {
                result.setErrorMessage("Could not unpack backup file '" + this.backupFile.getAbsolutePath() + "'!");
                return result;
            }
            assert (targetDirectory.exists() && targetDirectory.isDirectory());
            int storedDataVersion;
            try {
                storedDataVersion = readDataVersion(new File(targetDirectory, FILE_DATA_VERSION));
            } catch (BusinessException e) {
                result.setErrorMessage("Getting data version of backup file '" + this.backupFile.getAbsolutePath() + "' failed!");
                return result;
            }
            if (storedDataVersion != Movie.DATA_VERSION) {
                result.setErrorMessage("The backup can not be read by this application version!\n" + "Try using a more recent version of OurMovies.\n" + "Backup's version: " + storedDataVersion + "; Version in use: " + Movie.DATA_VERSION);
                return result;
            }
            List<Movie> backedUpMovies;
            try {
                backedUpMovies = parseXmlMovies(targetDirectory);
            } catch (BusinessException e) {
                result.setErrorMessage("The XML contents of the backup file '" + this.backupFile.getAbsolutePath() + "' are corrupted!");
                return result;
            }
            final List<Movie> insertedMovies = new ArrayList<Movie>(backedUpMovies.size());
            final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
            final boolean wasAutoCommitEnabled = dao.isAutoCommit();
            boolean copyingSucceeded = false;
            try {
                dao.setAutoCommit(false);
                LOG.info("Trying to insert " + backedUpMovies.size() + " movies into database.");
                final List<String> movieFolderPaths = dao.getMovieFolderPaths();
                for (Movie movie : backedUpMovies) {
                    if (movie.isFolderPathSet() == true && movieFolderPaths.contains(movie.getFolderPath())) {
                        LOG.debug("Skipping movie because its folder path '" + movie.getFolderPath() + "' is already in use (movie=" + movie + ").");
                        result.addSkippedMovie(movie);
                        continue;
                    }
                    LOG.debug("Inserting movie: " + movie);
                    final Movie insertedMovie = dao.insertMovie(movie);
                    insertedMovies.add(insertedMovie);
                }
                this.copyCoverFiles(targetDirectory, insertedMovies);
                dao.commit();
                copyingSucceeded = true;
            } catch (BusinessException e) {
                result.setErrorMessage("Some internal database error occured\nduring import of backup file '" + this.backupFile.getAbsolutePath() + "'!");
                return result;
            } finally {
                if (copyingSucceeded == false) {
                    dao.rollback();
                }
                dao.setAutoCommit(wasAutoCommitEnabled);
            }
            result.setInsertedMovie(insertedMovies);
            result.succeeded();
            return result;
        } finally {
            try {
                if (targetDirectory.exists()) {
                    FileUtil.deleteDirectoryRecursive(targetDirectory);
                }
            } catch (Exception e) {
                LOG.error("Could not delete temporary import directory '" + targetDirectory.getAbsolutePath() + "'.", e);
            }
        }
    }
