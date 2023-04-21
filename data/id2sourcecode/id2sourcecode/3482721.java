    public void toXML(final File file) {
        final String filename = file.getName();
        if (StringUtils.endsWith(filename, ".zip")) {
            ZipOutputStream zipOutputStream = null;
            try {
                zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
                final ZipEntry zipEntry = new ZipEntry("representation.xml");
                zipOutputStream.putNextEntry(zipEntry);
                toXML(zipOutputStream);
            } catch (final FileNotFoundException fileNotFoundException) {
                ErrorUtils.logAndThrowRuntimeException("The file specified in URL " + file.getAbsolutePath() + " was not found", fileNotFoundException);
            } catch (final IOException exception) {
                ErrorUtils.logAndThrowRuntimeException(exception);
            } finally {
                if (zipOutputStream != null) {
                    try {
                        zipOutputStream.closeEntry();
                        zipOutputStream.close();
                    } catch (final IOException exception2) {
                        ErrorUtils.logAndThrowRuntimeException(exception2);
                    }
                }
            }
        }
    }
