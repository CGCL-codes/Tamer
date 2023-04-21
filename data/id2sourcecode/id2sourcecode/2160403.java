    public static SourceChannel getChannel(String channelName, Stream stream) {
        SourceChannel s = new SourceChannel();
        s.x = stream.x;
        s.y = stream.y;
        s.width = stream.width;
        s.height = stream.height;
        s.opacity = stream.opacity;
        s.effects.addAll(stream.effects);
        s.startTransitions.addAll(stream.startTransitions);
        s.endTransitions.addAll(stream.endTransitions);
        s.volume = stream.volume;
        s.zorder = stream.zorder;
        s.name = channelName;
        s.isPlaying = stream.isPlaying();
        s.capHeight = stream.captureHeight;
        s.capWidth = stream.captureWidth;
        if (stream instanceof SourceText) {
            SourceText st = (SourceText) stream;
            s.text = st.content;
            s.font = st.fontName;
            s.color = st.color;
        } else if (stream instanceof SourceDesktop) {
            SourceDesktop sd = (SourceDesktop) stream;
        }
        return s;
    }
