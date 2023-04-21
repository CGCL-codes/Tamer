    private void extractSingleUserFile(final File outputDirectory, final ProgressMonitor monitor, final UserFile current) {
        try {
            FileChannel inputChannel = getImg().getImageChannel();
            List<Long> clusters = current.getClusterList();
            File outputFile = new File(outputDirectory, current.getOutputFile());
            FileOutputStream fos = new FileOutputStream(outputFile);
            FileChannel outputChannel = fos.getChannel();
            for (Long cl : clusters) {
                LOGGER.finer("writing Cluster: " + cl);
                inputbuff.rewind();
                inputChannel.read(inputbuff, (long) cl * AbstractPVRFileSystem.CLUSTER_SIZE + getImg().getOffset());
                inputbuff.rewind();
                if (outputChannel.write(inputbuff) != AbstractPVRFileSystem.CLUSTER_SIZE) {
                    LOGGER.severe("not enough bytes written!");
                }
                monitor.setProgress(progress());
            }
            outputChannel.close();
        } catch (IOException e) {
            ErrorMessage.showExceptionMessage(parent, e);
            LOGGER.log(Level.SEVERE, "Error during Extraction: ", e);
        }
    }
