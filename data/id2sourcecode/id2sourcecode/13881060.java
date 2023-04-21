    void beginRender(MarkupWriter writer) {
        for (int i = 0; i < breadcrumbTrack.size() - 1; i++) {
            writer.element("a", "href", breadcrumbTrack.get(i).getPage());
            writer.write(breadcrumbTrack.get(i).getName());
            writer.end();
            writer.write(" > ");
        }
        writer.element("a", "href", breadcrumbTrack.get(breadcrumbTrack.size() - 1).getPage());
        writer.write(breadcrumbTrack.get(breadcrumbTrack.size() - 1).getName());
        writer.end();
    }
