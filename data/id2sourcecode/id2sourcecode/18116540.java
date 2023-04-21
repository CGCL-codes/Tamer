    private void sayGoodbye() {
        for (Channel chan : session.getChannels()) {
            chan.say("I'm melting! (built-in sword of Damocles... or bucket of water, whatever)");
        }
    }
