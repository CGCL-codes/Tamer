package cx.ath.contribs.internal.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import cx.ath.contribs.internal.xerces.impl.msg.XMLMessageFormatter;
import cx.ath.contribs.internal.xerces.util.MessageFormatter;

/**
 * A simple ASCII byte reader. This is an optimized reader for reading
 * byte streams that only contain 7-bit ASCII characters.
 * 
 * @xerces.internal
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: ASCIIReader.java,v 1.2 2007/07/13 07:23:29 paul Exp $
 */
public class ASCIIReader extends Reader {

    /** Default byte buffer size (2048). */
    public static final int DEFAULT_BUFFER_SIZE = 2048;

    /** Input stream. */
    protected final InputStream fInputStream;

    /** Byte buffer. */
    protected final byte[] fBuffer;

    private final MessageFormatter fFormatter;

    private final Locale fLocale;

    /** 
     * Constructs an ASCII reader from the specified input stream 
     * using the default buffer size.
     *
     * @param inputStream The input stream.
     * @param messageFormatter  the MessageFormatter to use to message reporting.
     * @param locale    the Locale for which messages are to be reported
     */
    public ASCIIReader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, DEFAULT_BUFFER_SIZE, messageFormatter, locale);
    }

    /** 
     * Constructs an ASCII reader from the specified input stream 
     * and buffer size.
     *
     * @param inputStream The input stream.
     * @param size        The initial buffer size.
     * @param messageFormatter  the MessageFormatter to use to message reporting.
     * @param locale    the Locale for which messages are to be reported
     */
    public ASCIIReader(InputStream inputStream, int size, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, new byte[size], messageFormatter, locale);
    }

    /** 
     * Constructs an ASCII reader from the specified input stream and buffer.
     *
     * @param inputStream The input stream.
     * @param buffer      The byte buffer.
     * @param messageFormatter  the MessageFormatter to use to message reporting.
     * @param locale    the Locale for which messages are to be reported
     */
    public ASCIIReader(InputStream inputStream, byte[] buffer, MessageFormatter messageFormatter, Locale locale) {
        fInputStream = inputStream;
        fBuffer = buffer;
        fFormatter = messageFormatter;
        fLocale = locale;
    }

    /**
     * Read a single character.  This method will block until a character is
     * available, an I/O error occurs, or the end of the stream is reached.
     *
     * <p> Subclasses that intend to support efficient single-character input
     * should override this method.
     *
     * @return     The character read, as an integer in the range 0 to 127
     *             (<tt>0x00-0x7f</tt>), or -1 if the end of the stream has
     *             been reached
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read() throws IOException {
        int b0 = fInputStream.read();
        if (b0 >= 0x80) {
            throw new MalformedByteSequenceException(fFormatter, fLocale, XMLMessageFormatter.XML_DOMAIN, "InvalidASCII", new Object[] { Integer.toString(b0) });
        }
        return b0;
    }

    /**
     * Read characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param      ch     Destination buffer
     * @param      offset Offset at which to start storing characters
     * @param      length Maximum number of characters to read
     *
     * @return     The number of characters read, or -1 if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read(char ch[], int offset, int length) throws IOException {
        if (length > fBuffer.length) {
            length = fBuffer.length;
        }
        int count = fInputStream.read(fBuffer, 0, length);
        for (int i = 0; i < count; i++) {
            int b0 = fBuffer[i];
            if (b0 < 0) {
                throw new MalformedByteSequenceException(fFormatter, fLocale, XMLMessageFormatter.XML_DOMAIN, "InvalidASCII", new Object[] { Integer.toString(b0 & 0x0FF) });
            }
            ch[offset + i] = (char) b0;
        }
        return count;
    }

    /**
     * Skip characters.  This method will block until some characters are
     * available, an I/O error occurs, or the end of the stream is reached.
     *
     * @param  n  The number of characters to skip
     *
     * @return    The number of characters actually skipped
     *
     * @exception  IOException  If an I/O error occurs
     */
    public long skip(long n) throws IOException {
        return fInputStream.skip(n);
    }

    /**
     * Tell whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input,
     * false otherwise.  Note that returning false does not guarantee that the
     * next read will block.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public boolean ready() throws IOException {
        return false;
    }

    /**
     * Tell whether this stream supports the mark() operation.
     */
    public boolean markSupported() {
        return fInputStream.markSupported();
    }

    /**
     * Mark the present position in the stream.  Subsequent calls to reset()
     * will attempt to reposition the stream to this point.  Not all
     * character-input streams support the mark() operation.
     *
     * @param  readAheadLimit  Limit on the number of characters that may be
     *                         read while still preserving the mark.  After
     *                         reading this many characters, attempting to
     *                         reset the stream may fail.
     *
     * @exception  IOException  If the stream does not support mark(),
     *                          or if some other I/O error occurs
     */
    public void mark(int readAheadLimit) throws IOException {
        fInputStream.mark(readAheadLimit);
    }

    /**
     * Reset the stream.  If the stream has been marked, then attempt to
     * reposition it at the mark.  If the stream has not been marked, then
     * attempt to reset it in some way appropriate to the particular stream,
     * for example by repositioning it to its starting point.  Not all
     * character-input streams support the reset() operation, and some support
     * reset() without supporting mark().
     *
     * @exception  IOException  If the stream has not been marked,
     *                          or if the mark has been invalidated,
     *                          or if the stream does not support reset(),
     *                          or if some other I/O error occurs
     */
    public void reset() throws IOException {
        fInputStream.reset();
    }

    /**
     * Close the stream.  Once a stream has been closed, further read(),
     * ready(), mark(), or reset() invocations will throw an IOException.
     * Closing a previously-closed stream, however, has no effect.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void close() throws IOException {
        fInputStream.close();
    }
}
