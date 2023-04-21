    @Override
    public void install(final String location) throws IOException {
        URL lurl = new URL(location);
        InputStream is = lurl.openStream();
        JarInputStream jis = new JarInputStream(is);
        Manifest mf = jis.getManifest();
        if (mf == null) {
            throw new IOException("missing manifest");
        }
        Dictionary<String, String> headers = toDictionary(mf.getMainAttributes());
        manifestEntries.put(location, headers);
        String s = headers.get(Constants.BUNDLE_ISOLATION);
        if (s == null) {
            LOGGER.debug("no isolation constraints specified in manifest");
        } else {
            List<IsolationDirective> directives = this.getDirectives(location);
            Matcher m = DIRECTIVE.matcher(s);
            while (m.find()) {
                int level = Integer.parseInt(m.group(1));
                Filter filter = null;
                try {
                    filter = FrameworkUtil.createFilter(m.group(2));
                } catch (InvalidSyntaxException ise) {
                    throw new IOException("invalid filter in isolation directive: " + m.group(), ise);
                }
                IsolationDirective d = new IsolationDirective(level, filter);
                directives.add(d);
                LOGGER.debug("directive {} added", d);
            }
            for (IsolationDirective d : directives) {
                Filter f = d.getFilter();
                int level = d.getLevel();
                for (Map.Entry<String, Dictionary<String, String>> e : manifestEntries.entrySet()) {
                    String otherLocation = e.getKey();
                    if (!otherLocation.equals(location) && f.match(e.getValue())) {
                        IsolationConstraint c = new IsolationConstraint(location, otherLocation, level);
                        registry.add(c);
                    }
                }
            }
        }
        Dictionary<String, String> props = manifestEntries.get(location);
        for (Map.Entry<String, List<IsolationDirective>> e : directives.entrySet()) {
            String otherLocation = e.getKey();
            if (otherLocation.equals(location)) continue;
            for (IsolationDirective d : e.getValue()) {
                Filter f = d.getFilter();
                if (f.match(props)) {
                    int level = d.getLevel();
                    IsolationConstraint c = new IsolationConstraint(otherLocation, location, level);
                    registry.add(c);
                }
            }
        }
        List<Operation> ops = null;
        try {
            ops = engine.install(location);
        } catch (Exception e) {
            throw new IOException(e);
        }
        execute(ops);
    }
