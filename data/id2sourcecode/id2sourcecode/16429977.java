    @Override
    protected void executeTask(ErrorHandler err) throws Exception {
        try {
            File resolvedSource = Files.resolve(getBaseDirectory(), this.source);
            File resolvedTarget = Files.resolve(getBaseDirectory(), this.target);
            FileUtils.copyFile(resolvedSource, resolvedTarget);
        } catch (IOException e) {
            err.error(e);
        }
    }
