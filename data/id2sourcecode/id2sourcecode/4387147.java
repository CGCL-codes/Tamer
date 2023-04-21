    @SuppressWarnings("unchecked")
    public DiscoveryImpl() throws IOException, ClassNotFoundException {
        Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/pyrus/discovery.inf");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals("mobject")) {
                    if (parts[1].equals("")) {
                        parts[1] = parts[2];
                    }
                    String ifaceName, subId;
                    String[] subparts1 = parts[1].split("-");
                    if (subparts1.length == 2) {
                        ifaceName = subparts1[0];
                        subId = subparts1[1];
                        if (subId.equals("auto")) {
                            subId = Integer.toString(nextAutoId++);
                        }
                    } else {
                        ifaceName = parts[1];
                        subId = null;
                    }
                    mobjectDescs.add(new MObjectDesc((Class<? extends MObject>) Class.forName(ifaceName), subId, (Class<MObject>) Class.forName(parts[2])));
                } else if (parts[0].equals("theme")) {
                    themes.add(parts[1]);
                }
            }
        }
        mobjectDescs = Collections.unmodifiableList(mobjectDescs);
        themes = Collections.unmodifiableList(themes);
    }
