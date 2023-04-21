    public byte[] representation(final typeAffichage type, final int representation) {
        byte[] tab = null;
        if (type == typeAffichage.IMAGE) {
            try {
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                final String path = "dodico_java_ecrit/org/fudaa/dodico/simboat/ressources/" + representation + "/" + type.value() + ".gif";
                final FileInputStream is = new FileInputStream(path);
                while (is.available() != 0) {
                    os.write(is.read());
                }
                tab = os.toByteArray();
                os.close();
                is.close();
            } catch (final IOException _e) {
            }
        }
        return tab;
    }
