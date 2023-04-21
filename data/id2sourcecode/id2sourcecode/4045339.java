    public static ReadableByteChannel getChannel(final Representation representation) throws IOException {
        ReadableByteChannel result = null;
        if (Edition.CURRENT != Edition.GAE) {
            final java.nio.channels.Pipe pipe = java.nio.channels.Pipe.open();
            final org.restlet.Application application = org.restlet.Application.getCurrent();
            application.getTaskService().execute(new Runnable() {

                public void run() {
                    try {
                        WritableByteChannel wbc = pipe.sink();
                        representation.write(wbc);
                        wbc.close();
                    } catch (IOException ioe) {
                        Context.getCurrentLogger().log(Level.FINE, "Error while writing to the piped channel.", ioe);
                    }
                }
            });
            result = pipe.source();
        } else {
            Context.getCurrentLogger().log(Level.WARNING, "The GAE edition is unable to return a channel for a representation given its write(WritableByteChannel) method.");
        }
        return result;
    }
