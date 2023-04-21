    private static void outputCapture(AlsaMixerElement element) {
        out("  capture mono: " + element.isCaptureMono());
        for (int nChannel = AlsaMixerElement.SND_MIXER_SCHN_FRONT_LEFT; nChannel <= AlsaMixerElement.SND_MIXER_SCHN_WOOFER; nChannel++) {
            out("  capture channel (" + AlsaMixerElement.getChannelName(nChannel) + "): " + element.hasCaptureChannel(nChannel));
        }
        out("  common volume: " + element.hasCommonVolume());
        out("  capture volume: " + element.hasCaptureVolume());
        out("  capture volume joined: " + element.hasCaptureVolumeJoined());
        out("  common switch: " + element.hasCommonSwitch());
        out("  capture switch: " + element.hasCaptureSwitch());
        out("  capture switch joined: " + element.hasCaptureSwitchJoinded());
        out("  capture switch exclusive: " + element.hasCaptureSwitchExclusive());
        if (element.hasCaptureSwitchExclusive()) {
            out("  capture group: " + element.getCaptureGroup());
        }
    }
