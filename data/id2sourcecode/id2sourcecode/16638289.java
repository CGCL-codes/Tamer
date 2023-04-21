    public void testParser() throws UserException {
        File f = new File("G:\\Projects\\spindles\\data\\nikas\\nikas1");
        EEGParser p = new EEGParser(f, "C3");
        System.out.println(p.getChannelColumn());
        System.out.println(p.getChannel());
        System.out.println(p.getFirstName());
        System.out.println(p.getLastName());
        System.out.println(p.getSamplingRate().value());
        System.out.println(p.getSessionStartDate());
        System.out.println(p.getPartStartDate());
        f = new File("G:\\Projects\\spindles\\data\\nikas\\dummy");
        p = new EEGParser(f, "C3");
    }
