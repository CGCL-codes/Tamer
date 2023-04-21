    public void run() {
        try {
            try {
                if (sourceFile.isFile()) {
                    FileUtils.copyFile(sourceFile, destinationFile, false);
                } else {
                    FileUtils.copyDirectory(sourceFile, destinationFile, false);
                }
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECOPY:File '" + sourceFile.getPath() + "' copied to '" + destinationFile.getPath() + "' on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (SecurityException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECOPY:Security error detected!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (FileNotFoundException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECOPY:File '" + sourceFile.getPath() + "' cannot be copied to '" + destinationFile.getPath() + "' on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (ClosedByInterruptException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECOPY:File copy of file '" + sourceFile.getPath() + "' to '" + destinationFile.getPath() + "' interrupted!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (IOException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECOPY:File '" + sourceFile.getPath() + "' cannot be copied to '" + destinationFile.getPath() + "' on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            }
        } catch (Exception e) {
        }
        finished = true;
    }
