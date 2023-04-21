    public void writeOn(PrintStream ps) {
        long avgPerRead = (nread == 0 ? 0 : bytesRead / nread);
        long avgPerWrite = (nwritten == 0 ? 0 : bytesWritten / nwritten);
        ps.println("   versionId=" + versionId + ", class=" + serializer.getClass().getName() + ", read(" + nread + "," + bytesRead + "," + avgPerRead + ")" + ", write(" + nwritten + "," + bytesWritten + "," + avgPerWrite + ")");
    }
