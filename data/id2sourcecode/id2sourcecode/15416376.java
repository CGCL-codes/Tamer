    void removePreviewWindow(PreviewWindow w) {
        w.previewNode.getChannel().removeChannelChangeListener(w);
        previewWindows.remove(w);
    }
