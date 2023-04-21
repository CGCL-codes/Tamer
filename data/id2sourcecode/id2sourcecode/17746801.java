    public CourseUnpacker(URL u) {
        url = u;
        jar = null;
        try {
            JarURLConnection con = (JarURLConnection) url.openConnection();
            jar = con.getJarFile();
        } catch (IOException e) {
            MinigolfException.throwToDisplay(e);
        }
    }
