    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Property indexProperty = Property.getProperty("TextIndexPath");
            LuceneIndex index = new LuceneIndex(indexProperty.getValue());
            index.optimise();
            response.getWriter().write("Index Optimised Successfully");
        } catch (IOException e) {
            throw new ServletException("Unable to read or write the index", e);
        } catch (PersistentModelException e) {
            throw new ServletException("Unable to find the directory to put the index into", e);
        }
    }
