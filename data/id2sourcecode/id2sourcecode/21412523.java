    public void decode(BitStream in) throws AACException {
        final int start = in.getPosition();
        int type;
        Element prev = null;
        boolean content = true;
        if (!config.getProfile().isErrorResilientProfile()) {
            while (content && (type = in.readBits(3)) != ELEMENT_END) {
                switch(type) {
                    case ELEMENT_SCE:
                    case ELEMENT_LFE:
                        LOGGER.finest("SCE");
                        prev = decodeSCE_LFE(in);
                        break;
                    case ELEMENT_CPE:
                        LOGGER.finest("CPE");
                        prev = decodeCPE(in);
                        break;
                    case ELEMENT_CCE:
                        LOGGER.finest("CCE");
                        decodeCCE(in);
                        prev = null;
                        break;
                    case ELEMENT_DSE:
                        LOGGER.finest("DSE");
                        decodeDSE(in);
                        prev = null;
                        break;
                    case ELEMENT_PCE:
                        LOGGER.finest("PCE");
                        decodePCE(in);
                        prev = null;
                        break;
                    case ELEMENT_FIL:
                        LOGGER.finest("FIL");
                        decodeFIL(in, prev);
                        prev = null;
                        break;
                }
            }
            LOGGER.finest("END");
            content = false;
            prev = null;
        } else {
            switch(config.getChannelConfiguration()) {
                case CHANNEL_CONFIG_MONO:
                    decodeSCE_LFE(in);
                    break;
                case CHANNEL_CONFIG_STEREO:
                    decodeCPE(in);
                    break;
                case CHANNEL_CONFIG_STEREO_PLUS_CENTER:
                    decodeSCE_LFE(in);
                    decodeCPE(in);
                    break;
                case CHANNEL_CONFIG_STEREO_PLUS_CENTER_PLUS_REAR_MONO:
                    decodeSCE_LFE(in);
                    decodeCPE(in);
                    decodeSCE_LFE(in);
                    break;
                case CHANNEL_CONFIG_FIVE:
                    decodeSCE_LFE(in);
                    decodeCPE(in);
                    decodeCPE(in);
                    break;
                case CHANNEL_CONFIG_FIVE_PLUS_ONE:
                    decodeSCE_LFE(in);
                    decodeCPE(in);
                    decodeCPE(in);
                    decodeSCE_LFE(in);
                    break;
                case CHANNEL_CONFIG_SEVEN_PLUS_ONE:
                    decodeSCE_LFE(in);
                    decodeCPE(in);
                    decodeCPE(in);
                    decodeCPE(in);
                    decodeSCE_LFE(in);
                    break;
                default:
                    throw new AACException("unsupported channel configuration for error resilience: " + config.getChannelConfiguration());
            }
        }
        in.byteAlign();
        bitsRead = in.getPosition() - start;
    }
