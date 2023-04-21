    private String getConfigHashCode(Serializable aConfiguration) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(aConfiguration);
            oos.flush();
            oos.close();
            final MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(baos.toByteArray());
            return hexEncode(md.digest());
        } catch (final Exception ex) {
            Utils.getExceptionLogger().debug("Unable to calculate hashcode.", ex);
            return "ALWAYS FRESH: " + System.currentTimeMillis();
        }
    }
