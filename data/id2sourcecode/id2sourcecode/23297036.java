    private static String buildDescription(AudioFormat f, long numFrames, Time length) {
        String retval = "";
        retval += "(* This file contains machine-readable information ";
        retval += "necessary to open the payload\n";
        retval += "   of this resource. *) \n\n";
        retval += "{SampleResource: \n";
        retval += "    {Encoding:         " + f.getEncoding() + "}\n";
        retval += "    {SampleRate:       " + f.getSampleRate() + "}\n";
        retval += "    {SampleSizeInBits: " + f.getSampleSizeInBits() + "}\n";
        retval += "    {Channels:         " + f.getChannels() + "}\n";
        retval += "    {FrameSize:        " + f.getFrameSize() + "}\n";
        retval += "    {FrameRate:        " + f.getFrameRate() + "}\n";
        retval += "    {BigEndian:        " + f.isBigEndian() + "}\n";
        retval += "    {LengthInFrames:   " + numFrames + "}\n";
        retval += "    {LengthInSeconds:  " + length.asSeconds() + "}\n";
        retval += "}\n\n";
        return retval;
    }
