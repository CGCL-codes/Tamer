    private static String[] _splitTemplate(String rsc) throws IOException {
        InputStream is = ClassUtils.getContextClassLoader().getResourceAsStream(rsc);
        if (is == null) {
            is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(rsc);
            if (is == null) {
                is = ErrorPageWriter.class.getClassLoader().getResourceAsStream(rsc);
            }
        }
        if (is == null) {
            throw new IllegalArgumentException("Could not find resource " + rsc);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int read;
        while ((read = is.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
        String str = baos.toString();
        return str.split("@@");
    }
