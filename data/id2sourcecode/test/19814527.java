    @Override
    public byte[] getSectionBytes() {
        BitOutputStream os = new BitOutputStream(getSectionSizeInByte() * Byte.SIZE);
        os.writeFromLSB(table_id.getValue(), 8);
        os.writeFromLSB(section_syntax_indicator, 1);
        os.writeFromLSB(private_indicator, 1);
        os.writeFromLSB(reserved1, 2);
        os.writeFromLSB(getPrivateSectionLength(), 12);
        if (getSectionSyntaxIndicator() != 0) {
            os.writeFromLSB(table_id_extension, 16);
            os.writeFromLSB(reserved2, 2);
            os.writeFromLSB(version_number, 5);
            os.writeFromLSB(current_next_indicator, 1);
            os.writeFromLSB(section_number, 8);
            os.writeFromLSB(last_section_number, 8);
        }
        os.write(getPrivateDataBytes());
        if (getSectionSyntaxIndicator() != 0) {
            Crc32Mpeg2 crc = new Crc32Mpeg2();
            byte[] byte_array = os.toByteArray();
            crc.update(byte_array, 0, byte_array.length - 4);
            os.writeFromLSB((int) (crc.getValue() & 0x00000000FFFFFFFF), 32);
        }
        return os.toByteArray();
    }
