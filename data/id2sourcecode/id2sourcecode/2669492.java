    public void add(URL url) throws MPDServerException, MPDClientException {
        List urlContent;
        try {
            urlContent = (List) url.openConnection().getContent();
        } catch (UnsupportedMimeTypeException e) {
            urlContent = new LinkedList();
            urlContent.add(url.toString());
        } catch (IOException e) {
            throw new MPDClientException("Unable to fetch " + url.toString(), e);
        }
        if (urlContent.size() > 0) {
            String[] args = new String[1];
            Iterator it = urlContent.iterator();
            while (it.hasNext()) {
                args[0] = (String) it.next();
                this.mpd.getMpdConnection().queueCommand(MPD_CMD_PLAYLIST_ADD, args);
            }
            this.mpd.getMpdConnection().sendCommandQueue();
            this.refresh();
        }
    }
