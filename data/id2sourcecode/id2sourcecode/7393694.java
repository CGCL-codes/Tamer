    @Override
    public void updateSourceParam(AudioNode src, AudioParam param) {
        if (audioDisabled) {
            return;
        }
        if (src.getChannel() < 0) {
            return;
        }
        switch(param) {
            case Position:
                if (!src.isPositional()) {
                    return;
                }
                Vector3f pos = src.getWorldTranslation();
                break;
            case Velocity:
                if (!src.isPositional()) {
                    return;
                }
                Vector3f vel = src.getVelocity();
                break;
            case MaxDistance:
                if (!src.isPositional()) {
                    return;
                }
                break;
            case RefDistance:
                if (!src.isPositional()) {
                    return;
                }
                break;
            case ReverbFilter:
                if (!src.isPositional() || !src.isReverbEnabled()) {
                    return;
                }
                break;
            case ReverbEnabled:
                if (!src.isPositional()) {
                    return;
                }
                if (src.isReverbEnabled()) {
                    updateSourceParam(src, AudioParam.ReverbFilter);
                }
                break;
            case IsPositional:
                break;
            case Direction:
                if (!src.isDirectional()) {
                    return;
                }
                Vector3f dir = src.getDirection();
                break;
            case InnerAngle:
                if (!src.isDirectional()) {
                    return;
                }
                break;
            case OuterAngle:
                if (!src.isDirectional()) {
                    return;
                }
                break;
            case IsDirectional:
                if (src.isDirectional()) {
                    updateSourceParam(src, AudioParam.Direction);
                    updateSourceParam(src, AudioParam.InnerAngle);
                    updateSourceParam(src, AudioParam.OuterAngle);
                } else {
                }
                break;
            case DryFilter:
                if (src.getDryFilter() != null) {
                    Filter f = src.getDryFilter();
                    if (f.isUpdateNeeded()) {
                    }
                }
                break;
            case Looping:
                if (src.isLooping()) {
                }
                break;
            case Volume:
                MediaPlayer mp = musicPlaying.get(src);
                if (mp != null) {
                    mp.setVolume(src.getVolume(), src.getVolume());
                } else {
                    soundPool.setVolume(src.getChannel(), src.getVolume(), src.getVolume());
                }
                break;
            case Pitch:
                break;
        }
    }
