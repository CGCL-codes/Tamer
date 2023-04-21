    public static void main(String[] args) {
        System.out.println("<<<START>>>");
        try {
            ArrayList<Finders> y = new ArrayList<Finders>();
            y.add(new Finder(new String(")\r\n{"), new Converter_replaceString("){")));
            y.add(new Finder(new String(") \r\n{"), new Converter_replaceString("){")));
            y.add(new Finder(") \n{", new Converter_replaceString("){")));
            y.add(new Finder(") \r\n\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r\n	{", new Converter_replaceString("){")));
            y.add(new Finder(")\n	{", new Converter_replaceString("){")));
            y.add(new Finder(")/s\r\n\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r	{", new Converter_replaceString("){")));
            y.add(new Finder("\r\n	{", new Converter_replaceString("{")));
            y.add(new Finder("\r\n		{", new Converter_replaceString("{")));
            y.add(new Finder("\r\n			{", new Converter_replaceString("{")));
            y.add(new Finder("\r\n				{", new Converter_replaceString("{")));
            y.add(new Finder("\r\n					{", new Converter_replaceString("{")));
            y.add(new Finder("\r\n						{", new Converter_replaceString("{")));
            y.add(new Finder(")\r	{", new Converter_replaceString("else")));
            y.add(new Finder("\r\n	else", new Converter_replaceString("else")));
            y.add(new Finder("\r\n		else", new Converter_replaceString("else")));
            y.add(new Finder("\r\n			else", new Converter_replaceString("else")));
            y.add(new Finder("\r\n				else", new Converter_replaceString("else")));
            y.add(new Finder("\r\n					else", new Converter_replaceString("else")));
            y.add(new Finder("\r\n						else", new Converter_replaceString("else")));
            y.add(new Finder("\n\n	{", new Converter_replaceString("){")));
            y.add(new Finder(")\n\t\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r\n\t\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\n\t\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r\n\t\t\t{", new Converter_replaceString("){")));
            y.add(new Finder(")\r\n\t\t\t\t{", new Converter_replaceString("){")));
            y.add(new Finder("do\r\n{", new Converter_replaceString("do{")));
            y.add(new Finder("do\r\n\t{", new Converter_replaceString("do{")));
            y.add(new Finder("do\r\n\t\t{", new Converter_replaceString("do{")));
            y.add(new Finder("do\r\n\t\t\t{", new Converter_replaceString("do{")));
            y.add(new Finder("do\r\n\t\t\t\t{", new Converter_replaceString("do{")));
            y.add(new Finder("}\r\nwhile", new Converter_replaceString("}while")));
            y.add(new Finder("}\r\n\twhile", new Converter_replaceString("}while")));
            y.add(new Finder("}\r\n\t\twhile", new Converter_replaceString("}while")));
            y.add(new Finder("}\r\n\t\t\twhile", new Converter_replaceString("}while")));
            y.add(new Finder("}\r\n\t\t\t\twhile", new Converter_replaceString("}while")));
            y.add(new Finder(")\r\n\r\n{", new Converter_replaceString("){")));
            y.add(new Finder(new String("}\r\nelse\r\n{"), new Converter_replaceString("}else{")));
            y.add(new Finder(new String("}\r\n\telse\r\n\t{"), new Converter_replaceString("}else{")));
            y.add(new Finder(new String("\r\n		 else\r\n\t\t{"), new Converter_replaceString("else{")));
            y.add(new Finder("\r\n\t\telse", new Converter_replaceString("else")));
            y.add(new Finder("} \r\n		else\r\n\\t\t{", new Converter_replaceString("}else{")));
            y.add(new Finder(new String("}\r\n\t\t\telse\r\n\t\t\t{"), new Converter_replaceString("}else{")));
            y.add(new Finder(new String("}\r\nelse\r\n{"), new Converter_replaceString("}else{")));
            y.add(new Finder("else\r\n{", new Converter_replaceString("else{")));
            y.add(new Finder("\r\n \r\n", new Converter_replaceString(" ")));
            y.add(new Finder("\r\n\r\n", new Converter_replaceString("\r\n")));
            y.add(new Finder("\r\n*\r\n", new Converter_replaceString("\r\n")));
            y.add(new Finder("return true;", new Converter_replaceString("return true;//TODO clean return msg")));
            y.add(new Finder("return false;", new Converter_replaceString("return false;//TODO clean return msg")));
            File dest = new File("E:/_A_servers/xampp/htdocs/automaticsitev2.6/intersante scripts/test2.tuned.php");
            File source = new File("E:/_A_servers/xampp/htdocs/automaticsitev2.6/intersante scripts/test2.php");
            String encoding = "UTF8";
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dest), encoding);
            FileChannel channel = new RandomAccessFile(source, "rw").getChannel();
            InputStream is = Channels.newInputStream(channel);
            x = new StreamMultiManipulatorCR1(10, DEBUGMODE.NORMAL);
            x.findAndReplace(is, y, writer);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("file not exist or is opened by other program");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n<<<STOP:TEST>>>");
    }
