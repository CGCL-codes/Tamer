    public boolean transfer(String extension) throws Exception {
        return TegsoftPBX.transfer(extension, getActiveICR(), getRequest(), getChannel());
    }
