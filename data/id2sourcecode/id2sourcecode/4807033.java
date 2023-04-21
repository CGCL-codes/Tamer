    @Override
    public void execute() throws BuildException {
        try {
            long time = source.lastModified();
            receiver.setLastModified(time);
        } catch (SecurityException sex) {
            throw new BuildException("Can't read from source or write to target", sex);
        } catch (IllegalArgumentException illArgEx) {
            throw new BuildException("Illegal timestamp read from source", illArgEx);
        }
    }
