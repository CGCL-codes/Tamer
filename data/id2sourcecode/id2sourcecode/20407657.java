    public void pageCapture() {
        Link link = pagesToBeCaptured.get();
        try {
            System.out.println("Parsing " + link.getUrl());
            URL _url = new URL(link.getUrl());
            parser = new Parser(_url.openConnection());
            NodeIterator nodeIterator = parser.elements();
            ArrayList<String> listeBalises = new ArrayList<String>();
            Page page = new Page(link.getUrl());
            while (nodeIterator.hasMoreNodes()) {
                Node node = nodeIterator.nextNode();
                node.accept(new URLExtractingVisitor(link.getDepth() + 1, this, link.getUrl(), site.getStatsPages()));
                node.accept(new URLModifyingVisitor(site.getSourceString()));
                node.accept(new StatsVisitor(page, site.getStatsBalises()));
                node.accept(new EmailVisitor(site));
                listeBalises.add(node.toHtml());
            }
            PageDownloader fd = new PageDownloader(link.getUrl(), site.getTarget() + "/" + site.getName(), site.getSourceString(), listeBalises);
            fd.save(page);
            site.addPage(page);
        } catch (MalformedURLException e) {
            System.out.println("Malformed url " + link.getUrl());
            System.exit(-1);
        } catch (ParserException e) {
            System.out.println("Error while parsing " + link.getUrl());
        } catch (IOException e) {
            System.out.println("Connection failed to " + link.getUrl());
        }
        pagesToBeCaptured.remove(link);
        pagesCaptured.add(link);
    }
