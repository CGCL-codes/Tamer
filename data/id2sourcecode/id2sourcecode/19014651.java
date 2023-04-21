    private static int zipRecurs(final File _rootDir, final File _dir, final ZipOutputStream _zos, ProgressionInterface _prog, int nbFiles, int indFile) throws IOException {
        URI root = _rootDir.toURI();
        for (File f : _dir.listFiles()) {
            String name = root.relativize(f.toURI()).getPath();
            if (f.isDirectory()) {
                _zos.putNextEntry(new ZipEntry(name.endsWith("/") ? name : name + "/"));
                _zos.closeEntry();
                indFile = zipRecurs(_rootDir, f, _zos, _prog, nbFiles, indFile);
            } else {
                _zos.putNextEntry(new ZipEntry(name));
                FileInputStream finp = new FileInputStream(f);
                copyStream(finp, _zos, true, false);
                _zos.closeEntry();
            }
            if (_prog != null) {
                indFile++;
                _prog.setProgression((int) ((indFile / (double) nbFiles) * 85) + 10);
            }
        }
        return indFile;
    }
