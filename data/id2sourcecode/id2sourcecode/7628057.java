    private Vector getFactoryClassNames(URL url) throws IOException {
        Vector v = new Vector(1);
        if (url != null) {
            String transformerFactoryClassName = null;
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                transformerFactoryClassName = br.readLine();
                if (transformerFactoryClassName == null) {
                    break;
                }
                String tfcName = transformerFactoryClassName.trim();
                if (tfcName.length() == 0) {
                    continue;
                }
                int commentIdx = tfcName.indexOf("#");
                if (commentIdx == 0) {
                    continue;
                } else {
                    if (commentIdx < 0) {
                        v.addElement(tfcName);
                    } else {
                        v.addElement(tfcName.substring(0, commentIdx).trim());
                    }
                }
            }
            return v;
        } else {
            return null;
        }
    }
