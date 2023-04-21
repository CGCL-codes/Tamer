        public static Impl_SmdiSampleHeader getSampleHeader(byte[] data) {
            byte bpw = (byte) SMDIMsg.SampleHeader.getBitsPerWord(data);
            byte chnls = (byte) SMDIMsg.SampleHeader.getChannels(data);
            byte lc = (byte) SMDIMsg.SampleHeader.getLoopControl(data);
            int period = SMDIMsg.SampleHeader.getPeriodInNS(data);
            int length = SMDIMsg.SampleHeader.getSampleLength(data);
            int ls = SMDIMsg.SampleHeader.getLoopStart(data);
            int le = SMDIMsg.SampleHeader.getLoopEnd(data);
            short pitch = (short) SMDIMsg.SampleHeader.getSamplePitch(data);
            short pitchf = (short) SMDIMsg.SampleHeader.getSamplePitchFraction(data);
            String name = SMDIMsg.SampleHeader.getName(data);
            return new Impl_SmdiSampleHeader(true, bpw, chnls, lc, (byte) name.length(), period, length, ls, le, pitch, pitchf, name, 0);
        }
