    private void writeCommandToExternalCommandFile(String command) throws ConfigurationGenerationException {
        try {
            FileChannel channel = new FileOutputStream(new File(this.registry.getCommandFile()), true).getChannel();
            FileLock lock = channel.tryLock();
            while (lock == null) lock = channel.tryLock();
            channel.write(ByteBuffer.wrap(command.getBytes()));
            lock.release();
            channel.close();
        } catch (FileNotFoundException e) {
            logger.debug(cn + ".writeCommandToExternalCommandFile() - Cannot Find External Command File");
            throw new ConfigurationGenerationException("Cannot Locate External Command File to write too", e);
        } catch (IOException e) {
            logger.debug(cn + ".writeCommandToExternalCommandFile() - Error Writing to External Command File");
            throw new ConfigurationGenerationException("Cannot Write To Specified External Command File", e);
        }
    }
