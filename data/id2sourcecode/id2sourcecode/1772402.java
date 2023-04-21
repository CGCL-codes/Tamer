    private SchemaResource copyOrIdentifyDuplicateURL(String schemaLocation, String namespace) {
        String targetFilename;
        String digest;
        SchemaResource result;
        try {
            targetFilename = uniqueFilenameForURI(schemaLocation);
        } catch (URISyntaxException e) {
            warning("Invalid URI '" + schemaLocation + "':" + e.getMessage());
            return null;
        } catch (IOException e) {
            warning("Could not create local file for " + schemaLocation + ":" + e.getMessage());
            return null;
        }
        try {
            URL url = new URL(schemaLocation);
            DigestInputStream input = digestInputStream(url.openStream());
            writeInputStreamToFile(input, targetFilename);
            digest = HexBin.bytesToString(input.getMessageDigest().digest());
        } catch (Exception e) {
            warning("Could not copy remote resource " + schemaLocation + ":" + e.getMessage());
            return null;
        }
        result = (SchemaResource) _resourceForDigest.get(digest);
        if (result != null) {
            deleteFile(targetFilename);
            result.addSchemaLocation(schemaLocation);
            if (!_resourceForURL.containsKey(schemaLocation)) _resourceForURL.put(schemaLocation, result);
            return result;
        }
        warning("Downloaded " + schemaLocation + " to " + targetFilename);
        DownloadedSchemaEntry newEntry = addNewEntry();
        newEntry.setFilename(targetFilename);
        newEntry.setSha1(digest);
        if (namespace != null) newEntry.setNamespace(namespace);
        newEntry.addSchemaLocation(schemaLocation);
        return updateResource(newEntry);
    }
