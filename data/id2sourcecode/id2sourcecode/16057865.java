    private void sayGoodbye() {
        for (String channel : session.getChannelNames()) {
            session.sayChannel(channel, "I'm melting! (built-in sword of Damocles... or bucket of water, whatever)");
        }
    }
