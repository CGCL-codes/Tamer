    @Override
    public void load(Project project, URL url) throws OpenException {
        try {
            load(project, url.openStream());
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }
