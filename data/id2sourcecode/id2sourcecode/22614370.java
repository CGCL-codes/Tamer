    public void read() {
        if (log.isDebugEnabled()) {
            log.debug("Reading: " + url + "...");
        }
        SAXReader reader = new SAXReader();
        InputStream is = null;
        try {
            is = url.openStream();
            Document document = reader.read(is);
            Element root = document.getRootElement();
            boolean ignoreDuplicates = Dom4jUtil.getAttributeBooleanOptional(root, "ignoreDuplicates", true);
            boolean overwriteExisting = Dom4jUtil.getAttributeBooleanOptional(root, "overwriteExisting", false);
            for (Iterator<Element> iter = root.elementIterator(); iter.hasNext(); ) {
                Element itemElem = iter.next();
                String elemName = itemElem.getName();
                if ("menubar".equals(elemName)) {
                    setupMenubar(itemElem, ignoreDuplicates, overwriteExisting);
                } else if ("menu".equals(elemName)) {
                    setupMenu(itemElem, ignoreDuplicates, overwriteExisting);
                } else if ("menu-item".equals(elemName)) {
                    setupMenuItem(itemElem, ignoreDuplicates, overwriteExisting);
                } else if ("separator".equals(elemName)) {
                    setupSeparator(itemElem, ignoreDuplicates, overwriteExisting);
                } else {
                    if (!"overwriteExisting".equals(elemName) && !"ignoreDuplicates".equals(elemName) && !"module-id".equals(elemName) && !"depends-on".equals(elemName)) {
                        log.warn("Invalid XML element encountered in menu.xml: " + elemName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.fatal("Error reading menu config from " + url, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                log.error("Error closing input stream.", e);
            }
            installed = true;
        }
        if (log.isInfoEnabled()) {
            log.info("Menu initialization finished for: " + url);
        }
    }
