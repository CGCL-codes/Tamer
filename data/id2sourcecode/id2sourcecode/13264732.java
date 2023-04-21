    private void checkTalk() {
        boolean b = getStore().isVoicePassed() && getStore().getChannel() != null && getStore().isVoiceEnabled();
        meTalk.setEnabled(b);
    }
