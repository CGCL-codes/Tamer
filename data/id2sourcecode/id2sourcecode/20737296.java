    public Object addFromURL(Registry reg, URL url, ModifiableMap params, Workbench ui) {
        FilterSource fs = null;
        if (url != null) {
            try {
                MimeType mt = (mimeType != null) ? MimeType.valueOf(mimeType) : IO.getMimeType(url.toString());
                File file = Utils.urlToFile(url);
                if (file.exists()) {
                    fs = new FileSource(file, mt, reg, params);
                } else {
                    fs = new InputStreamSourceImpl(url.openStream(), url.toString(), mt, reg, params);
                }
            } catch (IOException e) {
                ui.logGUIInfo(IO.I18N.msg("openfile.failed", url.getFile()), e);
                return null;
            }
        }
        Object[] a = read(reg, fs, false, ui);
        if (a == null) {
            return null;
        }
        ObjectItem i = (ObjectItem) a[1];
        if (i != null) {
            i.setObjDescribes(true);
            ResourceDirectory rd = ResourceDirectory.get(this);
            rd.getProjectDirectory(reg).addUserItemWithUniqueName(i, rd.getBaseName());
        }
        return a[0];
    }
