    private void addPreActivationResourceTab(TabFolder tabFolder) {
        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        languageSet.associate(tabItem, Messages.LocalizeDialog_TabTitle_PluginXml);
        Composite c = new Composite(tabFolder, SWT.NONE);
        tabItem.setControl(c);
        c.setLayout(new GridLayout());
        final Properties p = new Properties();
        Enumeration<?> e = bundle.findEntries("", "plugin.properties", false);
        if (e != null) {
            while (e.hasMoreElements()) {
                URL url = (URL) e.nextElement();
                try {
                    InputStream is = url.openStream();
                    p.load(is);
                    is.close();
                } catch (IOException ex) {
                    throw new RuntimeException("", ex);
                }
                break;
            }
        }
        ITranslatableText[] texts = new ITranslatableText[p.size()];
        int i = 0;
        for (final Object key : p.keySet()) {
            texts[i++] = new ITranslatableText() {

                public String getLocalizedText(Locale locale) {
                    return p.getProperty((String) key);
                }

                public String getLocalizedText() {
                    return getLocalizedText(Locale.getDefault());
                }

                public void validateLocale(Locale locale) {
                }
            };
        }
        Control tv = new TranslatableTreeComposite(c, new TextInputContentProvider(), texts, languageSet, updatedBundles);
        tv.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    }
