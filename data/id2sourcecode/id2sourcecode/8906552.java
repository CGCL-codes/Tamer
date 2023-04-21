    public void writeFooter(ArrayList ffd) {
        try {
            fout.write("  </table>\n".getBytes());
            fout.write(" </map>\n".getBytes());
            fout.write("</spreadsheet>\n".getBytes());
            fout.flush();
            fout.close();
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }
