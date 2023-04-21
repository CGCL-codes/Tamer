        public void run() {
            now = (before + latency) % 10;
            try {
                tpo[now] = modPlayer.getPlayTempo();
                bpm[now] = modPlayer.getPlayBpm();
                pos[now] = modPlayer.getPlayPos();
                pat[now] = modPlayer.getPlayPat();
                time[now] = modPlayer.time() / 10;
                if (tpo[before] != oldTpo || bpm[before] != oldBpm || pos[before] != oldPos || pat[before] != oldPat) {
                    infoStatus.setText(String.format("Tempo:%02x BPM:%02x Pos:%02x Pat:%02x", tpo[before], bpm[before], pos[before], pat[before]));
                    oldTpo = tpo[before];
                    oldBpm = bpm[before];
                    oldPos = pos[before];
                    oldPat = pat[before];
                }
                if (time[before] != oldTime || showElapsed != oldShowElapsed) {
                    int t = time[before];
                    if (t < 0) t = 0;
                    if (showElapsed) {
                        elapsedTime.setText(String.format("%d:%02d", t / 60, t % 60));
                    } else {
                        t = totalTime - t;
                        elapsedTime.setText(String.format("-%d:%02d", t / 60, t % 60));
                    }
                    oldTime = time[before];
                    oldShowElapsed = showElapsed;
                }
                modPlayer.getChannelData(volumes[now], instruments[now], keys[now]);
                infoMeter.setVolumes(volumes[before]);
                if (showInsHighlight) instrumentList.setVolumes(volumes[before], instruments[before]);
                before++;
                if (before >= 10) before = 0;
            } catch (RemoteException e) {
            }
        }
