        private void license(SourceDetails details, Iterator<String> lines, StringWriter writer, String line) throws Exception {
            Pattern comment = Pattern.compile("^(\\s*)\\*.*$");
            writer.append(line.replaceAll("\\/\\*", "//"));
            writer.append("\n");
            while (lines.hasNext()) {
                String nextLine = lines.next();
                nextLine = nextLine.substring(1);
                if (nextLine.matches("^\\s*\\*\\/")) {
                    writer.append(nextLine.replaceAll("\\*\\/", "//"));
                    writer.append("\n");
                    return;
                }
                Matcher matcher = comment.matcher(nextLine);
                if (matcher.matches()) {
                    if (nextLine.matches(".*\\.java.*")) {
                        nextLine = nextLine.replaceAll("\\.java", ".cs");
                    }
                    writer.append(matcher.group(1));
                    writer.append(nextLine.replaceAll("^\\s*\\*", "//"));
                    writer.append("\n");
                } else {
                    throw new IllegalStateException("Comment does not end well '" + nextLine + "' in file " + details.getSource().getCanonicalPath());
                }
            }
        }
