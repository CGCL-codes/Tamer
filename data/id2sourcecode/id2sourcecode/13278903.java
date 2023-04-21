    private void saveTo(File xmlFile, Object obj) {
        XStream xs = new XStream();
        OutputStream os;
        try {
            os = new FileOutputStream(xmlFile);
            if (xmlFile.getName().endsWith(".zip")) {
                ZipOutputStream zip = new ZipOutputStream(os);
                zip.putNextEntry(new ZipEntry(zipEntryName));
                os = zip;
            } else if (xmlFile.getName().endsWith(".gz")) {
                GZIPOutputStream zip = new GZIPOutputStream(os);
                os = zip;
            }
            xs.toXML(obj, os);
            os.close();
        } catch (StreamException e) {
            GLog.warn(L.tr("XML_Write_Exception:"), e);
        } catch (MalformedURLException e) {
            GLog.warn(L.tr("Malformed_URL:"), e);
        } catch (FileNotFoundException ex) {
            GLog.warn(L.tr("Can't_find_file!"), ex);
        } catch (IOException e) {
            GLog.warn(L.tr("Can't_gzip_the_file:_IOException:"), e);
        }
        GLog.log(L.tr("Save_properties_to:_") + xmlFile.getAbsolutePath());
        changed = false;
    }
