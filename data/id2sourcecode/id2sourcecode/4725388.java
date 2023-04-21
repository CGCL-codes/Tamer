    public int cmdFrominstall(Dictionary opts, Reader in, PrintWriter out, Session session) {
        String fromURL = (String) opts.get("url");
        String loc = (String) opts.get("location");
        if (loc == null) {
            loc = fromURL;
        }
        try {
            URL url = new URL(fromURL);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            Bundle b = bc.installBundle(loc, inStream);
            out.println("Installed: " + showBundle(b));
        } catch (BundleException e) {
            Throwable t = e;
            while (t instanceof BundleException && ((BundleException) t).getNestedException() != null) t = ((BundleException) t).getNestedException();
            out.println("Couldn't install: url=" + fromURL + ", location=" + loc + " (due to: " + t + ")");
        } catch (Exception e) {
            out.println("Couldn't install: url=" + fromURL + ", location=" + loc + " (due to: " + e + ")");
        }
        return 0;
    }
