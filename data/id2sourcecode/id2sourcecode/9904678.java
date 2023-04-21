    private void writeJIPconfigFile(long time) throws IOException {
        FileWriter w = new FileWriter(new File(makeConfigfileName(time)));
        w.write("ClassLoaderFilter.1=com.mentorgen.tools.profile.instrument.clfilter.StandardClassLoaderFilter\n");
        w.write("thread.compact.threshold.ms=1\n");
        w.write("method.compact.threshold.ms=1\n");
        w.write("file=");
        w.write(makeJIPOutFilename(time) + "\n");
        w.write("track.object.alloc=on\n");
        w.write("output=text\n");
        w.write("output-method-signatures=yes");
        w.flush();
        w.close();
    }
