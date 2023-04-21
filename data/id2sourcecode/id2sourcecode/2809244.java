    public void exportData(TVData data, JFrame parent) throws Exception {
        JFileChooser chooser = new JFileChooser();
        if (config.path != null) {
            chooser.setSelectedFile(new File(config.path));
        }
        final FileFilter zipFilter = new FileFilter() {

            public String getDescription() {
                return i18n.getString("Format.zip");
            }

            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().endsWith(EXT_ZIP);
            }
        };
        final FileFilter gzipFilter = new FileFilter() {

            public String getDescription() {
                return i18n.getString("Format.gz");
            }

            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().endsWith(EXT_GZ);
            }
        };
        final FileFilter htmlFilter = new FileFilter() {

            public String getDescription() {
                return i18n.getString("Format.html");
            }

            public boolean accept(File f) {
                return !f.isDirectory() && (f.getName().endsWith(EXT_HTML) || f.getName().endsWith(EXT_HTM));
            }
        };
        chooser.addChoosableFileFilter(gzipFilter);
        chooser.addChoosableFileFilter(zipFilter);
        chooser.addChoosableFileFilter(htmlFilter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        IApplication app = Application.getInstance();
        final FileChooserExtension ext = new FileChooserExtension(app.getDataStorage(), app.getViewer(), app);
        chooser.setAccessory(ext);
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            String lowerPath = path.toLowerCase();
            if ((chooser.getFileFilter() == zipFilter) && !lowerPath.endsWith(EXT_ZIP)) {
                path += EXT_ZIP;
            }
            if ((chooser.getFileFilter() == gzipFilter) && !lowerPath.endsWith(EXT_GZ)) {
                path += EXT_GZ;
            }
            if ((chooser.getFileFilter() == htmlFilter) && !(lowerPath.endsWith(EXT_HTML) || (lowerPath.endsWith(EXT_HTM)))) {
                path += EXT_HTML;
            }
            config.path = path;
            lowerPath = path.toLowerCase();
            TemplateParser parser = new TemplateParser(TEMPLATE);
            final OutputStream outStream;
            if (lowerPath.endsWith(EXT_ZIP)) {
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path));
                ZipEntry zipEntry = new ZipEntry(ZIP_ENTRY);
                zipOut.putNextEntry(zipEntry);
                outStream = zipOut;
            } else if (lowerPath.endsWith(EXT_GZ)) {
                outStream = new GZIPOutputStream(new FileOutputStream(path));
            } else {
                outStream = new FileOutputStream(path);
            }
            final Writer out = new BufferedWriter(new OutputStreamWriter(outStream, CHARSET));
            try {
                parser.process(new TemplateHandler(ext, i18n), out);
            } finally {
                out.flush();
                out.close();
            }
        }
    }
