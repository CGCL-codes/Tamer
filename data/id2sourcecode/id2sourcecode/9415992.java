    public static boolean evaluate(Context context, Writer writer, String logTag, Reader reader) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
        SimpleNode nodeTree = null;
        try {
            nodeTree = RuntimeSingleton.parse(reader, logTag);
        } catch (ParseException pex) {
            throw new ParseErrorException(pex);
        } catch (TemplateInitException pex) {
            throw new ParseErrorException(pex);
        }
        if (nodeTree != null) {
            InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);
            ica.pushCurrentTemplateName(logTag);
            try {
                try {
                    nodeTree.init(ica, RuntimeSingleton.getRuntimeServices());
                } catch (TemplateInitException pex) {
                    throw new ParseErrorException(pex);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    getLog().error("Velocity.evaluate() : init exception for tag = " + logTag, e);
                }
                nodeTree.render(ica, writer);
            } finally {
                ica.popCurrentTemplateName();
            }
            return true;
        }
        return false;
    }
