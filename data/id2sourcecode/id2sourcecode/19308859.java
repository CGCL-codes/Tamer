    private void startPlaying(Button btn) {
        List<Button> stopPlaying = new ArrayList<Button>();
        for (Button oldBtn : isPlaying) {
            if (oldBtn.getChannel() == btn.getChannel()) {
                if (!oldBtn.isLooping() || btn.isLooping()) {
                    log(oldBtn + " was on same channel, stop playing it");
                    stopPlaying.add(oldBtn);
                } else {
                    log(oldBtn + " was muted");
                }
            }
        }
        for (Button b : stopPlaying) stopPlaying(b);
        boolean remove;
        for (Button b : pitchSet.buttons()) {
            remove = false;
            if (btn.isOwnBpm() && b != btn) {
                remove = true;
            } else if (!btn.isOwnBpm() && b.isOwnBpm()) {
                remove = true;
            }
            if (remove) {
                pitchSet.remove(b);
                b.setPreparedSample(null);
            } else {
                b.setPitchedSample(b.getPreparedSample());
            }
        }
        pitchSet.buttonChosen();
        isPlaying.add(btn);
    }
