    private void harvestCatalog(Element cata) throws Exception {
        if (cata == null) return;
        localCateg = new CategoryMapper(dbms);
        localGroups = new GroupMapper(dbms);
        Lib.net.setupProxy(context);
        InvCatalogFactory factory = new InvCatalogFactory("default", true);
        catalog = (InvCatalogImpl) factory.readXML(params.url);
        StringBuilder buff = new StringBuilder();
        if (!catalog.check(buff, true)) {
            throw new BadXmlResponseEx("Invalid catalog " + params.url + "\n" + buff.toString());
        }
        log.info("Catalog read from " + params.url + " is \n" + factory.writeXML(catalog));
        String serviceStyleSheet = context.getAppPath() + Geonet.Path.IMPORT_STYLESHEETS + "/ThreddsCatalog-to-ISO19119_ISO19139.xsl";
        URL url = new URL(params.url);
        hostUrl = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() != -1) hostUrl += ":" + url.getPort();
        log.info("Crawling the datasets in the catalog....");
        List<InvDataset> dsets = catalog.getDatasets();
        for (InvDataset ds : dsets) {
            crawlDatasets(ds);
        }
        int totalDs = result.collectionDatasetRecords + result.atomicDatasetRecords;
        log.info("Processed " + totalDs + " datasets.");
        if (params.createServiceMd) {
            log.info("Processing " + services.size() + " services...");
            processServices(cata, serviceStyleSheet);
            log.info("Creating service metadata for thredds catalog...");
            Map<String, String> param = new HashMap<String, String>();
            param.put("lang", params.lang);
            param.put("topic", params.topic);
            param.put("uuid", params.uuid);
            param.put("url", params.url);
            param.put("name", catalog.getName());
            param.put("type", "Thredds Data Service Catalog " + catalog.getVersion());
            param.put("version", catalog.getVersion());
            param.put("desc", Xml.getString(cata));
            param.put("props", catalog.getProperties().toString());
            param.put("serverops", "");
            log.debug("  - XSLT transformation using " + serviceStyleSheet);
            Element md = Xml.transform(cata, serviceStyleSheet, param);
            saveMetadata(md, Util.scramble(params.url), params.url);
            harvestUris.add(params.url);
            result.serviceRecords++;
        }
    }
