    private Entry[] fetch(URL url, boolean refetchContent) throws IOException, Exception {
        LOG.finest("Opening connection to URL " + url);
        URLConnection con = url.openConnection();
        long modified = con.getLastModified();
        String contentType = con.getContentType();
        String contentEnc = con.getContentEncoding() != null ? con.getContentEncoding() : defaultEncoding;
        ContentTypeProcessor proc = getProcessor(contentType);
        LOG.finest("ContentType: " + contentType + " - ContentEncoding: " + contentEnc);
        LOG.fine("Using Processor " + proc);
        Entry[] entries = proc.process(url, con.getInputStream(), contentEnc);
        List returnArr = new ArrayList();
        List fetchLater = new ArrayList();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) continue;
            if (refetchContent && entries[i].getLastFetched() == null) {
                fetchLater.add(entries[i]);
                continue;
            }
            Date modDate = new Date(modified);
            if (entries[i].getLastModified() == null) entries[i].setLastModified(modDate);
            if (entries[i].getIssued() == null) entries[i].setIssued(modDate);
            URL entryUrl = entries[i].getUrl();
            String fileType = entries[i].getFileType();
            if (fileType == null || "".equals(fileType)) {
                int dotIndex = entryUrl.getPath().lastIndexOf(".");
                if (dotIndex != -1) {
                    String type = entryUrl.getPath().substring(dotIndex + 1);
                    entries[i].setFileType(type.toLowerCase());
                    LOG.fine("Filetype is subsequently set to '" + type + "'");
                }
            }
            String title = entries[i].getTitle();
            if (title == null || "".equals(title)) entries[i].setTitle(entryUrl.toString());
            returnArr.add(entries[i]);
            entries[i] = null;
        }
        if (refetchContent) {
            for (int i = 0; i < fetchLater.size(); i++) {
                Entry e = (Entry) fetchLater.get(i);
                LOG.fine("Refetching URL " + e.getUrl());
                returnArr.addAll(Arrays.asList(fetch(e.getUrl(), false)));
            }
        }
        return (Entry[]) returnArr.toArray(new Entry[0]);
    }
