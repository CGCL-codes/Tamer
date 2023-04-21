    public InputSource getImportInputSource(String parentLocation, String importLocation) {
        lastImportLocation = URI.create(parentLocation).resolve(importLocation);
        if (isAbsolute(importLocation)) {
            return super.resolveEntity(null, importLocation, parentLocation);
        } else {
            ZipInputStream zin = null;
            try {
                zin = new ZipInputStream(new FileInputStream(aarFile));
                ZipEntry entry;
                byte[] buf = new byte[1024];
                int read;
                ByteArrayOutputStream out;
                String searchingStr = lastImportLocation.toString();
                while ((entry = zin.getNextEntry()) != null) {
                    String entryName = entry.getName().toLowerCase();
                    if (entryName.equalsIgnoreCase(searchingStr)) {
                        out = new ByteArrayOutputStream();
                        while ((read = zin.read(buf)) > 0) {
                            out.write(buf, 0, read);
                        }
                        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                        return new InputSource(in);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zin != null) {
                        zin.close();
                    }
                } catch (IOException e) {
                    log.debug(e);
                }
            }
        }
        log.info("AARBasedWSDLLocator: Unable to resolve " + lastImportLocation);
        return null;
    }
