    boolean serviceOverwriteOK() {
        overwriteService = false;
        if (addToExistingIsSelected()) {
            final File wsdlFile = getWsdlFile();
            try {
                if (WSDLFileUtils.serviceExists(getServiceName(), wsdlFile.getAbsolutePath())) {
                    overwriteService = showConfirmDialog(translator.getString("Service already exists.  Overwrite it?"));
                    if (!overwriteService) {
                        return false;
                    }
                }
            } catch (final WSDLException e) {
                showMessageDialog(translator.getString("Error reading ") + wsdlFile.getAbsolutePath() + translator.getString(": ") + e.getLocalizedMessage());
                return false;
            }
        }
        return true;
    }
