    private int getConversionType(int sourceType, int sourceChannels, int targetType, int targetChannels) {
        if (sourceType == 0 || targetType == 0 || (sourceChannels != 1 && targetChannels != 1 && targetChannels != sourceChannels)) {
            return CONVERT_NOT_POSSIBLE;
        }
        if (sourceType == targetType) {
            if (sourceChannels == targetChannels) {
                return CONVERT_NONE;
            } else if (sourceChannels == 1 && targetChannels > 1) {
                return CONVERT_ONLY_EXPAND_CHANNELS;
            }
        }
        if (!ONLY_FLOAT_CONVERSION && (sourceChannels == 1 && targetChannels >= 1 || sourceChannels == targetChannels)) {
            if ((sourceType == UNSIGNED8 && targetType == SIGNED8) || (sourceType == SIGNED8 && targetType == UNSIGNED8)) {
                return CONVERT_SIGN;
            } else if ((sourceType == BIG_ENDIAN16 && targetType == LITTLE_ENDIAN16) || (sourceType == LITTLE_ENDIAN16 && targetType == BIG_ENDIAN16)) {
                return CONVERT_BYTE_ORDER16;
            } else if ((sourceType == BIG_ENDIAN24 && targetType == LITTLE_ENDIAN24) || (sourceType == LITTLE_ENDIAN24 && targetType == BIG_ENDIAN24)) {
                return CONVERT_BYTE_ORDER24;
            } else if ((sourceType == BIG_ENDIAN32 && targetType == LITTLE_ENDIAN32) || (sourceType == LITTLE_ENDIAN32 && targetType == BIG_ENDIAN32)) {
                return CONVERT_BYTE_ORDER32;
            } else if (sourceType == SIGNED8 && targetType == LITTLE_ENDIAN16) {
                return CONVERT_8STO16L;
            } else if (sourceType == SIGNED8 && targetType == BIG_ENDIAN16) {
                return CONVERT_8STO16B;
            } else if (sourceType == UNSIGNED8 && targetType == LITTLE_ENDIAN16) {
                return CONVERT_8UTO16L;
            } else if (sourceType == UNSIGNED8 && targetType == BIG_ENDIAN16) {
                return CONVERT_8UTO16B;
            }
        }
        return CONVERT_FLOAT;
    }
