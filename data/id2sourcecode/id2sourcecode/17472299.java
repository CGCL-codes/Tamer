    public String[] getSongInfoAsStringArray() {
        String[] result = { "" + Math.round(getBitRate() / 1000), "" + getDuration(), "-", "-", "-", "" + getChannels() };
        if (getTitle() != null && getTitle().trim().length() > 0) result[2] = getTitle();
        if (getAlbum() != null && getAlbum().trim().length() > 0) result[3] = getAlbum();
        if (getAuthor() != null && getAuthor().trim().length() > 0) result[4] = getAuthor();
        return result;
    }
