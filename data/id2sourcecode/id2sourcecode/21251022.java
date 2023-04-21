    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java FormatImpl inputfile");
            System.exit(0);
        }
        Format format = null;
        try {
            URL url = new URL(Util.makeAbsoluteURL(args[0]));
            BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream()));
            int idx = args[0].indexOf(".");
            String id = (idx == -1) ? args[0] : args[0].substring(0, idx);
            idx = id.lastIndexOf("\\");
            if (idx != -1) id = id.substring(idx + 1);
            format = new FormatImpl(bReader, id);
        } catch (Exception e) {
            System.out.println("Format failed: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }
