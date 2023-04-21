    private static SchemeBoolean c35(StdlibProcedure p, SchemeObject... args) {
        p.checkArity(args.length);
        if (!((args[0]) instanceof SchemeString)) {
            String msg = String.format(TYPE_ERROR_FMT, args[0], 1, p);
            throw new IllegalArgumentException(msg);
        }
        try {
            Process q = Runtime.getRuntime().exec(args[0].toDisplayString());
            InputStream in = q.getInputStream();
            q.waitFor();
            Scanner sin = new Scanner(in);
            while (sin.hasNextLine()) {
                System.out.println(sin.nextLine());
            }
            return q.exitValue() == 0 ? SchemeBoolean.TRUE : SchemeBoolean.FALSE;
        } catch (IOException e) {
            e.printStackTrace();
            return SchemeBoolean.FALSE;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return SchemeBoolean.FALSE;
        }
    }
