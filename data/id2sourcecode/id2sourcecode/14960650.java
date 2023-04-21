    private TaskTagImage createTaskTagImage(Bundle bundle, String name, String resource) {
        if (name == null || name.length() == 0) {
            CommonsActivator.error("Task tag image name must not be null or empty!");
            return null;
        }
        if (resource == null || resource.length() == 0) {
            CommonsActivator.error("Task tag image resource must not be null or empty!");
            return null;
        }
        ImageDescriptor img = null;
        URL url = bundle.getResource(resource);
        if (url != null) {
            try {
                ImageData imgData = new ImageData(url.openConnection().getInputStream());
                img = ImageDescriptor.createFromImageData(imgData);
            } catch (IOException e) {
                CommonsActivator.error("unable to load '" + resource + "' image!", e);
                return null;
            }
        }
        return new TaskTagImage(name, img);
    }
