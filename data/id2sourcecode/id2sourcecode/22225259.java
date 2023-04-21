    public static void writeReadTag(ReadAceTag readTag, OutputStream out) throws IOException {
        Range range = readTag.asRange();
        writeString(String.format("RT{%n%s %s %s %d %d %s%n}%n", readTag.getId(), readTag.getType(), readTag.getCreator(), range.getBegin(), range.getEnd(), AceFileUtil.TAG_DATE_TIME_FORMATTER.print(readTag.getCreationDate().getTime())), out);
    }
