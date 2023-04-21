    public Tarantula(String urlSite) {
        try {
            if (!isArchivo()) {
                in = (new URL(urlSite)).openStream();
            } else {
                in = new FileInputStream(new File(urlSite));
            }
            HtmlCleaner cleaner = new HtmlCleaner();
            setProps(cleaner.getProperties());
            TagNode node = cleaner.clean(in);
            nodes = node.getElementListByAttValue("class", "entry", true, true);
        } catch (MalformedURLException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
