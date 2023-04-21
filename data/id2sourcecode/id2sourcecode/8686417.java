    public boolean eval() throws BuildException {
        if (spec == null) {
            throw new BuildException("No url specified in http condition");
        }
        log("Checking for " + spec, Project.MSG_VERBOSE);
        try {
            URL url = new URL(spec);
            try {
                URLConnection conn = url.openConnection();
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection http = (HttpURLConnection) conn;
                    http.setRequestMethod(requestMethod);
                    int code = http.getResponseCode();
                    log("Result code for " + spec + " was " + code, Project.MSG_VERBOSE);
                    if (code > 0 && code < errorsBeginAt) {
                        return true;
                    }
                    return false;
                }
            } catch (java.net.ProtocolException pe) {
                throw new BuildException("Invalid HTTP protocol: " + requestMethod, pe);
            } catch (java.io.IOException e) {
                return false;
            }
        } catch (MalformedURLException e) {
            throw new BuildException("Badly formed URL: " + spec, e);
        }
        return true;
    }
