    public DicomDic(String dicURL) {
        if (debug_level > 3) System.out.println("Now Loading DicomDic from " + dicURL + "...");
        InputStream inS;
        BufferedReader din;
        StringTokenizer tkn;
        try {
            if (debug_level > 3) System.out.println("Now Making Resive Stream....");
            if (dicURL.equals("none")) {
                inS = this.getClass().getResourceAsStream("Dicom.dic");
            } else {
                URL urlConn = new URL(dicURL);
                inS = urlConn.openStream();
            }
            din = new BufferedReader(new InputStreamReader(inS));
            String line_str;
            int j = 0;
            while ((line_str = din.readLine()) != null) {
                if (line_str.startsWith("#") != true) {
                    j++;
                    String tag = null;
                    Dicomvalue dicomvalue = new Dicomvalue();
                    tkn = new StringTokenizer(line_str);
                    tag = tkn.nextToken();
                    if (debug_level > 5) System.out.println("Tag  : " + tag);
                    dicomvalue.vr = tkn.nextToken();
                    if (debug_level > 5) System.out.println("VR   : " + dicomvalue.vr);
                    dicomvalue.name = tkn.nextToken();
                    if (debug_level > 5) System.out.println("Name : " + dicomvalue.name);
                    dicomvalue.vm = tkn.nextToken();
                    if (debug_level > 5) System.out.println("VM   : " + dicomvalue.vm);
                    dicomvalue.version = tkn.nextToken();
                    if (debug_level > 5) System.out.println("Ver. : " + dicomvalue.version);
                    table.put(tag, dicomvalue);
                }
            }
            din.close();
        } catch (EOFException eof) {
            System.out.println("EOFException: " + eof.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
