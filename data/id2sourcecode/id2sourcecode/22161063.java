    boolean wsdlIsValid() {
        overwriteWSDL = false;
        if (wsdlFileNameIsEmpty()) {
            showMessageDialog(translator.getString("Please enter a WSDL file name."));
            return false;
        }
        File wsdlFile = getWsdlFile();
        if (wsdlFile.isDirectory()) {
            showMessageDialog(translator.getString("Please enter a WSDL file name."));
            return false;
        }
        if (createNewIsSelected()) {
            String wsdlFileName = getWsdlFileName();
            if (!wsdlFileName.endsWith(".wsdl")) {
                if (wsdlFileName.endsWith(".")) {
                    wsdlFileName = wsdlFileName + "wsdl";
                } else {
                    wsdlFileName = wsdlFileName + ".wsdl";
                }
                this.wsdlFileName = wsdlFileName;
                wsdlFileTxt.setText(wsdlFileName);
                wsdlFile = getWsdlFile();
            }
            if (wsdlFile.isDirectory()) {
                showMessageDialog(translator.getString("WSDL file is a directory.  Please enter a new WSDL file name."));
                return false;
            }
            if (wsdlFile.exists()) {
                overwriteWSDL = showConfirmDialog(translator.getString("WSDL file already exists.  Overwrite it?"));
                if (!overwriteWSDL) {
                    return false;
                }
            }
        } else {
            if (!existingWsdlIsValid(wsdlFile)) {
                return false;
            }
            if (!wsdlFile.canWrite()) {
                showMessageDialog(translator.getString("WSDL file is not a writeable file.  Please enter a new WSDL file name."));
                return false;
            }
        }
        return true;
    }
