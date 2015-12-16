package com.bargetor.service.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class IOUtil {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	
	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}
	
	/**
	 *<p>Title: toString</p>
	 *<p>Description:输入转换成字符串</p>
	 * @param in
	 * @param encoding
	 * @return
	 * @return String 返回类型
	*/
	public static String toString(InputStream input, String encoding)throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}
	
	/**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the <code>copyLarge(InputStream, OutputStream)</code> method.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @throws ArithmeticException if the byte count is too large
     * @since Commons IO 1.1
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copy bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    /**
     * Copy bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(InputStream input, Writer output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encoding);
            copy(in, output);
        }
    }

    // copy from Reader
    //-----------------------------------------------------------------------
    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the <code>copyLarge(Reader, Writer)</code> method.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @throws ArithmeticException if the character count is too large
     * @since Commons IO 1.1
     */
    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copy chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copy chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform, and calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(Reader input, OutputStream output)
            throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out);
        // XXX Unless anyone is planning on rewriting OutputStreamWriter, we
        // have to flush here.
        out.flush();
    }

    /**
     * Copy chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(Reader input, OutputStream output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            copy(input, out);
            // XXX Unless anyone is planning on rewriting OutputStreamWriter,
            // we have to flush here.
            out.flush();
        }
    }
}
