    public String get(int index) {
        if (item == null) {
            return "";
        }
        switch(index) {
            case 0:
                return item.getName();
            case 1:
                return item.getTitle();
            case 2:
                return item.getArtist();
            case 3:
                return item.getAlbum();
            case 4:
                return item.getChannelInfo();
            case 5:
                return item.getBitRate();
            case 6:
                return item.getSampled();
            case 7:
                return item.getFormattedLength();
            default:
                return "YOYOPlayer";
        }
    }
