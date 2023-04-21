    public Map inspectPath(String classpath, String indexes) throws Exception {
        Map map = new HashMap();
        List list = new ArrayList();
        CCIHandler[] hdlrs = classInspectorHdlrs;
        if (indexes != null) {
            hdlrs = getSubsetForIndexes(indexes);
        }
        for (int i = 0; i < hdlrs.length; i++) {
            CCIHandler cih = hdlrs[i];
            list.add(cih.getClassPrefixes());
        }
        String[] prefixes = CCIUtils.mergeStringArrs(list);
        CCIUtils.say("CI inspectPath using prefixes: " + Arrays.asList(prefixes));
        URL[] urls = CCIUtils.extractAllURLsInClasspath(classpath);
        String[] matchOneOf = new String[prefixes.length];
        for (int i = 0; i < matchOneOf.length; i++) {
            matchOneOf[i] = prefixes[i] + ".*";
        }
        CCIIntrospector ccii = new CCIIntrospector(matchOneOf, matchNoneOf);
        for (int i = 0; i < urls.length; i++) {
            InputStream is = urls[i].openStream();
            String[] refApis = ccii.introspectClassAndGetReferencedAPIs(is);
            is.close();
            CCIResults cir = new CCIResults();
            for (int i2 = 0; i2 < hdlrs.length; i2++) {
                CCIHandler cih = hdlrs[i2];
                CCIResults cir1 = cih.getRefs(refApis);
                cir = CCIResults.merge(cir, cir1);
            }
            map.put(urls[i], cir);
        }
        return map;
    }
