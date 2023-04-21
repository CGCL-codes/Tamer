    public static void main(String args[]) {
        try {
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            url = new URL("https://www.google.com/accounts/ClientLogin");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            String params[][] = { { "accountType", "HOSTED_OR_GOOGLE" }, { "Email", "sachintheonly@gmail.com" }, { "Passwd", "mumbhai" }, { "service", "xapi" }, { "source", "Gulp-CalGulp-1.05" } };
            StringBuffer parameterString = new StringBuffer();
            for (int i = 0; i < params.length; i++) {
                parameterString.append(params[i][0] + "=");
                parameterString.append(URLEncoder.encode(params[i][1]) + "&");
            }
            printout.writeBytes(parameterString.toString());
            printout.flush();
            printout.close();
            input = new DataInputStream(urlConn.getInputStream());
            String str;
            while (null != ((str = input.readLine()))) {
                System.out.println(str);
            }
            input.close();
        } catch (MalformedURLException me) {
            System.err.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
