    public final void testReadWriteBitmap() {
        try {
            Bitmap bitmapPPM3 = (new PPMBitmapReaderWriter()).readBitmap(new FileInputStream("data/tests/final_ppm3.ppm"));
            Bitmap bitmapPPM6 = (new PPMBitmapReaderWriter()).readBitmap(new FileInputStream("data/tests/final_ppm6.ppm"));
            (new PPMBitmapReaderWriter()).writeBitmap(new FileOutputStream("data/tests/ppm3_2_ppm3.ppm"), bitmapPPM3, PPMBitmapReaderWriter.PPM3_FILEFORMAT);
            (new PPMBitmapReaderWriter()).writeBitmap(new FileOutputStream("data/tests/ppm3_2_ppm6.ppm"), bitmapPPM3, PPMBitmapReaderWriter.PPM6_FILEFORMAT);
            (new PPMBitmapReaderWriter()).writeBitmap(new FileOutputStream("data/tests/ppm6_2_ppm3.ppm"), bitmapPPM6, PPMBitmapReaderWriter.PPM3_FILEFORMAT);
            (new PPMBitmapReaderWriter()).writeBitmap(new FileOutputStream("data/tests/ppm6_2_ppm6.ppm"), bitmapPPM6, PPMBitmapReaderWriter.PPM6_FILEFORMAT);
            MessageDigest md_ppm3 = null;
            MessageDigest md_ppm6 = null;
            MessageDigest md_ppm3to3 = null;
            MessageDigest md_ppm3to6 = null;
            MessageDigest md_ppm6to3 = null;
            MessageDigest md_ppm6to6 = null;
            try {
                md_ppm3 = MessageDigest.getInstance("SHA");
                md_ppm6 = MessageDigest.getInstance("SHA");
                md_ppm3to3 = MessageDigest.getInstance("SHA");
                md_ppm3to6 = MessageDigest.getInstance("SHA");
                md_ppm6to3 = MessageDigest.getInstance("SHA");
                md_ppm6to6 = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            DigestInputStream dis_ppm3 = new DigestInputStream(new FileInputStream("data/tests/final_ppm3.ppm"), md_ppm3);
            DigestInputStream dis_ppm6 = new DigestInputStream(new FileInputStream("data/tests/final_ppm6.ppm"), md_ppm6);
            DigestInputStream dis_ppm3to3 = new DigestInputStream(new FileInputStream("data/tests/ppm3_2_ppm3.ppm"), md_ppm3to3);
            DigestInputStream dis_ppm3to6 = new DigestInputStream(new FileInputStream("data/tests/ppm3_2_ppm6.ppm"), md_ppm3to6);
            DigestInputStream dis_ppm6to3 = new DigestInputStream(new FileInputStream("data/tests/ppm6_2_ppm3.ppm"), md_ppm6to3);
            DigestInputStream dis_ppm6to6 = new DigestInputStream(new FileInputStream("data/tests/ppm6_2_ppm6.ppm"), md_ppm6to6);
            int i = 0;
            while ((i = dis_ppm3.read()) != -1) {
            }
            while ((i = dis_ppm6.read()) != -1) {
            }
            while ((i = dis_ppm3to3.read()) != -1) {
            }
            while ((i = dis_ppm3to6.read()) != -1) {
            }
            while ((i = dis_ppm6to3.read()) != -1) {
            }
            while ((i = dis_ppm6to6.read()) != -1) {
            }
            assertTrue("Conversion from P3 to P3", MessageDigest.isEqual(md_ppm3.digest(), md_ppm3to3.digest()));
            assertTrue("Conversion from P3 to P6", MessageDigest.isEqual(md_ppm6.digest(), md_ppm3to6.digest()));
            assertTrue("Conversion from P6 to P3", MessageDigest.isEqual(md_ppm3.digest(), md_ppm6to3.digest()));
            assertTrue("Conversion from P6 to P6", MessageDigest.isEqual(md_ppm6.digest(), md_ppm6to6.digest()));
        } catch (FileNotFoundException e) {
            System.err.print("File not found exception!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.print("IOException!");
            e.printStackTrace();
        }
    }
