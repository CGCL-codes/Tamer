    public void setValue(int value) throws IOException {
        if (type.isReadOnly()) {
            return;
        }
        if (value < getMinValue()) value = getMinValue();
        if (value > getMaxValue()) value = getMaxValue();
        int oldValue = this.value;
        byte[] data = new byte[3];
        if (getChannel() <= 256) {
            data[0] = 0x48;
            data[1] = (byte) (getChannel() - 1);
            data[2] = (byte) value;
        } else if (getChannel() <= 512) {
            data[0] = 0x49;
            data[1] = (byte) (getChannel() - 1);
            data[2] = (byte) value;
        }
        getController().getOutputStream().write(data);
        this.value = value;
        fireValueChanged(oldValue);
    }
