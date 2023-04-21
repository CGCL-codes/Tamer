        private void comment(Iterator<String> lines, StringWriter writer, String line) throws Exception {
            Pattern normalComment = Pattern.compile("^(\\s*)\\*.*$");
            Pattern parameterComment = Pattern.compile("^(\\s*)\\*.*@.*$");
            boolean endSummary = false;
            writer.append(line.replaceAll("\\/\\*\\*", "/// <summary>"));
            writer.append("\n");
            while (lines.hasNext()) {
                String nextLine = lines.next();
                nextLine = nextLine.substring(1);
                if (nextLine.matches("^\\s*\\*\\/")) {
                    if (!endSummary) {
                        writer.append(nextLine.replaceAll("\\*\\/", "/// </summary>"));
                    } else {
                        writer.append(nextLine.replaceAll("\\*\\/", "///"));
                    }
                    writer.append("\n");
                    return;
                }
                if (!endSummary) {
                    Matcher parameterMatch = parameterComment.matcher(nextLine);
                    if (parameterMatch.matches()) {
                        writer.append(parameterMatch.group(1));
                        writer.append("/// </summary>");
                        writer.append("\n");
                        writer.append(parameterMatch.group(1));
                        writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                        writer.append("\n");
                        endSummary = true;
                    } else {
                        Matcher normalMatch = normalComment.matcher(nextLine);
                        if (normalMatch.matches()) {
                            writer.append(normalMatch.group(1));
                            writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                            writer.append("\n");
                        }
                    }
                } else {
                    Matcher normalMatch = normalComment.matcher(nextLine);
                    if (normalMatch.matches()) {
                        writer.append(normalMatch.group(1));
                        writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                        writer.append("\n");
                    } else {
                        throw new IllegalStateException("Comment does not end well " + nextLine);
                    }
                }
            }
        }
