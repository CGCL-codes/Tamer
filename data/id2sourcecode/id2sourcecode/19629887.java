    private boolean copyUpdateFile(final File outFile, final String resPath, final Map<String, String> propsMap) {
        if ((propsMap == null) || (propsMap.size() <= 0)) return createOutputFile(outFile, resPath);
        try {
            final ResourcesAnchor anchor = ResourcesAnchor.getInstance();
            final URL url = anchor.getResource(resPath);
            final Reader in = new InputStreamReader(url.openStream());
            final StringWriter out = new StringWriter(1024);
            try {
                final long cpyBytes = IOCopier.copyReaderToWriter(in, out);
                if (cpyBytes < 0L) throw new StreamCorruptedException("Error (" + cpyBytes + ") on copy default project contents");
            } finally {
                in.close();
            }
            String content = out.toString();
            for (final Map.Entry<String, String> propEntry : propsMap.entrySet()) {
                final String propName = propEntry.getKey(), propValue = propEntry.getValue();
                content = content.replace(propName, propValue);
            }
            final Writer w = new FileWriter(outFile);
            try {
                w.append(content);
            } finally {
                w.close();
            }
            debug("Created " + outFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            error("Failed (" + e.getClass().getSimpleName() + ")" + " to create " + outFile.getAbsolutePath() + ": " + e.getMessage());
            return false;
        }
    }
