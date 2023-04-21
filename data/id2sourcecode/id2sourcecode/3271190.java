        public void open(ModelWavetable osc, float outputsamplerate) throws IOException {
            eof = false;
            nrofchannels = osc.getChannels();
            if (ibuffer.length < nrofchannels) {
                ibuffer = new float[nrofchannels][sector_size + pad2];
            }
            stream = osc.openStream();
            streampos = 0;
            stream_eof = false;
            pitchcorrection = osc.getPitchcorrection();
            samplerateconv = stream.getFormat().getSampleRate() / outputsamplerate;
            looplen = osc.getLoopLength();
            loopstart = osc.getLoopStart();
            sector_loopstart = (int) (loopstart / sector_size);
            sector_loopstart = sector_loopstart - 1;
            sector_pos = 0;
            if (sector_loopstart < 0) sector_loopstart = 0;
            started = false;
            loopmode = osc.getLoopType();
            if (loopmode != 0) {
                markset = false;
                marklimit = nrofchannels * (int) (looplen + pad2 + 1);
            } else markset = true;
            target_pitch = samplerateconv;
            current_pitch[0] = samplerateconv;
            ibuffer_order = true;
            loopdirection = true;
            noteOff_flag = false;
            for (int i = 0; i < nrofchannels; i++) Arrays.fill(ibuffer[i], sector_size, sector_size + pad2, 0);
            ix[0] = pad;
            eof = false;
            ix[0] = sector_size + pad;
            sector_pos = -1;
            streampos = -sector_size;
            nextBuffer();
        }
