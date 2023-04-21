    private void copyGroups() {
        Groups oldGroups = oldShow.getGroups();
        Groups newGroups = newShow.getGroups();
        for (Group oldGroup : oldGroups) {
            Group newGroup = new Group(newShow.getDirty(), oldGroup.getName());
            newGroups.add(newGroup);
            for (Channel oldChannel : oldGroup.getChannels()) {
                if (oldChannel.getId() < newShow.getNumberOfChannels()) {
                    Channel newChannel = newShow.getChannels().get(oldChannel.getId());
                    newGroup.add(newChannel);
                }
            }
        }
    }
