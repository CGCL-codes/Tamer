            @Override
            public Integer call() {
                return PjsuaClient.pjmedia_tonegen_create2(memoryPool, pjsua.pj_str_copy("ring"), ringToneSampleRate, mediaConfig.getChannel_count(), samplesPerFrame, DEFAULT_BITS_PER_SAMPLE, pjsuaConstants.PJMEDIA_TONEGEN_LOOP, ringPort);
            }
