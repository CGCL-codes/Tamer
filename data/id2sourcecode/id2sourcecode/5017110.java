    private Show buildBigShow() {
        long t1 = System.currentTimeMillis();
        Show show1 = ShowBuilder.build(512, 512, 512, "");
        for (int i = 0; i < 350; i++) {
            Group group = new Group(show1.getDirty(), "Group " + (i + 1));
            show1.getGroups().add(group);
            for (Channel channel : show1.getChannels()) {
                group.add(channel);
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Big show was created in " + (t2 - t1) + "ms");
        return show1;
    }
