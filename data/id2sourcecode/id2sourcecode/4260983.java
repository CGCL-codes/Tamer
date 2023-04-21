    public void loadProject(String proj_url) {
        Project p = null;
        try {
            URL url = new URL(proj_url);
            p = ProjectFileReader.readProjectFromStream(url.openStream());
            setProject(p);
        } catch (Exception e) {
            handleException(e);
        }
    }
