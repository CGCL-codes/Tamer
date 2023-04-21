    private void _setTitle() {
        int num = map.getChannel().getView().getMembers().size();
        setTitle("ReplicatedHashMapDemo: " + num + " server(s)");
    }
