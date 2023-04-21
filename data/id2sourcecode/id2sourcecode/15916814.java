    public void writeBody(PrintWriter body) throws IOException {
        String ejbname = request.getFormParameter("ejbname");
        String ejbdesc = request.getFormParameter("ejbdesc");
        String ejbauth = request.getFormParameter("ejbauth");
        String ejbpack = request.getFormParameter("ejbpack");
        String ejbtype = request.getFormParameter("ejbtype");
        String ejbsloc = request.getFormParameter("ejbsloc");
        String generate = request.getFormParameter("generate");
        String ejbstyp = request.getFormParameter("ejbstyp");
        boolean error = false;
        if (generate != null) {
            if (checkSourceDir(ejbsloc)) {
                error = false;
            } else {
                error = true;
            }
            if (error) {
                writeGUI(body, true);
            } else {
                if (ejbstyp.equals("local")) {
                    surl = ejbsloc + psep;
                } else {
                    surl = "/BeanSources/";
                }
                if (ejbtype.equals("BMP")) {
                    BMPBean newEJB = new BMPBean(ejbname, ejbdesc, ejbauth, ejbpack, ejbsloc, ejbstyp);
                    showBeanFiles(body);
                    System.out.println("EJB Generator has created " + ejbname + "!");
                } else if (ejbtype.equals("CMP")) {
                    CMPBean newEJB = new CMPBean(ejbname, ejbdesc, ejbauth, ejbpack, ejbsloc, ejbstyp);
                    showBeanFiles(body);
                    System.out.println("EJB Generator has created " + ejbname + "!");
                } else if (ejbtype.equals("Stateful")) {
                    StatefulBean newEJB = new StatefulBean(ejbname, ejbdesc, ejbauth, ejbpack, ejbsloc, ejbstyp);
                    showBeanFiles(body);
                    System.out.println("EJB Generator has created " + ejbname + "!");
                } else {
                    StatelessBean newEJB = new StatelessBean(ejbname, ejbdesc, ejbauth, ejbpack, ejbsloc, ejbstyp);
                    showBeanFiles(body);
                    System.out.println("EJB Generator has created " + ejbname + "!");
                }
                if (ejbstyp.equals("remote")) {
                    File nsdir = new File(oehp + psep + "htdocs" + psep + "EJBGenerator" + psep + "BeanSources");
                    if (!nsdir.exists()) {
                        nsdir.mkdirs();
                    }
                    File nzdir = new File(nsdir, ejbname);
                    if (!nzdir.exists()) {
                        nzdir.mkdirs();
                    }
                    FileUtils.copyFile(new File(nzdir, ejbname + ".zip"), new File(ejbsloc + psep + ejbname + psep + ejbname + ".zip"));
                }
            }
        } else {
            writeGUI(body, false);
        }
    }
