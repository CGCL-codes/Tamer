    public GetFrameRate() throws V4L4JException {
        try {
            vd = new VideoDevice(dev);
            imfList = vd.getDeviceInfo().getFormatList();
            if (outFmt == 0) getRawFg(); else if (outFmt == 1 && vd.supportJPEGConversion()) getJPEGfg(); else if (outFmt == 2 && vd.supportRGBConversion()) getRGBfg(); else if (outFmt == 3 && vd.supportBGRConversion()) getBGRfg(); else if (outFmt == 4 && vd.supportYUVConversion()) getYUVfg(); else if (outFmt == 5 && vd.supportYVUConversion()) getYVUfg(); else {
                System.out.println("Unknown / unsupported output format: " + outFmt);
                throw new V4L4JException("unknown / unsupported output format");
            }
            System.out.println("Input image format: " + fg.getImageFormat().getName());
        } catch (V4L4JException e) {
            e.printStackTrace();
            System.out.println("Failed to instanciate the FrameGrabber (" + dev + ")");
            vd.release();
            throw e;
        }
        width = fg.getWidth();
        height = fg.getHeight();
        std = fg.getStandard();
        channel = fg.getChannel();
    }
