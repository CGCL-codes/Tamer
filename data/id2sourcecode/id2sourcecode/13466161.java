    public void endDocument() throws SAXException {
        TaggedDevice device;
        OWPath branchPath;
        Vector singleBranchVector;
        for (int i = 0; i < deviceList.size(); i++) {
            device = (TaggedDevice) deviceList.elementAt(i);
            device.setOWPath(adapter, device.getBranches());
        }
        for (int i = 0; i < branchVectors.size(); i++) {
            singleBranchVector = (Vector) branchVectors.elementAt(i);
            branchPath = new OWPath(adapter);
            for (int j = 0; j < singleBranchVector.size(); j++) {
                device = (TaggedDevice) singleBranchVector.elementAt(i);
                branchPath.add(device.getDeviceContainer(), device.getChannel());
            }
            branchPaths.addElement(branchPath);
        }
    }
