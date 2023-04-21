    protected void runExtractor(UURI baseUURI, String encoding) throws IOException, InvalidAttributeValueException, AttributeNotFoundException, MBeanException, ReflectionException {
        if (baseUURI == null) {
            return;
        }
        this.extractor = createExtractor();
        URL url = new URL(baseUURI.toString());
        this.recorder = HttpRecorder.wrapInputStreamWithHttpRecord(getTmpDir(), this.getClass().getName(), url.openStream(), encoding);
        CrawlURI curi = setupCrawlURI(this.recorder, url.toString());
        this.extractor.innerProcess(curi);
        System.out.println("+" + this.extractor.report());
        int count = 0;
        Collection links = curi.getOutLinks();
        System.out.println("+HTML Links (hopType=" + Link.NAVLINK_HOP + "):");
        if (links != null) {
            for (Iterator i = links.iterator(); i.hasNext(); ) {
                Link link = (Link) i.next();
                if (link.getHopType() == Link.NAVLINK_HOP) {
                    count++;
                    System.out.println(link.getDestination());
                }
            }
        }
        System.out.println("+HTML Embeds (hopType=" + Link.EMBED_HOP + "):");
        if (links != null) {
            for (Iterator i = links.iterator(); i.hasNext(); ) {
                Link link = (Link) i.next();
                if (link.getHopType() == Link.EMBED_HOP) {
                    count++;
                    System.out.println(link.getDestination());
                }
            }
        }
        System.out.println("+HTML Speculative Embeds (hopType=" + Link.SPECULATIVE_HOP + "):");
        if (links != null) {
            for (Iterator i = links.iterator(); i.hasNext(); ) {
                Link link = (Link) i.next();
                if (link.getHopType() == Link.SPECULATIVE_HOP) {
                    count++;
                    System.out.println(link.getDestination());
                }
            }
        }
        System.out.println("+HTML Other (all other hopTypes):");
        if (links != null) {
            for (Iterator i = links.iterator(); i.hasNext(); ) {
                Link link = (Link) i.next();
                if (link.getHopType() != Link.SPECULATIVE_HOP && link.getHopType() != Link.NAVLINK_HOP && link.getHopType() != Link.EMBED_HOP) {
                    count++;
                    System.out.println(link.getHopType() + " " + link.getDestination());
                }
            }
        }
        System.out.println("TOTAL URIS EXTRACTED: " + count);
    }
