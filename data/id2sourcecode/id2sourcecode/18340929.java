    private void writeId(UPBDeviceI device, UPBProductI product) {
        fields.add(String.valueOf(ID));
        fields.add(String.valueOf(device.getDeviceID()));
        fields.add(String.valueOf(device.getNetworkID()));
        fields.add(String.valueOf(product.getManufacturerID()));
        fields.add(String.valueOf(product.getProductID()));
        fields.add(String.valueOf(device.getFirmwareVersion() >> 8));
        fields.add(String.valueOf(device.getFirmwareVersion() & 0xff));
        fields.add(String.valueOf(product.getProductKind()));
        fields.add(String.valueOf(device.getChannelCount()));
        fields.add(String.valueOf(product.getTransmitComponentCount()));
        fields.add(String.valueOf(device.getReceiveComponentCount()));
        fields.add(device.getRoom().getRoomName());
        fields.add(device.getDeviceName());
        fields.add("0");
        writeRecord();
    }
