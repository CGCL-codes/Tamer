    public boolean isActionValid() {
        if (isEditFunctoId) return true;
        if (mappedData.startsWith("$java:") || mappedData.startsWith("$xaware:")) {
            int confirm = ControlFactory.showConfirmDialog(translator.getString("Already functoid existing on selected node, applying new functoid will overwrite existing functoid. Do you want to continue?"));
            if (confirm == Window.OK) return true;
            return false;
        }
        if (childElementCount > 0) {
            ControlFactory.showMessageDialog(translator.getString("Functoids are not allowed on elements that contain child elements."), translator.getString("Information"));
            return false;
        }
        return true;
    }
