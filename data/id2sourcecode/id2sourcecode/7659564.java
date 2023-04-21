    public static void copyContent(IVariable rootVariable, String exportDirectory, int outputFormat) throws MM4UToolsException {
        VariableList varList = null;
        if (rootVariable instanceof IBasicOperator) {
            varList = (VariableList) rootVariable.getVariables();
        } else if (rootVariable instanceof IComplexOperator) {
            IComplexOperator tempComplexOperator = (IComplexOperator) rootVariable;
            copyContent(tempComplexOperator.getRootOperator(), exportDirectory, outputFormat);
        }
        if (varList == null) return;
        for (Iterator iter = varList.iterator(); iter.hasNext(); ) {
            AbstractVariable element = (AbstractVariable) iter.next();
            if ((element instanceof IVideo) || (element instanceof IImage) || (element instanceof IAudio)) {
                IMedium medium = (IMedium) element;
                String uri = medium.getURI();
                String uniqueMediaName = uri.hashCode() + "." + Utilities.getURISuffix(uri);
                String outputPathAndFilename = exportDirectory + EXPORT_MEDIA_DIR + File.separator + uniqueMediaName;
                Debug.println("CopyMediaElements->copyContent: media uri: " + uri + " - unique: " + uniqueMediaName);
                if (!(new File(outputPathAndFilename)).exists()) {
                    try {
                        URL url = new URL(uri);
                        Debug.println("Source file: " + url.toString());
                        Debug.println("Destination file: " + outputPathAndFilename);
                        InputStream srcStream = url.openStream();
                        FileOutputStream output = new FileOutputStream(outputPathAndFilename);
                        Utilities.copy(srcStream, output);
                    } catch (IOException excp) {
                        throw new MM4UToolsException(null, "copyContent", "Could not copy the media:\n" + excp);
                    }
                }
                File output = new File(outputPathAndFilename);
                try {
                    if (outputFormat == GeneratorToolkit.SMIL2_0 || outputFormat == GeneratorToolkit.SMIL2_0_BASIC_LANGUAGE_PROFILE || outputFormat == GeneratorToolkit.REALPLAYER_SMIL2_0 || outputFormat == GeneratorToolkit.REALPLAYER_SMIL2_0_BASIC_LANGUAGE_PROFILE || outputFormat == GeneratorToolkit.XMT_OMEGA) {
                        medium.setURI(EXPORT_MEDIA_DIR + "/" + uniqueMediaName);
                    } else medium.setURI(output.toURL().toString());
                } catch (MalformedURLException excp) {
                    throw new MM4UToolsException(null, "copyContent", "Could not transform the filename " + outputPathAndFilename + " into an url.");
                }
            }
            copyContent(element, exportDirectory, outputFormat);
        }
    }
