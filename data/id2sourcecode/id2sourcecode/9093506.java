        private Thread save(Locator loc, File location) {
            if (loc == null) return null;
            try {
                System.out.println("Loc: " + loc + ", " + location);
                System.out.println(loc.getName());
                File out = new File(location, loc.getName());
                InputStream r = null;
                if (loc.isURL()) r = loc.url().openStream(); else r = new FileInputStream(loc.file());
                InputStream in = new ProgressMonitorInputStream(m.getGUIManager().getParent(), "Downloading file " + loc.getName(), r);
                FileOutputStream fos = new FileOutputStream(out);
                return copy(in, fos);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
