    public void run(String query, ISearchScope scope, ISearchEngineResultCollector collector, IProgressMonitor monitor) throws CoreException {
        URL url = createURL(query, (Scope) scope);
        if (url == null) return;
        InputStream is = null;
        tocs.clear();
        try {
            URLConnection connection = url.openConnection();
            monitor.beginTask(HelpBaseResources.InfoCenter_connecting, 5);
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            monitor.worked(1);
            load(((Scope) scope).url, reader, collector, new SubProgressMonitor(monitor, 4));
            reader.close();
        } catch (FileNotFoundException e) {
            reportError(HelpBaseResources.InfoCenter_fileNotFound, e, collector);
        } catch (IOException e) {
            reportError(HelpBaseResources.InfoCenter_io, e, collector);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
