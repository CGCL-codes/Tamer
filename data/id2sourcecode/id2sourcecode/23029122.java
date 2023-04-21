    private ResourceCollectionInfo downloadFiles(URLConnection conn) throws IOException {
        ResourceCollectionInfo info = null;
        InputStream response = new BufferedInputStream(conn.getInputStream());
        ZipInputStream zipIn = new ZipInputStream(response);
        ZipEntry e;
        while ((e = zipIn.getNextEntry()) != null) {
            String name = e.getName();
            long modTime = e.getTime();
            if (ResourceContentStream.MANIFEST_FILENAME.equals(name)) {
                InputStream infoIn = new ByteArrayInputStream(FileUtils.slurpContents(zipIn, false));
                info = XmlCollectionListing.parseListing(infoIn);
                continue;
            }
            OutputStream out = localCollection.getOutputStream(name, modTime);
            if (out == null) continue;
            logger.fine("downloading resource " + name);
            FileUtils.copyFile(zipIn, out);
            out.close();
            zipIn.closeEntry();
        }
        zipIn.close();
        return info;
    }
