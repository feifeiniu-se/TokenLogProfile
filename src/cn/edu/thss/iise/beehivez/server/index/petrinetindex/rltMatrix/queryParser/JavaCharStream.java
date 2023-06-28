/* Generated By:JavaCC: Do not edit this line. JavaCharStream.java Version 5.0 */
/* JavaCCOptions:STATIC=false,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package cn.edu.thss.iise.beehivez.server.index.petrinetindex.rltMatrix.queryParser;

/**
 * An implementation of interface CharStream, where the stream is assumed to
 * contain only ASCII characters (with java-like unicode escape staticgenerator).
 */

public class JavaCharStream {
	/** Whether parser is static. */
	public static final boolean staticFlag = false;

	static final int hexval(char c) throws java.io.IOException {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;

		case 'a':
		case 'A':
			return 10;
		case 'b':
		case 'B':
			return 11;
		case 'c':
		case 'C':
			return 12;
		case 'd':
		case 'D':
			return 13;
		case 'e':
		case 'E':
			return 14;
		case 'f':
		case 'F':
			return 15;
		}

		throw new java.io.IOException(); // Should never come here
	}

	/** Position in buffer. */
	public int bufpos = -1;
	int bufsize;
	int available;
	int tokenBegin;

	protected java.io.Reader inputStream;

	protected char[] nextCharBuf;
	protected char[] buffer;
	protected int maxNextCharInd = 0;
	protected int nextCharInd = -1;
	protected int inBuf = 0;
	protected int tabSize = 8;

	protected void setTabSize(int i) {
		tabSize = i;
	}

	protected int getTabSize(int i) {
		return tabSize;
	}

	protected void ExpandBuff(boolean wrapAround) {
		char[] newbuffer = new char[bufsize + 2048];

		try {
			if (wrapAround) {
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize
						- tokenBegin);
				System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin,
						bufpos);
				buffer = newbuffer;

				bufpos += (bufsize - tokenBegin);
			} else {
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize
						- tokenBegin);
				buffer = newbuffer;

				bufpos -= tokenBegin;
			}
		} catch (Throwable t) {
			throw new Error(t.getMessage());
		}

		available = (bufsize += 2048);
		tokenBegin = 0;
	}

	protected void FillBuff() throws java.io.IOException {
		int i;
		if (maxNextCharInd == 4096)
			maxNextCharInd = nextCharInd = 0;

		try {
			if ((i = inputStream.read(nextCharBuf, maxNextCharInd,
					4096 - maxNextCharInd)) == -1) {
				inputStream.close();
				throw new java.io.IOException();
			} else
				maxNextCharInd += i;
			return;
		} catch (java.io.IOException e) {
			if (bufpos != 0) {
				--bufpos;
				backup(0);
			}
			throw e;
		}
	}

	protected char ReadByte() throws java.io.IOException {
		if (++nextCharInd >= maxNextCharInd)
			FillBuff();

		return nextCharBuf[nextCharInd];
	}

	/** @return starting character for token. */
	public char BeginToken() throws java.io.IOException {
		if (inBuf > 0) {
			--inBuf;

			if (++bufpos == bufsize)
				bufpos = 0;

			tokenBegin = bufpos;
			return buffer[bufpos];
		}

		tokenBegin = 0;
		bufpos = -1;

		return readChar();
	}

	protected void AdjustBuffSize() {
		if (available == bufsize) {
			if (tokenBegin > 2048) {
				bufpos = 0;
				available = tokenBegin;
			} else
				ExpandBuff(false);
		} else if (available > tokenBegin)
			available = bufsize;
		else if ((tokenBegin - available) < 2048)
			ExpandBuff(true);
		else
			available = tokenBegin;
	}

	/** Read a character. */
	public char readChar() throws java.io.IOException {
		if (inBuf > 0) {
			--inBuf;

			if (++bufpos == bufsize)
				bufpos = 0;

			return buffer[bufpos];
		}

		char c;

		if (++bufpos == available)
			AdjustBuffSize();

		if ((buffer[bufpos] = c = ReadByte()) == '\\') {

			int backSlashCnt = 1;

			for (;;) // Read all the backslashes
			{
				if (++bufpos == available)
					AdjustBuffSize();

				try {
					if ((buffer[bufpos] = c = ReadByte()) != '\\') {
						// UpdateLineColumn(c);
						// found a non-backslash char.
						if ((c == 'u') && ((backSlashCnt & 1) == 1)) {
							if (--bufpos < 0)
								bufpos = bufsize - 1;

							break;
						}

						backup(backSlashCnt);
						return '\\';
					}
				} catch (java.io.IOException e) {
					// We are returning one backslash so we should only backup
					// (count-1)
					if (backSlashCnt > 1)
						backup(backSlashCnt - 1);

					return '\\';
				}

				backSlashCnt++;
			}

			// Here, we have seen an odd number of backslash's followed by a 'u'
			try {
				while ((c = ReadByte()) == 'u')
					;

				buffer[bufpos] = c = (char) (hexval(c) << 12
						| hexval(ReadByte()) << 8 | hexval(ReadByte()) << 4 | hexval(ReadByte()));

			} catch (java.io.IOException e) {
				throw new Error("Invalid escape character in input");
			}

			if (backSlashCnt == 1)
				return c;
			else {
				backup(backSlashCnt - 1);
				return '\\';
			}
		} else {
			return c;
		}
	}

	@Deprecated
	/*
	 * *
	 * 
	 * @deprecated
	 * 
	 * @see #getEndColumn
	 */
	public int getColumn() {
		return -1;
	}

	@Deprecated
	/*
	 * *
	 * 
	 * @deprecated
	 * 
	 * @see #getEndLine
	 */
	public int getLine() {
		return -1;
	}

	/** Get end column. */
	public int getEndColumn() {
		return -1;
	}

	/** Get end line. */
	public int getEndLine() {
		return -1;
	}

	/** @return column of token start */
	public int getBeginColumn() {
		return -1;
	}

	/** @return line number of token start */
	public int getBeginLine() {
		return -1;
	}

	/** Retreat. */
	public void backup(int amount) {

		inBuf += amount;
		if ((bufpos -= amount) < 0)
			bufpos += bufsize;
	}

	/** Constructor. */
	public JavaCharStream(java.io.Reader dstream, int startline,
			int startcolumn, int buffersize) {
		inputStream = dstream;

		available = bufsize = buffersize;
		buffer = new char[buffersize];
		nextCharBuf = new char[4096];
	}

	/** Constructor. */
	public JavaCharStream(java.io.Reader dstream, int startline, int startcolumn) {
		this(dstream, startline, startcolumn, 4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.Reader dstream) {
		this(dstream, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream, int startline, int startcolumn,
			int buffersize) {
		inputStream = dstream;

		if (buffer == null || buffersize != buffer.length) {
			available = bufsize = buffersize;
			buffer = new char[buffersize];
			nextCharBuf = new char[4096];
		}
		tokenBegin = inBuf = maxNextCharInd = 0;
		nextCharInd = bufpos = -1;
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream, int startline, int startcolumn) {
		ReInit(dstream, startline, startcolumn, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream) {
		ReInit(dstream, 1, 1, 4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn, int buffersize)
			throws java.io.UnsupportedEncodingException {
		this(encoding == null ? new java.io.InputStreamReader(dstream)
				: new java.io.InputStreamReader(dstream, encoding), startline,
				startcolumn, buffersize);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream, int startline,
			int startcolumn, int buffersize) {
		this(new java.io.InputStreamReader(dstream), startline, startcolumn,
				4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn)
			throws java.io.UnsupportedEncodingException {
		this(dstream, encoding, startline, startcolumn, 4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream, int startline,
			int startcolumn) {
		this(dstream, startline, startcolumn, 4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream, String encoding)
			throws java.io.UnsupportedEncodingException {
		this(dstream, encoding, 1, 1, 4096);
	}

	/** Constructor. */
	public JavaCharStream(java.io.InputStream dstream) {
		this(dstream, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn, int buffersize)
			throws java.io.UnsupportedEncodingException {
		ReInit(encoding == null ? new java.io.InputStreamReader(dstream)
				: new java.io.InputStreamReader(dstream, encoding), startline,
				startcolumn, buffersize);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, int startline,
			int startcolumn, int buffersize) {
		ReInit(new java.io.InputStreamReader(dstream), startline, startcolumn,
				buffersize);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn)
			throws java.io.UnsupportedEncodingException {
		ReInit(dstream, encoding, startline, startcolumn, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, int startline,
			int startcolumn) {
		ReInit(dstream, startline, startcolumn, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding)
			throws java.io.UnsupportedEncodingException {
		ReInit(dstream, encoding, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream) {
		ReInit(dstream, 1, 1, 4096);
	}

	/** @return token image as String */
	public String GetImage() {
		if (bufpos >= tokenBegin)
			return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
		else
			return new String(buffer, tokenBegin, bufsize - tokenBegin)
					+ new String(buffer, 0, bufpos + 1);
	}

	/** @return suffix */
	public char[] GetSuffix(int len) {
		char[] ret = new char[len];

		if ((bufpos + 1) >= len)
			System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
		else {
			System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0, len
					- bufpos - 1);
			System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
		}

		return ret;
	}

	/** Set buffers back to null when finished. */
	public void Done() {
		nextCharBuf = null;
		buffer = null;
	}
}
/*
 * JavaCC - OriginalChecksum=b06e9eb4707c4f4983346faeb25c70f9 (do not edit this
 * line)
 */
