    public List<Extension> getExtensions(String channelName) {
        Channel channel = RBNBController.getInstance().getChannel(channelName);
        String mime = null;
        if (channel != null) {
            mime = channel.getMetadata("mime");
        }
        List<Extension> usefulExtensions = new ArrayList<Extension>();
        for (int i = 0; i < extensions.size(); i++) {
            Extension extension = (Extension) extensions.get(i);
            List<String> mimeTypes = extension.getMimeTypes();
            for (int j = 0; j < mimeTypes.size(); j++) {
                String mimeType = mimeTypes.get(j);
                if (mimeType.equals(mime)) {
                    usefulExtensions.add(extension);
                }
            }
        }
        return usefulExtensions;
    }
