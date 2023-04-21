    public synchronized void createTone() throws Exception {
        if (toneCreated) {
            return;
        }
        if (ringToneSampleRate < 0 || samplesPerFrame < 0) {
            return;
        }
        int toneDescCount = 0;
        for (RingtoneSpecification ringSpec : ringParams) {
            toneDescCount += ringSpec.ringCount;
        }
        final pjmedia_tone_desc tones[] = new pjmedia_tone_desc[toneDescCount];
        ringPort = new pjmedia_port();
        int status = PjsuaClient.pjsuaWorker.syncExec(new Callable<Integer>() {

            @Override
            public Integer call() {
                return PjsuaClient.pjmedia_tonegen_create2(memoryPool, pjsua.pj_str_copy("ring"), ringToneSampleRate, mediaConfig.getChannel_count(), samplesPerFrame, DEFAULT_BITS_PER_SAMPLE, pjsuaConstants.PJMEDIA_TONEGEN_LOOP, ringPort);
            }
        });
        if (status != pjsuaConstants.PJ_SUCCESS) {
            ringPort = null;
            throw new Exception("Creating the ringtone failed because pjmedia_tonegen_create2 returned " + status);
        }
        int toneIndex = 0;
        for (RingtoneSpecification ringSpec : ringParams) {
            for (int i = 0; i < ringSpec.ringCount; ++i) {
                pjmedia_tone_desc tone = new pjmedia_tone_desc();
                tone.setFreq1(ringSpec.freq1);
                tone.setFreq2(ringSpec.freq2);
                tone.setOn_msec(ringSpec.onMs);
                tone.setOff_msec(ringSpec.offMs);
                tone.setVolume(ringSpec.volume);
                tones[toneIndex] = tone;
                toneIndex++;
            }
        }
        tones[toneDescCount - 1].setOff_msec(ringParams.get(ringParams.size() - 1).intervalMs);
        final int finalToneDescCount = toneDescCount;
        status = PjsuaClient.pjsuaWorker.syncExec(new Callable<Integer>() {

            @Override
            public Integer call() {
                return PjsuaClient.pjmedia_tonegen_play(ringPort, finalToneDescCount, tones, pjsuaConstants.PJMEDIA_TONEGEN_LOOP);
            }
        });
        createToneExtended();
        toneCreated = true;
    }
