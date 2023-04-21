    protected String execute(String cmd) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        BufferedInputStream in = null;
        StringWriter out = null;
        try {
            p = r.exec(cmd);
            in = new BufferedInputStream(p.getErrorStream());
            out = new StringWriter();
            int c;
            while ((c = in.read()) != -1) out.write(c);
            out.close();
            in.close();
            return out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return out == null ? null : out.toString();
        } finally {
            if (out != null) try {
                out.close();
            } catch (Exception e) {
            }
            if (in != null) try {
                in.close();
            } catch (Exception e) {
            }
        }
    }
