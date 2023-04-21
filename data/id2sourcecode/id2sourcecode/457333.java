    private static void outputPlayback(AlsaMixerElement element) {
        out("  playback mono: " + element.isPlaybackMono());
        for (int nChannel = AlsaMixerElement.SND_MIXER_SCHN_FRONT_LEFT; nChannel <= AlsaMixerElement.SND_MIXER_SCHN_WOOFER; nChannel++) {
            out("  playback channel (" + AlsaMixerElement.getChannelName(nChannel) + "): " + element.hasPlaybackChannel(nChannel));
        }
        out("  common volume: " + element.hasCommonVolume());
        out("  playback volume: " + element.hasPlaybackVolume());
        out("  playback volume joined: " + element.hasPlaybackVolumeJoined());
        out("  common switch: " + element.hasCommonSwitch());
        out("  playback switch: " + element.hasPlaybackSwitch());
        out("  playback switch joined: " + element.hasPlaybackSwitchJoined());
    }
