    public void writeOn(PrintStream ps) {
        long avgPerRead = (nread == 0 ? 0 : bytesRead / nread);
        long avgPerWrite = (nwritten == 0 ? 0 : bytesWritten / nwritten);
        ps.println("class=" + classname + (stateless ? "(Stateless)" : "") + ", classId=" + classId + ", read(" + nread + "," + bytesRead + "," + avgPerRead + ")" + ", write(" + nwritten + "," + bytesWritten + "," + avgPerWrite + ")");
        Iterator itr = versionStatistics.values().iterator();
        while (itr.hasNext()) {
            VersionStatistics stats = (VersionStatistics) itr.next();
            stats.writeOn(ps);
        }
    }
