    public static void processFile(Context cx, Scriptable scope, String filename) {
        Reader in;
        try {
            URL url = new URL(filename);
            InputStream is = url.openStream();
            in = new BufferedReader(new InputStreamReader(is));
        } catch (MalformedURLException mfex) {
            in = null;
        } catch (IOException ioex) {
            Context.reportError(IVErrorReporter.getMessage("msg.couldnt.open.url", filename, ioex.toString()));
            exitCode = EXITCODE_FILE_NOT_FOUND;
            return;
        }
        if (in == null) {
            try {
                in = new FileReader(filename);
                filename = new java.io.File(filename).getCanonicalPath();
            } catch (FileNotFoundException ex) {
                Context.reportError(IVErrorReporter.getMessage("msg.couldnt.open", filename));
                exitCode = EXITCODE_FILE_NOT_FOUND;
                return;
            } catch (IOException ioe) {
                Log.logRB(Resource.JSERROR, ioe);
            }
        }
        evaluateReader(cx, scope, in, filename, 1);
    }
