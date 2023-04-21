	public void apptrans(String outFilename, String tarfiletype)
			throws CharacterCodingException, IOException {
		
		RandomAccessFile inraf=null;
		RandomAccessFile outraf=null;
		
		try {
			File outfile = new File(outFilename);

			inraf = new RandomAccessFile(this, "r");
			outraf = new RandomAccessFile(outfile, "rw");

			FileChannel finc = inraf.getChannel();
			FileChannel foutc = outraf.getChannel();

			MappedByteBuffer inmbb = finc.map(FileChannel.MapMode.READ_ONLY, 0,
					(int) this.length());

			Charset inCharset = Charset.forName("Shift-JIS");
			Charset outCharset = Charset.forName("UTF-8");
			// ISO-8859-1抛出UnmappableCharacterException,原因是Shift-JIS字符不能全部映射为ISO-8859-1字符，但可以全部映射为UTF字符

			/*
			 * public class UnmappableCharacterException extends
			 * CharacterCodingException Checked exception thrown when an input
			 * character (or byte) sequence is valid but cannot be mapped to an
			 * output byte (or character) sequence.
			 * 
			 * ——so It is not mapped to GBK!!
			 */

			CharsetDecoder inDecoder = inCharset.newDecoder();
			CharsetEncoder outEncoder = outCharset.newEncoder();

			CharBuffer cb = inDecoder.decode(inmbb);

			String sb = cb.toString();

			try {
				Pattern p = Pattern.compile("\\." + oriFileType
						+ ".*(WAVE|wave)");
				Matcher a = p.matcher(sb);
				sb = a.replaceFirst("\\." + tarfiletype + "\"" + " WAVE");

			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// cb.clear();不能使用，clear()并没有实际清除数据。新建一个CharBuffer
			CharBuffer cb2 = CharBuffer.wrap(sb);
			ByteBuffer outbb = outEncoder.encode(cb2);

			foutc.write(outbb);

			inraf.close();
			outraf.close();
		} catch (CharacterCodingException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		}
	}
