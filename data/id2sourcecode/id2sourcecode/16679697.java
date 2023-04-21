        private float getValueImpl() {
            int nChannel = getChannel();
            float fValue = 0.0F;
            switch(getDirection()) {
                case DIRECTION_COMMON:
                case DIRECTION_PLAYBACK:
                    fValue = getElement().getPlaybackVolume(nChannel);
                    break;
                case DIRECTION_CAPTURE:
                    fValue = getElement().getCaptureVolume(nChannel);
                    break;
            }
            return fValue;
        }
