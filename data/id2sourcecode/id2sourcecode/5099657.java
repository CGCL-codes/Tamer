    public void write(Resource resource, OutputStream stream) throws ExportException {
        if (resource == null) {
            throw new IllegalArgumentException("Can't export 'null' resource.");
        }
        if (stream == null) {
            throw new IllegalArgumentException("Can't export to 'null' stream.");
        }
        ZipOutputStream zip = new ZipOutputStream(stream);
        try {
            ZipEntry mimeType = new ZipEntry(MIME_TYPE_NAME);
            mimeType.setMethod(ZipEntry.STORED);
            zip.putNextEntry(mimeType);
            zip.write(MIME_TYPE_VALUE.getBytes(charset));
            zip.closeEntry();
            ZipEntry container = new ZipEntry(CONTAINER_NAME);
            zip.putNextEntry(container);
            writeContainer(resource, zip);
            zip.closeEntry();
            ZipEntry packageInfo = new ZipEntry(PACKAGE_INFO_NAME);
            zip.putNextEntry(packageInfo);
            writePackageInfo(resource, zip);
            zip.closeEntry();
            zip.finish();
        } catch (IOException io) {
            throw new ExportException(io);
        } catch (TransformerException t) {
            throw new ExportException(t);
        } catch (FormatException f) {
            throw new ExportException(f);
        }
    }
