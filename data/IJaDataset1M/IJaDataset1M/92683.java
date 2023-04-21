package it.infodea.tapestrydea.exceptions;

public class PageAccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PageAccessDeniedException() {
        super();
    }

    public PageAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageAccessDeniedException(String message) {
        super(message);
    }

    public PageAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
