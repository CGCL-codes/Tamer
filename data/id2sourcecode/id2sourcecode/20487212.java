    void afterRender(MarkupWriter writer) {
        boolean test = false;
        Iterator<BreadcrumbBean> iterator = breadcrumbService.iterator();
        Element header = writer.element("span");
        header.addClassName("breadcrumb-header");
        writer.end();
        Element breadcrumb = writer.element("span");
        breadcrumb.addClassName("breadcrumb");
        while (iterator.hasNext()) {
            BreadcrumbBean bread = (BreadcrumbBean) iterator.next();
            String label = bread.getLabel();
            if (label != null) {
                test = true;
                boolean hasNext = iterator.hasNext();
                if (linkLast) {
                    writeLink(writer, bread.getLink());
                }
                Element labelSpan = writer.element("span");
                labelSpan.addClassName("breadcrumb-label");
                writer.write(label.toLowerCase());
                writer.end();
                if (linkLast) {
                    writer.end();
                }
                if (hasNext) {
                    writer.write(separator);
                }
            }
        }
        writer.end();
        if (test) {
            header.text(messages.get("breadcrumb-initial-message"));
        }
    }
