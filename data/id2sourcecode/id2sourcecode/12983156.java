    private void discretizeChannel(ChannelMap map) {
        int channelResolution = FluxSettings.getChannelResolution();
        FluxChannelMap chachedMap = getCachedChannelMap(map);
        if (chachedMap == null) {
            Environment env = new Environment();
            if (!(env.userObject instanceof ChannelMapInput)) {
                env.userObjectOwner = map;
                env.userObject = new ChannelMapInput();
            }
            ChannelData src = ((ChannelMapInput) env.userObject).getUserData(map, null), sink = src.createSink(map);
            env.normal.set(0, 0, 1);
            env.dpdu.set(1, 0, 0);
            env.dpdv.set(0, 1, 0);
            env.solid = true;
            Point3f image[][] = new Point3f[channelResolution][channelResolution];
            ChannelData cd = sink.getData(map);
            src.setFloat(Channel.IOR, env.iorRatio);
            src.setTuple3f(Channel.DPXDU, env.dpdu);
            src.setTuple3f(Channel.DPXDV, env.dpdv);
            for (int x = 0; x < channelResolution; x++) for (int y = 0; y < channelResolution; y++) {
                float u = x / (float) channelResolution;
                float v = y / (float) channelResolution;
                LightDistribution.map2cartesian(env.point, u, v);
                env.localPoint.set(env.point);
                env.normal.set(env.point);
                env.uv.set(u, v);
                src.setTuple3f(Channel.PX, env.point);
                src.setTuple3f(Channel.X, env.localPoint);
                src.setTuple3f(Channel.NX, env.normal);
                src.setTuple2f(Channel.U, env.uv);
                image[y][x] = new Point3f();
                cd.getTuple3f(image[y][x], sink, Channel.R);
            }
            addCachedChannelMap(new FluxHDRImageMap(image), map, map);
        } else {
            currentChannelNode = chachedMap;
        }
    }
