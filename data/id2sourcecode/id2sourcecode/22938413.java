    private void broadcastMembers(Set<String> members) {
        Channel channel = getBayeux().getChannel("/webadmin/members", false);
        if (channel != null) channel.publish(getClient(), members, null);
    }
