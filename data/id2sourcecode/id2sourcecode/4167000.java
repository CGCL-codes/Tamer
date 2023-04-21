    public boolean createDeviceChannel(String title, String link, String description, String imageURL, String imageTitle, int maxNumberOfItems) {
        try {
            FSAChannel channel = new FSAChannel();
            channel.setTitle(title);
            if (!imageURL.contentEquals("")) {
                ImageIF image = new Image();
                image.setLink(new URL(imageURL));
                image.setTitle(imageTitle);
                channel.setImage(image);
            }
            channel.setDescription(description);
            if (!link.contentEquals("")) {
                channel.setSite(new URL(link));
            }
            channel.setMaxNumberOfItems(maxNumberOfItems);
            channel.setUpdateFrequency(1);
            channel.setUpdateFrequency(1);
            channel.setTtl(2);
            if (getChannels().containsKey(title)) {
                return false;
            }
            getChannels().put(title, channel);
            return true;
        } catch (MalformedURLException ex) {
            Logger.getLogger(RSS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
