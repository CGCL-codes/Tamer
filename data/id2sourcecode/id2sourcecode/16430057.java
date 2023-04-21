    public void execute() throws MojoExecutionException {
        this.checkConfig();
        File directory = getDirectory();
        this.checkDirectory(directory);
        try {
            for (File f : this.getFiles()) {
                this.checkFile(f);
                if (unpack) {
                    this.getLog().info("Extracting " + f + " into " + directory);
                    UnArchiver unArchiver = archiverManager.getUnArchiver(f);
                    unArchiver.setSourceFile(f);
                    unArchiver.setDestDirectory(directory);
                    unArchiver.extract();
                } else {
                    this.getLog().info("Copying " + f + " to " + directory);
                    FileUtils.copyFileToDirectory(f, directory);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("File copy failed", e);
        } catch (NoSuchArchiverException nsae) {
            throw new MojoExecutionException("Unknown archive", nsae);
        } catch (ArchiverException ae) {
            throw new MojoExecutionException("Error unpacking", ae);
        }
    }
