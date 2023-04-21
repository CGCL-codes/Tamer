    public void run() {
        new Thread(new Runnable() {

            public void run() {
                view.getStatus().setMessage("Preparing download...");
                if (!getPackages()) {
                    GUIUtilities.error(view, "codebook.msg.invalid-api-download-url", null);
                    return;
                }
                LinkedList<String> pagelist = new LinkedList<String>();
                pagelist.add(url + "index.html");
                pagelist.add(url + "allclasses-frame.html");
                pagelist.add(url + "allclasses-noframe.html");
                pagelist.add(url + "constant-values.html");
                pagelist.add(url + "deprecated-list.html");
                pagelist.add(url + "help-doc.html");
                pagelist.add(url + "overview-frame.html");
                pagelist.add(url + "overview-summary.html");
                pagelist.add(url + "overview-tree.html");
                pagelist.add(url + "package-list");
                pagelist.add(url + "serialized-form.html");
                pagelist.add(url + "stylesheet.css");
                pagelist.add(url + "toc.xml");
                pagelist.add(url + "resources/inherit.gif");
                String pkg;
                while ((pkg = packages.poll()) != null) {
                    String pkgurl = url + pkg.replace(".", "/") + "/";
                    pagelist.add(pkgurl + "package-frame.html");
                    pagelist.add(pkgurl + "package-summary.html");
                    pagelist.add(pkgurl + "package-use.html");
                    pagelist.add(pkgurl + "package-tree.html");
                    try {
                        URLConnection con = new URL(pkgurl + "package-frame.html").openConnection();
                        Scanner read = new Scanner(new InputStreamReader(con.getInputStream()));
                        String line = null;
                        while (read.hasNextLine()) {
                            line = read.nextLine();
                            if (line.toUpperCase().startsWith("<A HREF=\"")) {
                                String pagename = line.substring(9, line.indexOf("\"", 10));
                                if (pagename.endsWith("package-summary.html")) continue;
                                pagelist.add(pkgurl + pagename);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                view.getStatus().setMessage("Choose a target directory");
                VFSFileChooserDialog dialog = new VFSFileChooserDialog(view, jEdit.getSettingsDirectory(), VFSBrowser.CHOOSE_DIRECTORY_DIALOG, false);
                String[] files = dialog.getSelectedFiles();
                if (files == null) {
                    view.getStatus().setMessageAndClear("Download cancelled");
                    return;
                }
                String target = files[0];
                if (!target.endsWith(File.separator)) target += File.separator;
                int i = 0;
                int total = pagelist.size();
                for (String page : pagelist) {
                    view.getStatus().setMessage("(" + i + " / " + total + ") Downloading " + page + "...");
                    i++;
                    System.out.println("Downloading " + i + " of " + total + " (" + page + ")");
                    String path = target + page.substring(url.length());
                    try {
                        URLConnection con = new URL(page).openConnection();
                        File f = new File(path);
                        new File(f.getParent()).mkdirs();
                        f.createNewFile();
                        FileOutputStream fout = new FileOutputStream(f);
                        InputStream fin = con.getInputStream();
                        int b = -1;
                        while ((b = fin.read()) != -1) {
                            fout.write(b);
                        }
                        fout.close();
                    } catch (Exception e) {
                    }
                }
                view.getStatus().setMessage("Api at " + url + " downloaded to " + target);
            }
        }).start();
    }
