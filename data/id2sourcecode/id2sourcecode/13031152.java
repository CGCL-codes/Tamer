    protected void readSampleData() {
        boolean read_ready = false;
        boolean write_ready = false;
        int mode = header.mode();
        int i;
        do {
            for (i = 0; i < num_subbands; ++i) read_ready = subbands[i].read_sampledata(stream);
            do {
                for (i = 0; i < num_subbands; ++i) write_ready = subbands[i].put_next_sample(which_channels, filter1, filter2);
                filter1.calculate_pcm_samples(buffer);
                if ((which_channels == OutputChannels.BOTH_CHANNELS) && (mode != Header.SINGLE_CHANNEL)) filter2.calculate_pcm_samples(buffer);
            } while (!write_ready);
        } while (!read_ready);
    }
