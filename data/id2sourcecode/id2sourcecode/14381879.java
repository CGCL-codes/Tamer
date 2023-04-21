    public Element exec(Element params, ServiceContext context) throws Exception {
        int id = Integer.parseInt(params.getChildText(Constants.MAP_SERVICE_ID));
        MapMerger mm = MapUtil.getMapMerger(context);
        Element ret = new Element("response");
        MapService ms = mm.getService(id);
        if (ms instanceof WmsService) {
            WmsService ws = (WmsService) ms;
            WMSLayer wlayer = ws.getWmsLayer();
            ret.addContent(new Element("type").setText("WMS"));
            ret.addContent(new Element("title").setText(wlayer.getTitle()));
            ret.addContent(new Element("abstract").setText(wlayer.getAbstract()));
            String legendURL = ws.getLegendUrl();
            if (legendURL != null) ret.addContent(new Element("legendURL").setText(legendURL));
            WMSMetadataURL md = null;
            for (PreferredInfo pref : _preferred) {
                md = getMetadata(wlayer, pref);
                if (md != null) break;
            }
            if (md != null) {
                String href = md.getOnlineResource().getHref();
                URL url = new URL(href);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String info = (String) conn.getContent();
                ret.addContent(new Element("info").setText(info).setAttribute("format", md.getFormat().toString()).setAttribute("type", md.getType().toString()));
            }
        } else {
            ret.addContent(new Element("type").setText("ARCIMS"));
            ret.addContent(new Element("title").setText(ms.getTitle()));
        }
        return ret;
    }
