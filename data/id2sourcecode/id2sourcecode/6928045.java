    public static String loadResourceFile(FacesContext ctx, String file) {
        ByteArrayOutputStream content = new ByteArrayOutputStream(10240);
        InputStream in = null;
        try {
            in = ctx.getExternalContext().getResourceAsStream(file);
            if (in == null) {
                return null;
            }
            byte[] fileBuffer = new byte[10240];
            int read;
            while ((read = in.read(fileBuffer)) > -1) {
                content.write(fileBuffer, 0, read);
            }
        } catch (FileNotFoundException e) {
            if (log.isLoggable(Level.WARNING)) log.log(Level.WARNING, "no such file " + file, e);
            content = null;
        } catch (IOException e) {
            if (log.isLoggable(Level.WARNING)) log.log(Level.WARNING, "problems during processing resource " + file, e);
            content = null;
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                log.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }
        return content.toString();
    }
