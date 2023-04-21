    private void addTreeLevel(Group parent, int x1, int z1, int x2, int z2, int l) {
        int xc = (x1 + x2) / 2;
        int zc = (z1 + z2) / 2;
        if (l == spatialTreeDepth - 1) {
            addTiles(parent, x1, z1, xc, zc);
            addTiles(parent, xc, z1, x2, zc);
            addTiles(parent, xc, zc, x2, z2);
            addTiles(parent, x1, zc, xc, z2);
            return;
        }
        l++;
        Group nwGroup = new Group();
        addTreeLevel(nwGroup, x1, z1, xc, zc, l);
        parent.addChild(nwGroup);
        Group neGroup = new Group();
        addTreeLevel(neGroup, xc, z1, x2, zc, l);
        parent.addChild(neGroup);
        Group seGroup = new Group();
        addTreeLevel(seGroup, xc, zc, x2, z2, l);
        parent.addChild(seGroup);
        Group swGroup = new Group();
        addTreeLevel(swGroup, x1, zc, xc, z2, l);
        parent.addChild(swGroup);
    }
