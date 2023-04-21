    private AudioInputStream getCDDAStream() throws Exception {
        InputStream is = null;
        int i = trackListPanel.getSelection(0);
        if (i >= 0) {
            URL url = new URL("cdda://dev/cdrom#" + (i + 1));
            is = url.openStream();
            currTrackLengthInMs = trackList.getDurationInMs(i);
        } else {
            throw new Exception("Please select a track!");
        }
        return (AudioInputStream) is;
    }
