    public Collection loadModel(URL url, URL publicId) throws ProfileException {
        if (url == null) {
            throw new ProfileException("Null profile URL");
        }
        ZipInputStream zis = null;
        try {
            Collection elements = null;
            XmiReader xmiReader = Model.getXmiReader();
            if (url.getPath().toLowerCase().endsWith(".zip")) {
                zis = new ZipInputStream(url.openStream());
                ZipEntry entry = zis.getNextEntry();
                if (entry != null) {
                    url = makeZipEntryUrl(url, entry.getName());
                }
                zis.close();
            }
            InputSource inputSource = new InputSource(url.toExternalForm());
            inputSource.setPublicId(publicId.toString());
            elements = xmiReader.parse(inputSource, true);
            return elements;
        } catch (UmlException e) {
            throw new ProfileException("Invalid XMI data!", e);
        } catch (IOException e) {
            throw new ProfileException("Invalid zip file with XMI data!", e);
        }
    }
