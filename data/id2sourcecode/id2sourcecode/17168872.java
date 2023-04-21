    public int pcm_seek(long pos) {
        int link = -1;
        long total = pcm_total(-1);
        if (!seekable) return (-1);
        if (pos < 0 || pos > total) {
            pcm_offset = -1;
            decode_clear();
            return -1;
        }
        for (link = links - 1; link >= 0; link--) {
            total -= pcmlengths[link];
            if (pos >= total) break;
        }
        {
            long target = pos - total;
            long end = offsets[link + 1];
            long begin = offsets[link];
            int best = (int) begin;
            Page og = new Page();
            while (begin < end) {
                long bisect;
                int ret;
                if (end - begin < CHUNKSIZE) {
                    bisect = begin;
                } else {
                    bisect = (end + begin) / 2;
                }
                seek_helper(bisect);
                ret = get_next_page(og, end - bisect);
                if (ret == -1) {
                    end = bisect;
                } else {
                    long granulepos = og.granulepos();
                    if (granulepos < target) {
                        best = ret;
                        begin = offset;
                    } else {
                        end = bisect;
                    }
                }
            }
            if (raw_seek(best) != 0) {
                pcm_offset = -1;
                decode_clear();
                return -1;
            }
        }
        if (pcm_offset >= pos) {
            pcm_offset = -1;
            decode_clear();
            return -1;
        }
        if (pos > pcm_total(-1)) {
            pcm_offset = -1;
            decode_clear();
            return -1;
        }
        while (pcm_offset < pos) {
            float[][] pcm;
            int target = (int) (pos - pcm_offset);
            float[][][] _pcm = new float[1][][];
            int[] _index = new int[getInfo(-1).channels];
            int samples = vd.synthesis_pcmout(_pcm, _index);
            pcm = _pcm[0];
            if (samples > target) samples = target;
            vd.synthesis_read(samples);
            pcm_offset += samples;
            if (samples < target) if (process_packet(1) == 0) {
                pcm_offset = pcm_total(-1);
            }
        }
        return 0;
    }
