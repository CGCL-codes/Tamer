    public static String generateKey(final CtProject project) {
        String key = project.getKey();
        if ((key == null) || (key.trim().equals(""))) {
            String all = project.getName() + project.getEmail();
            key = MD5String.digest(all);
        }
        return key;
    }
