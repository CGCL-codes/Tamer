    public void connectionOpened(RTMPConnection conn, RTMP state) {
        System.out.println("opened");
        Channel channel = conn.getChannel((byte) 3);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("app", "test");
        params.put("flashVer", "WIN 9,0,16,0");
        params.put("swfUrl", "http://localhost/test.swf");
        params.put("tcUrl", "rtmp://localhost/movieStreaming");
        params.put("fpad", false);
        params.put("audioCodecs", (double) 615);
        params.put("videoCodecs", (double) 76);
        params.put("pageUrl", "http://localhost/test.html");
        params.put("objectEncoding", (double) 0);
        PendingCall pendingCall = new PendingCall("connect");
        Invoke invoke = new Invoke(pendingCall);
        invoke.setConnectionParams(params);
        invoke.setInvokeId(1);
        channel.write(invoke);
    }
