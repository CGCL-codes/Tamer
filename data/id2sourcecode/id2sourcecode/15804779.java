    private void parseHead(String head, String axis) {
        try {
            StringTokenizer st1 = new StringTokenizer(head, ":", true);
            String speciesAlias = st1.nextToken();
            st1.nextToken();
            String chromosome = st1.nextToken();
            if (chromosome.equals(":")) {
                chromosome = "";
            } else {
                st1.nextToken();
            }
            long start = Long.parseLong(st1.nextToken("-"));
            st1.nextToken();
            long end = Long.parseLong(st1.nextToken());
            if (axis.equals("X")) {
                xchr = chromosome;
                xpos = (start + end) / 2;
            } else {
                ychr = chromosome;
                ypos = (start + end) / 2;
            }
        } catch (NoSuchElementException e) {
            log.warn(e);
        } catch (NumberFormatException e) {
            log.warn(e);
        }
    }
