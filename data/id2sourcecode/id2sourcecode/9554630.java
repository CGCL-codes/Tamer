    void writeMacrosBond() throws IOException {
        out("#macro bond1(X1,Y1,Z1,X2,Y2,Z2,RADIUS,R,G,B)\n" + " cylinder{<X1,Y1,Z1>,<X2,Y2,Z2>,RADIUS\n" + "  pigment{rgb<R,G,B>}}\n" + " sphere{<X1,Y1,Z1>,RADIUS\n" + "  pigment{rgb<R,G,B>}}\n" + " sphere{<X2,Y2,Z2>,RADIUS\n" + "  pigment{rgb<R,G,B>}}\n" + "#end\n\n");
        out("#macro bond2(X1,Y1,Z1,X2,Y2,Z2,RADIUS,R1,G1,B1,R2,G2,B2)\n" + "#local xc = (X1 + X2) / 2;\n" + "#local yc = (Y1 + Y2) / 2;\n" + "#local zc = (Z1 + Z2) / 2;\n" + " cylinder{<X1,Y1,Z1>,<xc,yc,zc>,RADIUS\n" + "  pigment{rgb<R1,G1,B1>}}\n" + " cylinder{<xc,yc,zc>,<X2,Y2,Z2>,RADIUS\n" + "  pigment{rgb<R2,G2,B2>}}\n" + " sphere{<X1,Y1,Z1>,RADIUS\n" + "  pigment{rgb<R1,G1,B1>}}\n" + " sphere{<X2,Y2,Z2>,RADIUS\n" + "  pigment{rgb<R2,G2,B2>}}\n" + "#end\n\n");
    }
