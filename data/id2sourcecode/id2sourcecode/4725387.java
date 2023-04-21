    public int cmdFromupdate(Dictionary opts, Reader in, PrintWriter out, Session session) {
        String bname = (String) opts.get("bundle");
        Bundle[] bl = getBundles(new String[] { bname }, true);
        String fromURL = (String) opts.get("url");
        Bundle b = bl[0];
        if (b == null) {
            out.println("ERROR! No matching bundle for '" + bname + "'");
            return 1;
        }
        try {
            URL url = new URL(fromURL);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            b.update(inStream);
            out.println("Updated: " + showBundle(b));
        } catch (BundleException e) {
            Throwable t = e;
            while (t instanceof BundleException && ((BundleException) t).getNestedException() != null) t = ((BundleException) t).getNestedException();
            out.println("Couldn't update: " + showBundle(b) + " (due to: " + t + ")");
        } catch (Exception e) {
            out.println("Couldn't update: " + showBundle(b) + " (due to: " + e + ")");
        }
        out.println("Note: Use refresh command to update exported packages in running bundles");
        return 0;
    }
