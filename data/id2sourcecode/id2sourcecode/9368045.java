    private void loadAvailableColorizer() {
        List<Colorizer> l = new ArrayList<Colorizer>();
        try {
            Enumeration<URL> en = ClassLoader.getSystemClassLoader().getResources("ch/unibe/im2/inkanno/plugins/colorizer_implementation.properties");
            while (en.hasMoreElements()) {
                Properties p = new Properties();
                URL url = en.nextElement();
                p.load(url.openStream());
                for (Object objstr : p.keySet()) {
                    String str = (String) objstr;
                    if (str.equals("colorizer") || str.startsWith("colorizer.")) {
                        if (p.getProperty(str) != null) {
                            l.add(createColorizerPlugin(p.getProperty(str)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FactoryException e) {
            e.printStackTrace();
        }
        colorizers = l;
        if (colorizers.size() == 0) {
            setColorizer(new NullColorizer());
        } else if (Config.getMain().get("colorizer") != null && !Config.getMain().get("colorizer").isEmpty()) {
            String selection = Config.getMain().get("colorizer");
            for (Colorizer col : colorizers) {
                if (col.isResponsible(selection)) {
                    setColorizer(col);
                    break;
                }
            }
        }
        if (currentColorizer == null) {
            setColorizer(new NullColorizer());
        }
    }
