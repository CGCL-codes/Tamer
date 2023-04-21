    public void setImage(String imageName) {
        try {
            InputStream url = ProjectManager.getCurrentProject().getUrl(imageName).openStream();
            Image im = new Image(getDisplay(), url);
            if (im != null) setImage(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
