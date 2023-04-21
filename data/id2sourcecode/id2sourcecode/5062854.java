    public void setOWPath(DSPortAdapter adapter, Vector Branches) {
        branchPath = new OWPath(adapter);
        TaggedDevice TDevice;
        for (int i = 0; i < Branches.size(); i++) {
            TDevice = (TaggedDevice) Branches.elementAt(i);
            branchPath.add(TDevice.getDeviceContainer(), TDevice.getChannel());
        }
    }
