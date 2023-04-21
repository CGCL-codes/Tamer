        @Override
        protected Object doInBackground() throws Exception {
            DocumentController newDocumentController = new DocumentController();
            if (readFromXml) {
                PaccmanFileOld paccmanFile = new PaccmanFileOld();
                logger.fine("Opening file " + file.getAbsolutePath());
                paccmanFile.read(file, newDocumentController);
                logger.fine("File opened");
                File fileOut = new File(file.getAbsolutePath() + new SimpleDateFormat("-yyMMddHHmmss").format(new Date()));
                try {
                    FileUtils.copyFile(file, fileOut);
                    logger.fine("Copied file to " + fileOut.getAbsolutePath());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException ex) {
                    throw new PaccmanIOException("Failed to make a save copy of the file");
                }
            } else {
                PaccmanDao db = new PaccmanDao(new File(file.getAbsolutePath()).getPath() + "db");
                try {
                    db.load(newDocumentController);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            newDocumentController.registerView(main);
            newDocumentController.notifyChange();
            setDocumentController(newDocumentController);
            getDocumentController().setFile(file);
            String path = file.getParent();
            MainPrefs.putDataDirectory(path);
            try {
                MainPrefs.addFilenameToMru(file.getCanonicalPath());
                MainPrefs.setLastSelectedFile(file.getCanonicalPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
