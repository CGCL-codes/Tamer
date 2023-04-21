        public void format(PCMAudioFile adb, XmlElement xml) {
            xml.setAttribute("pcmFile", adb.getPcmFile());
            xml.setAttribute("bitsPerSample", adb.getBitsPerSample());
            xml.setAttribute("samplesPerSecond", adb.getSamplesPerSecond());
            xml.setAttribute("channelCount", adb.getChannelCount());
        }
