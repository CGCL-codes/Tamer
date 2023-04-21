    private void sendServerRequest_new(String vergleich) throws IOException {
        String tmp = "http://greententacle.techfak.uni-bielefeld.de:5171/sparql?default-graph-uri=&query=PREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+res%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%0D%0A%0D%0ASELECT+DISTINCT+%3Fp+%3Fl+WHERE++{%0D%0A+{+res%3ABerlin+%3Fp+%3Fo+.+}%0D%0A+UNION%0D%0A+{+%3Fs+%3Fp+res%3A" + vergleich + "+.+}%0D%0A+{+%3Fp+rdfs%3Alabel+%3Fl+.+}%0D%0A}%0D%0A&format=text%2Fhtml&debug=on&timeout=";
        URL url;
        InputStream is;
        InputStreamReader isr;
        BufferedReader r;
        String str;
        String result = "";
        try {
            url = new URL(tmp);
            is = url.openStream();
            isr = new InputStreamReader(is);
            r = new BufferedReader(isr);
            do {
                str = r.readLine();
                if (str != null) result = result + str;
            } while (str != null);
        } catch (MalformedURLException e) {
            System.out.println("Must enter a valid URL");
        } catch (IOException e) {
            System.out.println("Can not connect");
        }
        FileWriter w = new FileWriter("answer");
        w.write(result);
        w.close();
    }
