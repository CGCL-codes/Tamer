    public static void main(String[] args) {
        args = Gst.init("ColorBalance video test", args);
        Pipeline pipe = new Pipeline("pipeline");
        final Element videosrc = ElementFactory.make("v4l2src", "source");
        videosrc.set("device", "/dev/video0");
        final Element videosink = ElementFactory.make("xvimagesink", "xv");
        pipe.addMany(videosrc, videosink);
        Element.linkMany(videosrc, videosink);
        pipe.play();
        Tuner tun = Tuner.wrap(videosrc);
        List<TunerNorm> normList = tun.getNormList();
        for (TunerNorm n : normList) {
            System.out.println("Available norm: " + n.getLabel());
        }
        List<TunerChannel> chList = tun.getChannelList();
        for (TunerChannel ch : chList) {
            System.out.println("Channel [" + ch.getLabel() + "]: " + ch.isTuningChannel());
        }
        for (int i = 0; ; i++) {
            tun.setChannel(chList.get(i % chList.size()));
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
