    public String digest(final IEditorInput input) {
        String result = null;
        if (input instanceof IFileEditorInput) {
            if (digesterService != null) {
                try {
                    result = digesterService.digest(((IFileEditorInput) input).getFile().getContents());
                } catch (final Exception e) {
                    Activator.log(Status.ERROR, "Could not generate digest for resource " + input);
                }
            }
        }
        return result;
    }
