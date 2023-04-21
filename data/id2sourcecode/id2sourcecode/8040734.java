    }

    void saveBlueprint(Blueprint blueprint) throws IOException {
        if (!blueprint.isDirty()) return;
        File file = getBlueprintFile(blueprint);
        File parent = file.getParentFile();
        if ((!parent.exists() && !parent.mkdirs()) || !parent.isDirectory()) throw new IOException("Cannot create folder for blueprint " + blueprint.getName());
        readwrite(new StringReader(blueprint.getHTML()), new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
        blueprint.clearDirty();
