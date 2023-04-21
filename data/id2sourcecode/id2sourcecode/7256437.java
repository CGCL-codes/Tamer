    public void test_com_sun_media_format_WavAudioFormat() throws Exception {
        assertEquals(com.sun.media.format.WavAudioFormat.class.getModifiers(), 1);
        assertTrue(!com.sun.media.format.WavAudioFormat.class.isInterface());
        assertTrue(com.sun.media.format.WavAudioFormat.class.getSuperclass().equals(javax.media.format.AudioFormat.class));
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_PCM == 1);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_ADPCM == 2);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_ALAW == 6);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_MULAW == 7);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_OKI_ADPCM == 16);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_DIGISTD == 21);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_DIGIFIX == 22);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_GSM610 == 49);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_IBM_FORMAT_MULAW == 257);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_IBM_FORMAT_ALAW == 258);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_IBM_FORMAT_ADPCM == 259);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_DVI_ADPCM == 17);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_SX7383 == 7175);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_DSPGROUP_TRUESPEECH == 34);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_MSNAUDIO == 50);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_MSG723 == 66);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_MPEG_LAYER3 == 85);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_AC8 == 112);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_AC10 == 113);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_AC16 == 114);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_AC20 == 115);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_METAVOICE == 116);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_METASOUND == 117);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_RT29H == 118);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_VR12 == 119);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_VR18 == 120);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_TQ40 == 121);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_VOXWARE_TQ60 == 129);
        assertTrue(com.sun.media.format.WavAudioFormat.WAVE_FORMAT_MSRT24 == 130);
        assertTrue(AudioFormat.BIG_ENDIAN == 1);
        assertTrue(AudioFormat.LITTLE_ENDIAN == 0);
        assertTrue(AudioFormat.SIGNED == 1);
        assertTrue(AudioFormat.UNSIGNED == 0);
        assertTrue(AudioFormat.LINEAR.equals("LINEAR"));
        assertTrue(AudioFormat.ULAW.equals("ULAW"));
        assertTrue(AudioFormat.ULAW_RTP.equals("ULAW/rtp"));
        assertTrue(AudioFormat.ALAW.equals("alaw"));
        assertTrue(AudioFormat.IMA4.equals("ima4"));
        assertTrue(AudioFormat.IMA4_MS.equals("ima4/ms"));
        assertTrue(AudioFormat.MSADPCM.equals("msadpcm"));
        assertTrue(AudioFormat.DVI.equals("dvi"));
        assertTrue(AudioFormat.DVI_RTP.equals("dvi/rtp"));
        assertTrue(AudioFormat.G723.equals("g723"));
        assertTrue(AudioFormat.G723_RTP.equals("g723/rtp"));
        assertTrue(AudioFormat.G728.equals("g728"));
        assertTrue(AudioFormat.G728_RTP.equals("g728/rtp"));
        assertTrue(AudioFormat.G729.equals("g729"));
        assertTrue(AudioFormat.G729_RTP.equals("g729/rtp"));
        assertTrue(AudioFormat.G729A.equals("g729a"));
        assertTrue(AudioFormat.G729A_RTP.equals("g729a/rtp"));
        assertTrue(AudioFormat.GSM.equals("gsm"));
        assertTrue(AudioFormat.GSM_MS.equals("gsm/ms"));
        assertTrue(AudioFormat.GSM_RTP.equals("gsm/rtp"));
        assertTrue(AudioFormat.MAC3.equals("MAC3"));
        assertTrue(AudioFormat.MAC6.equals("MAC6"));
        assertTrue(AudioFormat.TRUESPEECH.equals("truespeech"));
        assertTrue(AudioFormat.MSNAUDIO.equals("msnaudio"));
        assertTrue(AudioFormat.MPEGLAYER3.equals("mpeglayer3"));
        assertTrue(AudioFormat.VOXWAREAC8.equals("voxwareac8"));
        assertTrue(AudioFormat.VOXWAREAC10.equals("voxwareac10"));
        assertTrue(AudioFormat.VOXWAREAC16.equals("voxwareac16"));
        assertTrue(AudioFormat.VOXWAREAC20.equals("voxwareac20"));
        assertTrue(AudioFormat.VOXWAREMETAVOICE.equals("voxwaremetavoice"));
        assertTrue(AudioFormat.VOXWAREMETASOUND.equals("voxwaremetasound"));
        assertTrue(AudioFormat.VOXWARERT29H.equals("voxwarert29h"));
        assertTrue(AudioFormat.VOXWAREVR12.equals("voxwarevr12"));
        assertTrue(AudioFormat.VOXWAREVR18.equals("voxwarevr18"));
        assertTrue(AudioFormat.VOXWARETQ40.equals("voxwaretq40"));
        assertTrue(AudioFormat.VOXWARETQ60.equals("voxwaretq60"));
        assertTrue(AudioFormat.MSRT24.equals("msrt24"));
        assertTrue(AudioFormat.MPEG.equals("mpegaudio"));
        assertTrue(AudioFormat.MPEG_RTP.equals("mpegaudio/rtp"));
        assertTrue(AudioFormat.DOLBYAC3.equals("dolbyac3"));
        assertTrue(Format.NOT_SPECIFIED == -1);
        assertTrue(Format.TRUE == 1);
        assertTrue(Format.FALSE == 0);
        if (true) {
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("clone", new Class[] {});
                assertEquals(m.getReturnType(), java.lang.Object.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("equals", new Class[] { java.lang.Object.class });
                assertEquals(m.getReturnType(), boolean.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("matches", new Class[] { javax.media.Format.class });
                assertEquals(m.getReturnType(), boolean.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("intersects", new Class[] { javax.media.Format.class });
                assertEquals(m.getReturnType(), javax.media.Format.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getAverageBytesPerSecond", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getCodecSpecificHeader", new Class[] {});
                assertEquals(m.getReturnType(), byte[].class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("toString", new Class[] {});
                assertEquals(m.getReturnType(), java.lang.String.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getSampleRate", new Class[] {});
                assertEquals(m.getReturnType(), double.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getSampleSizeInBits", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getChannels", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getEndian", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getSigned", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getFrameSizeInBits", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getFrameRate", new Class[] {});
                assertEquals(m.getReturnType(), double.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("computeDuration", new Class[] { long.class });
                assertEquals(m.getReturnType(), long.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getEncoding", new Class[] {});
                assertEquals(m.getReturnType(), java.lang.String.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getDataType", new Class[] {});
                assertEquals(m.getReturnType(), java.lang.Class.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("isSameEncoding", new Class[] { javax.media.Format.class });
                assertEquals(m.getReturnType(), boolean.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("isSameEncoding", new Class[] { java.lang.String.class });
                assertEquals(m.getReturnType(), boolean.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("relax", new Class[] {});
                assertEquals(m.getReturnType(), javax.media.Format.class);
                assertEquals(m.getModifiers(), 1);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("hashCode", new Class[] {});
                assertEquals(m.getReturnType(), int.class);
                assertEquals(m.getModifiers(), 257);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("getClass", new Class[] {});
                assertEquals(m.getReturnType(), java.lang.Class.class);
                assertEquals(m.getModifiers(), 273);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("wait", new Class[] { long.class });
                assertEquals(m.getReturnType(), void.class);
                assertEquals(m.getModifiers(), 273);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("wait", new Class[] { long.class, int.class });
                assertEquals(m.getReturnType(), void.class);
                assertEquals(m.getModifiers(), 17);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("wait", new Class[] {});
                assertEquals(m.getReturnType(), void.class);
                assertEquals(m.getModifiers(), 17);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("notify", new Class[] {});
                assertEquals(m.getReturnType(), void.class);
                assertEquals(m.getModifiers(), 273);
            }
            {
                final Method m = com.sun.media.format.WavAudioFormat.class.getMethod("notifyAll", new Class[] {});
                assertEquals(m.getReturnType(), void.class);
                assertEquals(m.getModifiers(), 273);
            }
        }
        if (true) {
            {
                final Constructor c = com.sun.media.format.WavAudioFormat.class.getConstructor(new Class[] { java.lang.String.class });
                assertEquals(c.getModifiers(), 1);
            }
            {
                final Constructor c = com.sun.media.format.WavAudioFormat.class.getConstructor(new Class[] { java.lang.String.class, double.class, int.class, int.class, int.class, int.class, byte[].class });
                assertEquals(c.getModifiers(), 1);
            }
            {
                final Constructor c = com.sun.media.format.WavAudioFormat.class.getConstructor(new Class[] { java.lang.String.class, double.class, int.class, int.class, int.class, int.class, int.class, int.class, float.class, java.lang.Class.class, byte[].class });
                assertEquals(c.getModifiers(), 1);
            }
        }
        if (false) {
            com.sun.media.format.WavAudioFormat o = null;
            o.clone();
            o.equals((java.lang.Object) null);
            o.matches((javax.media.Format) null);
            o.intersects((javax.media.Format) null);
            o.getAverageBytesPerSecond();
            o.getCodecSpecificHeader();
            o.toString();
            o.getSampleRate();
            o.getSampleSizeInBits();
            o.getChannels();
            o.getEndian();
            o.getSigned();
            o.getFrameSizeInBits();
            o.getFrameRate();
            o.computeDuration(0L);
            o.getEncoding();
            o.getDataType();
            o.isSameEncoding((javax.media.Format) null);
            o.isSameEncoding((java.lang.String) null);
            o.relax();
            o.hashCode();
            o.getClass();
            o.wait(0L);
            o.wait(0L, 0);
            o.wait();
            o.notify();
            o.notifyAll();
        }
        if (false) {
            com.sun.media.format.WavAudioFormat o = null;
            new com.sun.media.format.WavAudioFormat((java.lang.String) null);
            new com.sun.media.format.WavAudioFormat((java.lang.String) null, 0.0, 0, 0, 0, 0, (byte[]) null);
            new com.sun.media.format.WavAudioFormat((java.lang.String) null, 0.0, 0, 0, 0, 0, 0, 0, 0.f, (java.lang.Class) null, (byte[]) null);
        }
    }
