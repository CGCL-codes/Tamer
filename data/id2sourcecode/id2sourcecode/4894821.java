    public static void save(File confFile, Class<?> containerClass, String comment) throws IOException {
        boolean useWinSep = Util.IS_WINDOWS || AppUtil.isPortable();
        String lineSep = useWinSep ? "\r\n" : "\n";
        if (useWinSep) comment = Util.ensureWindowsLineSep(comment.trim()); else comment = Util.ensureLinuxLineSep(comment.trim());
        BufferedWriter out = null;
        try {
            FileOutputStream fout = new FileOutputStream(confFile, false);
            FileLock lock = fout.getChannel().lock();
            try {
                out = new BufferedWriter(new OutputStreamWriter(fout, "utf-8"));
                out.write(comment);
                out.write(lineSep);
                out.write("#");
                out.write(lineSep);
                out.write("# ");
                out.write(new Date().toString());
                out.write(lineSep);
                out.write(lineSep);
                int i = 0;
                for (Class<? extends Storable> clazz : ConfLoader.<Storable>getEnums(containerClass)) {
                    Storable[] entries = clazz.getEnumConstants();
                    if (entries.length == 0) continue;
                    if (i++ > 0) {
                        out.write(lineSep);
                        out.write(lineSep);
                    }
                    String description = clazz.getAnnotation(Description.class).value();
                    if (useWinSep) description = Util.ensureWindowsLineSep(description); else description = Util.ensureLinuxLineSep(description);
                    out.write(description);
                    out.write(lineSep);
                    int j = 0;
                    for (Storable entry : entries) {
                        if (j++ > 0) out.write(lineSep);
                        String key = convert(entry.name(), true);
                        String value = convert(entry.valueToString(), false);
                        out.write(key + " = " + value);
                    }
                }
            } finally {
                lock.release();
            }
        } finally {
            Closeables.closeQuietly(out);
        }
    }
