    private byte[] nactiFotoPomociTokenu(String accessToken, ContactEntry kontakt) {
        if (accessToken == null || kontakt == null) {
            LOGGER.log(Level.WARNING, "Špatně zadené parametry accessToken: {0}, kontakt: {1}", new Object[] { accessToken, kontakt });
            return null;
        }
        Link photoLink = kontakt.getContactPhotoLink();
        int read;
        if (photoLink != null) {
            InputStream in = null;
            try {
                ContactsService kontaktService = new ContactsService(JMENO_APLIKACE);
                kontaktService.setAuthSubToken(accessToken);
                in = kontaktService.getStreamFromLink(photoLink);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                while (true) {
                    if ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    } else {
                        break;
                    }
                }
                LOGGER.log(Level.WARNING, "Foto načteno.");
                return out.toByteArray();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Chyba na vstupu/výstupu", ex);
            } catch (ServiceException ex) {
                LOGGER.log(Level.WARNING, "Chyba služby", ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Chyba na vstupu/výstupu", ex);
                }
            }
        }
        return null;
    }
