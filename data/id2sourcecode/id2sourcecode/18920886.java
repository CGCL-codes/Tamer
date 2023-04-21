        private void calcVolume() {
            if (getFormat() == null) {
                return;
            }
            if (muteControl.getValue()) {
                leftGain = 0.0f;
                rightGain = 0.0f;
                return;
            }
            float gain = gainControl.getLinearGain();
            if (getFormat().getChannels() == 1) {
                leftGain = gain;
                rightGain = gain;
            } else {
                float bal = balanceControl.getValue();
                if (bal < 0.0f) {
                    leftGain = gain;
                    rightGain = gain * (bal + 1.0f);
                } else {
                    leftGain = gain * (1.0f - bal);
                    rightGain = gain;
                }
            }
        }
