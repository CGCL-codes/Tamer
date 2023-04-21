    public void handleRequest(BufferedReader bufferedreader, InputStream inputstream, BufferedOutputStream bufferedoutputstream, PrintWriter printwriter, String s, HashMap hashmap) {
        Hashtable hashtable = new Hashtable();
        String s1 = s;
        System.out.println(s);
        StringTokenizer stringtokenizer = new StringTokenizer(s);
        stringtokenizer.nextToken();
        s = stringtokenizer.nextToken();
        if (s.indexOf("?") >= 0) {
            int i = s.indexOf("?");
            String s2 = s.substring(i + 1);
            s = s.substring(0, i);
            System.out.println(s);
            hashtable = parseParams(s2);
        }
        if (s.endsWith("/AxisServlet") || s.endsWith(".jws") || s.endsWith("?wsdl")) {
            LombokServletContext lombokservletcontext = context.getAxisContext();
            HttpServlet httpservlet = (HttpServlet) lombokservletcontext.getServlet(0);
            LombokServletRequest lombokservletrequest = new LombokServletRequest(s, s1, hashmap, hashtable, bufferedreader);
            LombokServletResponse lombokservletresponse = new LombokServletResponse(bufferedoutputstream, printwriter);
            try {
                httpservlet.service(lombokservletrequest, lombokservletresponse);
                printwriter.flush();
                bufferedoutputstream.flush();
            } catch (Exception exception) {
                System.out.println("cannot access servlet: " + exception);
                exception.printStackTrace();
                exception.printStackTrace(printwriter);
                outputError(printwriter, exception);
            }
        } else if (s.endsWith("/admin/") || s.endsWith("/admin")) {
            LombokServletContext adminContext = context.getAdminContext();
            HttpServlet adminServlet = (HttpServlet) adminContext.getServlet(0);
            LombokServletRequest lombokservletrequest1 = new LombokServletRequest(s, s1, hashmap, hashtable, bufferedreader);
            LombokServletResponse lombokservletresponse1 = new LombokServletResponse(bufferedoutputstream, printwriter);
            try {
                adminServlet.service(lombokservletrequest1, lombokservletresponse1);
                printwriter.flush();
                bufferedoutputstream.flush();
            } catch (Exception exception1) {
                System.out.println("cannot access servlet: " + exception1);
                exception1.printStackTrace();
                exception1.printStackTrace(printwriter);
                outputError(printwriter, exception1);
            }
        } else if (s.length() >= 10 && s.substring(0, 9).equals("/services")) {
            LombokServletContext lombokservletcontext2 = context.getAxisContext();
            HttpServlet httpservlet2 = (HttpServlet) lombokservletcontext2.getServlet(0);
            LombokServletRequest lombokservletrequest2 = new LombokServletRequest(s, s1, hashmap, hashtable, bufferedreader);
            LombokServletResponse lombokservletresponse2 = new LombokServletResponse(bufferedoutputstream, printwriter);
            try {
                httpservlet2.service(lombokservletrequest2, lombokservletresponse2);
                printwriter.flush();
                bufferedoutputstream.flush();
            } catch (Exception exception2) {
                System.out.println("cannot access servlet: " + exception2);
                exception2.printStackTrace();
                exception2.printStackTrace(printwriter);
                outputError(printwriter, exception2);
            }
        } else {
            try {
                File file = new File(root.getAbsolutePath() + "/webapps/axis/index.html");
                FileInputStream fileinputstream = new FileInputStream(file);
                int j = fileinputstream.available();
                for (int k = 0; k < j; k++) bufferedoutputstream.write(fileinputstream.read());
                fileinputstream.close();
                bufferedoutputstream.flush();
                printwriter.close();
            } catch (IOException ioexception) {
                System.out.println("Problems outputting the file :" + ioexception);
                outputError(printwriter, ioexception);
            }
        }
    }
