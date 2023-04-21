    public static Version getLatestVersion() {
        if (latest != null) return latest;
        try {
            URL url = new URL("http://cvjmirror.sourceforge.net/version.txt");
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            int iMajor = Integer.parseInt(in.readLine().trim());
            int iMinor = Integer.parseInt(in.readLine().trim());
            int iTag = 0;
            try {
                iTag = Integer.parseInt(in.readLine().trim());
            } catch (Exception ex) {
            }
            latest = new Version(iMajor, iMinor, iTag);
        } catch (MalformedURLException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return latest;
    }
