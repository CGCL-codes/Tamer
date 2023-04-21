    public static void main(String[] args) {
        args = Gst.init("ColorBalance video test", args);
        Pipeline pipe = new Pipeline("pipeline");
        final Element videosrc = ElementFactory.make("v4l2src", "source");
        videosrc.set("device", "/dev/video1");
        final Element videosink = ElementFactory.make("xvimagesink", "xv");
        pipe.addMany(videosrc, videosink);
        Element.linkMany(videosrc, videosink);
        pipe.play();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        VideoOrientation vo = VideoOrientation.wrap(videosrc);
        System.out.println("VideoOrientation: " + vo.getHflip(true));
        vo.setHflip(false);
        System.out.println("VideoOrientation: " + vo.getHflip(true));
        org.gstreamer.interfaces.ColorBalance cb = org.gstreamer.interfaces.ColorBalance.wrap(videosrc);
        List<ColorBalanceChannel> cbcList = cb.getChannelList();
        for (ColorBalanceChannel cbc : cbcList) {
            System.out.println("ColorBalance channels: " + cbc.getName() + " " + cbc.getMinValue() + " - " + cbc.getMaxValue());
        }
        for (int i = 0; i < 1000; ++i) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            cbcList.get(0).setValue(60000);
            cbcList.get(1).setValue(60000);
            cbcList.get(2).setValue(60000);
            cbcList.get(3).setValue(60000);
            switch(i % 4) {
                case 0:
                    cbcList.get(0).setValue(10000);
                    break;
                case 1:
                    cbcList.get(1).setValue(10000);
                    break;
                case 2:
                    cbcList.get(2).setValue(10000);
                    break;
                case 3:
                    cbcList.get(3).setValue(10000);
                    break;
            }
        }
    }
