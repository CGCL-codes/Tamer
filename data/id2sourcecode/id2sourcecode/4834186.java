    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Document document = new Document();
        try {
            OutputStream os = new FileOutputStream(RESULT);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            writer.setPageEvent(new Movies05().new Ellipse());
            document.open();
            Session session = (Session) MySessionFactory.currentSession();
            Query q = session.createQuery("from FilmTitle order by title");
            java.util.List<FilmTitle> results = q.list();
            Paragraph p;
            Chunk c;
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
            Font white = new Font(Font.HELVETICA, 12, Font.BOLD | Font.ITALIC, Color.WHITE);
            for (FilmTitle movie : results) {
                p = new Paragraph(20);
                c = new Chunk(movie.getTitle(), bold);
                c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
                p.add(c);
                c = new Chunk(" (" + movie.getYear() + ") ", italic);
                p.add(c);
                c = new Chunk("IMDB", white);
                c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
                c.setGenericTag("ellipse");
                p.add(c);
                document.add(p);
                Set<DirectorName> directors = movie.getDirectorNames();
                List list = new List();
                for (DirectorName director : directors) {
                    list.add(director.getName());
                }
                document.add(list);
            }
            document.close();
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        } catch (DocumentException e) {
            LOGGER.error("DocumentException: ", e);
        }
    }
