    private BSFAction readAction(IXMLElement element, AutomatedInstallData idata) throws InstallerException {
        BSFAction action = new BSFAction();
        String src = element.getAttribute("src");
        if (src != null) {
            InputStream is = null;
            InputStream subis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                byte buf[] = new byte[10 * 1024];
                int read = 0;
                is = ResourceManager.getInstance().getInputStream(src);
                subis = new SpecHelper().substituteVariables(is, new VariableSubstitutor(idata.getVariables()));
                while ((read = subis.read(buf, 0, 10 * 1024)) != -1) {
                    baos.write(buf, 0, read);
                }
                action.setScript(new String(baos.toByteArray()));
            } catch (Exception e) {
                throw new InstallerException(e);
            } finally {
                try {
                    if (subis != null) {
                        subis.close();
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String script = element.getContent();
            if (script == null) {
                script = "";
            }
            action.setScript(script);
        }
        String language = element.getAttribute("language");
        action.setLanguage(language);
        return action;
    }
