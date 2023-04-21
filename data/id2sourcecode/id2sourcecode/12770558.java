    public static void saveToFile(Object obj, File xmlFile) {
        ISerializer xs = Lookup.get().getImplObject(ISerializer.class);
        OutputStream os;
        try {
            os = new FileOutputStream(xmlFile);
            try {
                if (xmlFile.getName().endsWith(".zip")) {
                    ZipOutputStream zip = new ZipOutputStream(os);
                    zip.putNextEntry(new ZipEntry(zipEntryName));
                    os = zip;
                } else if (xmlFile.getName().endsWith(".gz")) {
                    GZIPOutputStream zip = new GZIPOutputStream(os);
                    os = zip;
                }
                xs.toXML(obj, os);
            } finally {
                os.close();
            }
        } catch (SerializeException e) {
            GLog.warn(L.tr("XML_Write_Exception:"), e);
        } catch (MalformedURLException e) {
            GLog.warn(L.tr("Malformed_URL:"), e);
        } catch (FileNotFoundException ex) {
            GLog.warn(L.tr("Can't_find_file!"));
        } catch (IOException e) {
            GLog.warn(L.tr("Can't_gzip_the_file:_IOException:"), e);
        }
        GLog.log(L.tr("Saved_object_to:") + " " + xmlFile.getAbsolutePath());
    }
