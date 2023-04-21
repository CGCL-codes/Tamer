        public boolean mix(AudioBuffer buffer) {
            lfoVib.update();
            lfo1.update();
            lfo2.update();
            oscillator1.update();
            lpfOsc1Level = lpFilterMixerVars.getLevel(0);
            lpfOsc2Level = lpFilterMixerVars.getLevel(1);
            lpfOsc3Level = lpFilterMixerVars.getLevel(2);
            svfOsc1Level = svFilterMixerVars.getLevel(0);
            svfOsc2Level = svFilterMixerVars.getLevel(1);
            svfOsc3Level = svFilterMixerVars.getLevel(2);
            osc2Enabled = lpfOsc2Level + svfOsc2Level > 0.01f;
            if (osc2Enabled) oscillator2.update();
            osc3Enabled = lpfOsc3Level + svfOsc3Level > 0.01f;
            if (osc3Enabled) oscillator3.update();
            osc1WidthModDepths = osc1WidthModMixer.getDepths();
            osc2WidthModDepths = osc2WidthModMixer.getDepths();
            osc3WidthModDepths = osc3WidthModMixer.getDepths();
            lpfCutoffModDepths = lpfCutoffModMixer.getDepths();
            svfCutoffModDepths = svfCutoffModMixer.getDepths();
            vibModDepths = vibModMixer.getDepths();
            modSamples[4] = amplitude;
            modSamples[5] = getChannelPressure() / 128;
            modSamples[6] = getController(Controller.MODULATION) / 128;
            vibModPre = modSamples[5] * vibModDepths[1] + modSamples[6] * vibModDepths[2];
            lpfEnabled = lpfOsc1Level + lpfOsc2Level + lpfOsc3Level > 0.01f;
            if (lpfEnabled) {
                flpstatic = modulation(4, 3, lpfCutoffModDepths);
                flpstatic += lpFilter.update();
            }
            svfEnabled = svfOsc1Level + svfOsc2Level + svfOsc3Level > 0.01f;
            if (svfEnabled) {
                fsvstatic = modulation(4, 3, svfCutoffModDepths);
                fsvstatic += svFilter.update();
            }
            ampLevel = amplifierVars.getLevel() * ampT;
            return super.mix(buffer);
        }
