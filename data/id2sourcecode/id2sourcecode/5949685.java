    public int getFrameLength() {
        switch(version) {
            case VERSION_2:
            case VERSION_2_5:
                switch(layer) {
                    case LAYER_I:
                        return (LAYER_I_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength()) * LAYER_I_SLOT_SIZE;
                    case LAYER_II:
                        if (this.getChannelMode() == MODE_MONO) {
                            return (LAYER_II_FRAME_SIZE_COEFFICIENT / 2) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_II_SLOT_SIZE;
                        } else {
                            return (LAYER_II_FRAME_SIZE_COEFFICIENT) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_II_SLOT_SIZE;
                        }
                    case LAYER_III:
                        if (this.getChannelMode() == MODE_MONO) {
                            return (LAYER_III_FRAME_SIZE_COEFFICIENT / 2) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;
                        } else {
                            return (LAYER_III_FRAME_SIZE_COEFFICIENT) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;
                        }
                    default:
                        throw new RuntimeException("Mp3 Unknown Layer:" + layer);
                }
            case VERSION_1:
                switch(layer) {
                    case LAYER_I:
                        return (LAYER_I_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength()) * LAYER_I_SLOT_SIZE;
                    case LAYER_II:
                        return LAYER_II_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_II_SLOT_SIZE;
                    case LAYER_III:
                        return LAYER_III_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;
                    default:
                        throw new RuntimeException("Mp3 Unknown Layer:" + layer);
                }
            default:
                throw new RuntimeException("Mp3 Unknown Version:" + version);
        }
    }
