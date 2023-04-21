    boolean archiveOverwriteOK() {
        ZipFile archiveZipFile = null;
        final String archiveName = getArchiveFileName();
        try {
            archiveZipFile = getArchiveZipFile();
            final String rootWsdlFileName = getWsdlFile().getName();
            final ZipEntry existingWsdlEntry = archiveZipFile.getEntry(rootWsdlFileName);
            if (existingWsdlEntry != null) {
                boolean overwriteArchiveWSDL = showConfirmDialog(translator.getString("WSDL file already exists in archive.  Overwrite it?"));
                if (!overwriteArchiveWSDL) {
                    return false;
                }
            }
        } catch (final IOException e) {
            showMessageDialog(translator.getString("Error reading contents of ") + archiveName + translator.getString(": ") + e.getLocalizedMessage());
        } finally {
            if (archiveZipFile != null) {
                try {
                    archiveZipFile.close();
                } catch (final IOException e) {
                    showMessageDialog(translator.getString("Error closing ") + archiveName + translator.getString(": ") + e.getLocalizedMessage());
                }
            }
        }
        return true;
    }
