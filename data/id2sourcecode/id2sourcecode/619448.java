        public boolean process(Set<? extends TypeElement> annots, RoundEnvironment renv) {
            round++;
            System.err.println("Round " + round + " annots:" + annots + " rootElems:" + renv.getRootElements());
            String gen = getOption("gen");
            if (round == 1 && gen != null) {
                try {
                    Filer filer = processingEnv.getFiler();
                    JavaFileObject f;
                    if (gen.endsWith(".java")) f = filer.createSourceFile("p.package-info"); else f = filer.createClassFile("p.package-info");
                    System.err.println("copy " + gen + " to " + f.getName());
                    write(f, read(new File(gen)));
                } catch (IOException e) {
                    error("Cannot create package-info file: " + e);
                }
            }
            if (renv.processingOver()) {
                System.err.println("final round");
                Elements eu = processingEnv.getElementUtils();
                TypeElement te = eu.getTypeElement("p.Test");
                PackageElement pe = eu.getPackageOf(te);
                System.err.println("final: te:" + te + " pe:" + pe);
                List<? extends AnnotationMirror> annos = pe.getAnnotationMirrors();
                System.err.println("final: annos:" + annos);
                if (annos.size() == 1) {
                    String expect = "@" + te + "(\"" + getOption("expect") + "\")";
                    String actual = annos.get(0).toString();
                    checkEqual("package annotations", actual, expect);
                } else {
                    error("Wrong number of annotations found: (" + annos.size() + ") " + annos);
                }
            }
            return true;
        }
