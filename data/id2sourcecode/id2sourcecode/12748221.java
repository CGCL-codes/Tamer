    private MD5Model loadModel(InputStream in, String modelFileName, String skin) throws IOException, IncorrectFormatException, ParsingErrorException {
        MD5MeshModel prototype = MD5Reader.readMeshFile(in);
        List<URL> animResources = new ResourceLocator(getBaseURL()).findAllResources("md5anim", true, false);
        HashMap<String, MD5Animation> animsMap = new HashMap<String, MD5Animation>();
        for (URL url : animResources) {
            MD5Animation protoAnim = MD5AnimationReader.readAnimFile(url.openStream());
            final int lastSlash = url.getFile().lastIndexOf('/');
            final String filename;
            if (lastSlash < 0) filename = url.getFile().substring(5); else filename = url.getFile().substring(lastSlash + 1);
            animsMap.put(filename, protoAnim);
        }
        ArrayList<MD5RenderMesh> renderMeshes = new ArrayList<MD5RenderMesh>();
        for (MD5Mesh mesh : prototype.getMeshes()) renderMeshes.add(new MD5RenderMesh(prototype, mesh));
        MD5Model model = new MD5Model(prototype, animsMap, renderMeshes, skin, getFlags());
        return (model);
    }
