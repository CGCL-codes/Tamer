    public void add(ChatLogEntry e) {
        all.add(e);
        String channel = e.getChannel();
        if (channel == null) return;
        if (channel.equals("Whisper") || channel.equals("Say") || channel.equals("Yell")) {
            whisper.add(e);
        } else if (channel.equals("Guild") || channel.equals("Officer")) {
            guild.add(e);
        } else if (e.getType().equals("Public Chat")) {
            pub.add(e);
        }
    }
