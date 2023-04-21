    public static final void load(String filename, String name) throws LoadException {
        {
            Object o = resources.get(name);
            if (o != null) return;
        }
        ReadableByteChannel channel = ResourcePathManager.getChannel(filename);
        if (channel == null) throw new LoadException("Unable to open path: " + filename);
        Image2D image = null;
        if (filename.endsWith(".tga")) image = TGAFile.read(channel); else image = BuiltinFile.read(channel);
        if (image == null) throw new LoadException("Unable to load Image2D; path: " + filename);
        resources.put(name, image);
    }
