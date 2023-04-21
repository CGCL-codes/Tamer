    @Test
    @Ignore
    public void testChannelsAndUsers() {
        ServerStatus status = MMOMumbleServerStatus.getServerStatus(190L, TOKEN, SECRET);
        for (Channel channel : status.getChannels()) {
            int parent = channel.getParent();
            if (parent == -1) {
                log.info("" + channel.getId() + ". " + channel.getName());
            } else {
                log.info("" + channel.getId() + ". " + channel.getName() + " [child of " + parent + "]");
            }
            log.info("  State: " + channel.getState());
            log.info("  Position: " + channel.getPosition());
        }
        for (MumbleUser user : status.getUsers()) {
            log.info("User " + user.getName() + " [in channel " + user.getChannel() + "]");
            log.info("  Seconds online: " + user.getSecondsOnline());
            log.info("  State: " + user.getState());
            log.info("  Deaf: " + user.isDeaf());
            log.info("  Mute: " + user.isMute());
        }
    }
