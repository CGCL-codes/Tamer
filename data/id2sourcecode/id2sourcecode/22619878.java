    public void updateSourceParam(AudioNode src, AudioParam param) {
        checkDead();
        synchronized (threadLock) {
            while (!threadLock.get()) {
                try {
                    threadLock.wait();
                } catch (InterruptedException ex) {
                }
            }
            if (audioDisabled) return;
            if (src.getChannel() < 0) return;
            assert src.getChannel() >= 0;
            int id = channels[src.getChannel()];
            switch(param) {
                case Position:
                    if (!src.isPositional()) return;
                    Vector3f pos = src.getWorldTranslation();
                    alSource3f(id, AL_POSITION, pos.x, pos.y, pos.z);
                    break;
                case Velocity:
                    if (!src.isPositional()) return;
                    Vector3f vel = src.getVelocity();
                    alSource3f(id, AL_VELOCITY, vel.x, vel.y, vel.z);
                    break;
                case MaxDistance:
                    if (!src.isPositional()) return;
                    alSourcef(id, AL_MAX_DISTANCE, src.getMaxDistance());
                    break;
                case RefDistance:
                    if (!src.isPositional()) return;
                    alSourcef(id, AL_REFERENCE_DISTANCE, src.getRefDistance());
                    break;
                case ReverbFilter:
                    if (!supportEfx || !src.isPositional() || !src.isReverbEnabled()) return;
                    int filter = EFX10.AL_FILTER_NULL;
                    if (src.getReverbFilter() != null) {
                        Filter f = src.getReverbFilter();
                        if (f.isUpdateNeeded()) {
                            updateFilter(f);
                        }
                        filter = f.getId();
                    }
                    AL11.alSource3i(id, EFX10.AL_AUXILIARY_SEND_FILTER, reverbFxSlot, 0, filter);
                    break;
                case ReverbEnabled:
                    if (!supportEfx || !src.isPositional()) return;
                    if (src.isReverbEnabled()) {
                        updateSourceParam(src, AudioParam.ReverbFilter);
                    } else {
                        AL11.alSource3i(id, EFX10.AL_AUXILIARY_SEND_FILTER, 0, 0, EFX10.AL_FILTER_NULL);
                    }
                    break;
                case IsPositional:
                    if (!src.isPositional()) {
                        alSourcei(id, AL_SOURCE_RELATIVE, AL_TRUE);
                        alSource3f(id, AL_POSITION, 0, 0, 0);
                        alSource3f(id, AL_VELOCITY, 0, 0, 0);
                    } else {
                        alSourcei(id, AL_SOURCE_RELATIVE, AL_FALSE);
                        updateSourceParam(src, AudioParam.Position);
                        updateSourceParam(src, AudioParam.Velocity);
                        updateSourceParam(src, AudioParam.MaxDistance);
                        updateSourceParam(src, AudioParam.RefDistance);
                        updateSourceParam(src, AudioParam.ReverbEnabled);
                    }
                    break;
                case Direction:
                    if (!src.isDirectional()) return;
                    Vector3f dir = src.getDirection();
                    alSource3f(id, AL_DIRECTION, dir.x, dir.y, dir.z);
                    break;
                case InnerAngle:
                    if (!src.isDirectional()) return;
                    alSourcef(id, AL_CONE_INNER_ANGLE, src.getInnerAngle());
                    break;
                case OuterAngle:
                    if (!src.isDirectional()) return;
                    alSourcef(id, AL_CONE_OUTER_ANGLE, src.getOuterAngle());
                    break;
                case IsDirectional:
                    if (src.isDirectional()) {
                        updateSourceParam(src, AudioParam.Direction);
                        updateSourceParam(src, AudioParam.InnerAngle);
                        updateSourceParam(src, AudioParam.OuterAngle);
                        alSourcef(id, AL_CONE_OUTER_GAIN, 0);
                    } else {
                        alSourcef(id, AL_CONE_INNER_ANGLE, 360);
                        alSourcef(id, AL_CONE_OUTER_ANGLE, 360);
                        alSourcef(id, AL_CONE_OUTER_GAIN, 1f);
                    }
                    break;
                case DryFilter:
                    if (!supportEfx) return;
                    if (src.getDryFilter() != null) {
                        Filter f = src.getDryFilter();
                        if (f.isUpdateNeeded()) {
                            updateFilter(f);
                            alSourcei(id, EFX10.AL_DIRECT_FILTER, f.getId());
                        }
                    } else {
                        alSourcei(id, EFX10.AL_DIRECT_FILTER, EFX10.AL_FILTER_NULL);
                    }
                    break;
                case Looping:
                    if (src.isLooping()) {
                        if (!(src.getAudioData() instanceof AudioStream)) {
                            alSourcei(id, AL_LOOPING, AL_TRUE);
                        }
                    } else {
                        alSourcei(id, AL_LOOPING, AL_FALSE);
                    }
                    break;
                case Volume:
                    alSourcef(id, AL_GAIN, src.getVolume());
                    break;
                case Pitch:
                    alSourcef(id, AL_PITCH, src.getPitch());
                    break;
            }
        }
    }
