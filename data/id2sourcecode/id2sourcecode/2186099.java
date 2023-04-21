    @Override
    public void printHTML(PrintWriter out, PrintConfig config) {
        super.printHTML(out, config);
        out.format("<div><b>Lock</b>: %s</div>", isLocked() ? "Locked" : "Not locked");
        out.format("<div>%d writer(s) and %d reader(s)</div>", getReaders(), getWriters());
        if (getState() == ResourceState.DONE || getState() == ResourceState.ERROR || getState() == ResourceState.RUNNING) {
            long start = getStartTimestamp();
            long end = getState() == ResourceState.RUNNING ? System.currentTimeMillis() : getEndTimestamp();
            out.format("<div>Started: %s</div>", longDateFormat.format(new Date(start)));
            if (getState() != ResourceState.RUNNING) out.format("<div>Ended: %s</div>", longDateFormat.format(new Date(end)));
            out.format("<div>Duration: %s</div>", Time.formatTimeInMilliseconds(end - start));
        }
        TreeMap<String, Dependency> dependencies = getDependencies();
        if (!dependencies.isEmpty()) {
            out.format("<h2>Dependencies</h2><ul>");
            out.format("<div>%d unsatisfied dependencie(s)</div>", nbUnsatisfied);
            for (Entry<String, Dependency> entry : dependencies.entrySet()) {
                String dependency = entry.getKey();
                Dependency status = entry.getValue();
                Resource resource = null;
                try {
                    resource = scheduler.getResource(entry.getKey());
                } catch (DatabaseException e) {
                }
                out.format("<li><a href=\"%s/resource?id=%s\">%s</a>: %s [%b]</li>", config.detailURL, XPMServlet.urlEncode(dependency), dependency, status.getType(), resource == null ? false : resource.accept(status.type).isOK());
            }
            out.println("</ul>");
        }
    }
