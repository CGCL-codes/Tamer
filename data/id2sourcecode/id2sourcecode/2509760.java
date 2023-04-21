    public void writeTab(String[] titles, ArrayList contents) throws IOException {
        writer.write(messageWriter.print0args("table"));
        writer.write(messageWriter.print0args("thread"));
        writer.write(messageWriter.print0args("trColored"));
        for (int i = 0; i < titles.length; i++) {
            writer.write(messageWriter.print1args("td", titles[i]));
        }
        writer.write(messageWriter.print0args("/tr"));
        writer.write(messageWriter.print0args("/thread"));
        writer.write(messageWriter.print0args("body"));
        for (int i = 0; i < contents.size(); i++) {
            writer.write(messageWriter.print0args("tr"));
            String property[] = (String[]) contents.get(i);
            for (int j = 0; j < property.length; j++) {
                writer.write(messageWriter.print1args("td", property[j]));
            }
            writer.write(messageWriter.print0args("/tr"));
        }
        writer.write(messageWriter.print0args("/body"));
        writer.write(messageWriter.print0args("/table"));
    }
