    public PointFeature loadText(BufferedReader in, String Str) throws IOException {
        float x = 0, y = 0;
        String DataStr = "";
        int DataMark = ' ';
        int pos = 0;
        String StrX1, StrY1, StrX2, StrY2;
        float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        int Text_index = Str.trim().indexOf(sTEXT);
        if (Text_index == -1) {
            log.logError("Expected PLine found: \"" + Str + "\"");
        } else {
            DataStr = Str.substring(Text_index + sTEXT.length()).trim();
        }
        String TextContent = DataStr.substring(1, DataStr.length() - 1);
        TextCodes.addElement(TextContent);
        DataStr = in.readLine().trim();
        StringTokenizer tok = new StringTokenizer(DataStr, " ");
        int number_tok = tok.countTokens();
        StrX1 = tok.nextToken();
        StrY1 = tok.nextToken();
        StrX2 = tok.nextToken();
        StrY2 = tok.nextToken();
        x1 = Float.valueOf(StrX1).floatValue();
        y1 = Float.valueOf(StrY1).floatValue() * -1;
        x2 = Float.valueOf(StrX2).floatValue();
        y2 = Float.valueOf(StrY2).floatValue() * -1;
        x = (x1 + x2) / 2;
        y = (y1 + y2) / 2;
        return new PointFeature(new Point2D.Float(x, y));
    }
