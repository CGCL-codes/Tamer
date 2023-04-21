    public static HexCommand fromString(String s) throws RFIDException {
        byte[] data = s.getBytes();
        int actual_size = data.length;
        int data_size = ByteUtils.unsignedByteToInt(data[0]);
        byte[] crc = ByteUtils.range(actual_size - 2, -1, data);
        if (actual_size > 0xFF) {
            byte[] data_size_bytes = new byte[data_size];
            for (int i = 0; i < data_size; i++) data_size_bytes[i] = data[i + 1];
            data_size = ByteUtils.mergeBytes(data_size_bytes);
        }
        if (data_size != actual_size) throw new CommandException("size mismatch, given: " + data_size + ", string was: " + actual_size);
        data = ByteUtils.range(1, data.length - 1, data);
        HexCommand hc = new HexCommand(data);
        byte[] calc_crc = CRC16.crc16(data);
        if (!(ByteUtils.sameContents(crc, calc_crc))) throw new CRCException("checksum (crc16) mismatch, given: " + ByteUtils.byteArrayToHex(crc) + ", calculated: " + ByteUtils.byteArrayToHex(calc_crc));
        return hc;
    }
