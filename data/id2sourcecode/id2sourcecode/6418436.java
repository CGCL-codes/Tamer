    public static void main(String[] args) throws IOException, OgnlscriptCompileException {
        Map contextValues = new HashMap();
        contextValues.put("ctx1", "valctx1");
        Map mapa = new HashMap();
        mapa.put("atributo", "valor");
        Map root = new HashMap();
        root.put("mapa", mapa);
        long start = System.currentTimeMillis();
        Ognlscript ognlscript = new Ognlscript(JOINLINECHAR, STARTSINGLECOMMENTLINE, STARTMULTICOMMENT, ENDMULTICOMMENT, CR);
        ognlscript.setGrantClasses(GRANTCLASSES);
        FunctionBlock cb = ognlscript.digest(new File("c:\\work\\Proy\\Local\\Aventura\\doc\\testtodo.txt"), null);
        long COUNT = 1000;
        System.out.println("----- EXECUTE ----");
        OgnlscriptContext context = new OgnlscriptContext(new HashMap());
        context.setRoot(root);
        ResultObject result = null;
        try {
            result = ognlscript.execute(cb, context);
        } catch (OgnlscriptRuntimeException e) {
            e.printStackTrace();
        }
        System.out.println("----- RESULTS ----");
        System.out.println("Return:" + (result != null ? result.getResult() : null) + " (" + (result == null ? "no se devolvio nada" : (result.getResult() == null ? "null" : result.getResult().getClass().toString())) + ")");
        System.out.println("StdOut:" + context.getResponse().getStdOut());
        System.out.println("StdErr:" + context.getResponse().getStdErr());
        long end = System.currentTimeMillis() - start;
        System.out.println("Total " + end + " ms");
        COUNT = COUNT * 1000;
        System.out.println((double) ((double) end / (double) COUNT) + " seg cada iteracion");
        System.out.println("---------");
        System.out.println("root " + root);
        System.out.println("context " + context.getValues());
    }
