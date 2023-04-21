    public void setAudioFormat(AudioFormat audio_format) {
        if (audio_format != null) {
            float sample_rate = audio_format.getSampleRate();
            if (sample_rate == 8000.0F) sr_8000_rb.setSelected(true); else if (sample_rate == 11025.0F) sr_11025_rb.setSelected(true); else if (sample_rate == 16000.0F) sr_16000_rb.setSelected(true); else if (sample_rate == 22050.0F) sr_22050_rb.setSelected(true); else if (sample_rate == 44100.0F) sr_44100_rb.setSelected(true); else {
                sr_other_rb.setSelected(true);
                sr_text_area.setText((new Float(sample_rate * 1000.0F)).toString());
            }
            int bit_depth = audio_format.getSampleSizeInBits();
            if (bit_depth == 8) bd_8_rb.setSelected(true); else if (bit_depth == 16) bd_16_rb.setSelected(true); else {
                bd_other_rb.setSelected(true);
                bd_text_area.setText((new Integer(bit_depth)).toString());
            }
            int channels = audio_format.getChannels();
            if (channels == 1) chan_1_rb.setSelected(true); else if (channels == 2) chan_2_rb.setSelected(true); else {
                chan_other_rb.setSelected(true);
                chan_text_area.setText((new Integer(bit_depth)).toString());
            }
            AudioFormat.Encoding encoding = audio_format.getEncoding();
            if (encoding == AudioFormat.Encoding.PCM_SIGNED) signed_rb.setSelected(true); else if (encoding == AudioFormat.Encoding.PCM_UNSIGNED) unsigned_rb.setSelected(true);
            boolean is_big_endian = audio_format.isBigEndian();
            if (is_big_endian) big_endian_rb.setSelected(true); else little_endian_rb.setSelected(true);
        }
    }
