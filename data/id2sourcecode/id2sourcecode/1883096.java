    private void readIncludedTemplates(Collection templates, IConfigurationElement element) throws IOException {
        String file = element.getAttribute(FILE);
        if (file != null) {
            Bundle plugin = Platform.getBundle(element.getContributor().getName());
            URL url = FileLocator.find(plugin, Path.fromOSString(file), null);
            if (url != null) {
                ResourceBundle bundle = null;
                InputStream bundleStream = null;
                InputStream stream = null;
                try {
                    String translations = element.getAttribute(TRANSLATIONS);
                    if (translations != null) {
                        URL bundleURL = FileLocator.find(plugin, Path.fromOSString(translations), null);
                        if (bundleURL != null) {
                            bundleStream = bundleURL.openStream();
                            bundle = new PropertyResourceBundle(bundleStream);
                        }
                    }
                    stream = new BufferedInputStream(url.openStream());
                    TemplateReaderWriter reader = new TemplateReaderWriter();
                    TemplatePersistenceData[] datas = reader.read(stream, bundle);
                    for (int i = 0; i < datas.length; i++) {
                        TemplatePersistenceData data = datas[i];
                        if (data.isCustom()) {
                            if (data.getId() == null) EditorsPlugin.logErrorMessage(NLSUtility.format(ContributionTemplateMessages.ContributionTemplateStore_ignore_no_id, data.getTemplate().getName())); else EditorsPlugin.logErrorMessage(NLSUtility.format(ContributionTemplateMessages.ContributionTemplateStore_ignore_deleted, data.getTemplate().getName()));
                        } else if (!validateTemplate(data.getTemplate())) {
                            if (contextExists(data.getTemplate().getContextTypeId())) EditorsPlugin.logErrorMessage(NLSUtility.format(ContributionTemplateMessages.ContributionTemplateStore_ignore_validation_failed, data.getTemplate().getName()));
                        } else {
                            templates.add(data);
                        }
                    }
                } finally {
                    try {
                        if (bundleStream != null) bundleStream.close();
                    } catch (IOException x) {
                    } finally {
                        try {
                            if (stream != null) stream.close();
                        } catch (IOException x) {
                        }
                    }
                }
            }
        }
    }
