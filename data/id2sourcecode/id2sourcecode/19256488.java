    public static void setRoot(ModelBit model) throws ModelException {
        if (theModelRoot != null) {
            throw new ModelException("Root has already been set; can't overwrite it");
        }
        theModelRoot = model;
        allNames.put(model.getName(), model);
    }
