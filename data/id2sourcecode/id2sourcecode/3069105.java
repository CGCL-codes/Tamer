    @Override
    public PropertyReadWrite readWrite() {
        return (readMethod == null ? PropertyReadWrite.WRITE_ONLY : (writeMethod == null ? PropertyReadWrite.READ_ONLY : PropertyReadWrite.READ_WRITE));
    }
