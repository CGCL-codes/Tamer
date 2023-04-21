    public float[] toRGBAFloatArray(float[] values) {
        String cos = channelOrder.name().toUpperCase();
        switch(channelOrder) {
            case ARGB:
            case ARBG:
            case AGRB:
            case AGBR:
            case ABRG:
            case ABGR:
            case RAGB:
            case RABG:
            case RGAB:
            case RGBA:
            case RBAG:
            case RBGA:
            case GARB:
            case GABR:
            case GRAB:
            case GRBA:
            case GBAR:
            case GBRA:
            case BARG:
            case BAGR:
            case BRAG:
            case BRGA:
            case BGAR:
            case BGRA:
                return new float[] { values[cos.indexOf('R')], values[cos.indexOf('G')], values[cos.indexOf('B')], values[cos.indexOf('A')] };
            case RGB:
            case RBG:
            case GRB:
            case GBR:
            case BRG:
            case BGR:
                return new float[] { values[cos.indexOf('R')], values[cos.indexOf('G')], values[cos.indexOf('B')], 1.0f };
            case AY:
            case YA:
                return new float[] { values[cos.indexOf('Y')], values[cos.indexOf('Y')], values[cos.indexOf('Y')], values[cos.indexOf('A')] };
            case Y:
                return new float[] { values[cos.indexOf('Y')], values[cos.indexOf('Y')], values[cos.indexOf('Y')], 1.0f };
            case A:
                return new float[] { 0.0f, 0.0f, 0.0f, values[cos.indexOf('A')] };
            case AHSV:
            case AHVS:
            case ASHV:
            case ASVH:
            case AVHS:
            case AVSH:
            case HASV:
            case HAVS:
            case HSAV:
            case HSVA:
            case HVAS:
            case HVSA:
            case SAHV:
            case SAVH:
            case SHAV:
            case SHVA:
            case SVAH:
            case SVHA:
            case VAHS:
            case VASH:
            case VHAS:
            case VHSA:
            case VSAH:
            case VSHA:
                int rgb1 = Color.HSBtoRGB(values[cos.indexOf('H')], values[cos.indexOf('S')], values[cos.indexOf('V')]);
                return new float[] { ((rgb1 >> 16) & 0xFF) / 255.0f, ((rgb1 >> 8) & 0xFF) / 255.0f, (rgb1 & 0xFF) / 255.0f, values[cos.indexOf('A')] };
            case HSV:
            case HVS:
            case SHV:
            case SVH:
            case VHS:
            case VSH:
                int rgb2 = Color.HSBtoRGB(values[cos.indexOf('H')], values[cos.indexOf('S')], values[cos.indexOf('V')]);
                return new float[] { ((rgb2 >> 16) & 0xFF) / 255.0f, ((rgb2 >> 8) & 0xFF) / 255.0f, (rgb2 & 0xFF) / 255.0f, 1.0f };
            case AHSL:
            case AHLS:
            case ASHL:
            case ASLH:
            case ALHS:
            case ALSH:
            case HASL:
            case HALS:
            case HSAL:
            case HSLA:
            case HLAS:
            case HLSA:
            case SAHL:
            case SALH:
            case SHAL:
            case SHLA:
            case SLAH:
            case SLHA:
            case LAHS:
            case LASH:
            case LHAS:
            case LHSA:
            case LSAH:
            case LSHA:
                float hh1 = values[cos.indexOf('H')];
                float ss1 = values[cos.indexOf('S')];
                float ll1 = values[cos.indexOf('L')];
                float h1 = hh1;
                ll1 *= 2;
                ss1 *= (ll1 <= 1) ? ll1 : 2 - ll1;
                float v1 = (ll1 + ss1) / 2;
                float s1 = ((ll1 + ss1) == 0) ? 0 : ((2 * ss1) / (ll1 + ss1));
                int rgb3 = Color.HSBtoRGB(h1, s1, v1);
                return new float[] { ((rgb3 >> 16) & 0xFF) / 255.0f, ((rgb3 >> 8) & 0xFF) / 255.0f, (rgb3 & 0xFF) / 255.0f, values[cos.indexOf('A')] };
            case HSL:
            case HLS:
            case SHL:
            case SLH:
            case LHS:
            case LSH:
                float hh2 = values[cos.indexOf('H')];
                float ss2 = values[cos.indexOf('S')];
                float ll2 = values[cos.indexOf('L')];
                float h2 = hh2;
                ll2 *= 2;
                ss2 *= (ll2 <= 1) ? ll2 : 2 - ll2;
                float v2 = (ll2 + ss2) / 2;
                float s2 = ((ll2 + ss2) == 0) ? 0 : ((2 * ss2) / (ll2 + ss2));
                int rgb4 = Color.HSBtoRGB(h2, s2, v2);
                return new float[] { ((rgb4 >> 16) & 0xFF) / 255.0f, ((rgb4 >> 8) & 0xFF) / 255.0f, (rgb4 & 0xFF) / 255.0f, 1.0f };
            case CMYK:
            case CMKY:
            case CYMK:
            case CYKM:
            case CKMY:
            case CKYM:
            case MCYK:
            case MCKY:
            case MYCK:
            case MYKC:
            case MKCY:
            case MKYC:
            case YCMK:
            case YCKM:
            case YMCK:
            case YMKC:
            case YKCM:
            case YKMC:
            case KCMY:
            case KCYM:
            case KMCY:
            case KMYC:
            case KYCM:
            case KYMC:
                float k1 = values[cos.indexOf('K')];
                float c1 = k1 + (values[cos.indexOf('C')] * (1.0f - k1));
                float m1 = k1 + (values[cos.indexOf('M')] * (1.0f - k1));
                float y1 = k1 + (values[cos.indexOf('Y')] * (1.0f - k1));
                return new float[] { 1.0f - c1, 1.0f - m1, 1.0f - y1, 1.0f };
            case CMY:
            case CYM:
            case MCY:
            case MYC:
            case YCM:
            case YMC:
                return new float[] { 1.0f - values[cos.indexOf('C')], 1.0f - values[cos.indexOf('M')], 1.0f - values[cos.indexOf('Y')], 1.0f };
            case YIQ:
            case YQI:
            case IYQ:
            case IQY:
            case QYI:
            case QIY:
                double[] rgb5 = YIQtoRGB(values[cos.indexOf('Y')], values[cos.indexOf('I')], values[cos.indexOf('Q')]);
                return new float[] { (float) rgb5[0], (float) rgb5[1], (float) rgb5[2], 1.0f };
            case YUV:
            case YVU:
            case UYV:
            case UVY:
            case VYU:
            case VUY:
                double[] rgb6 = YUVtoRGB(values[cos.indexOf('Y')], values[cos.indexOf('U')], values[cos.indexOf('V')]);
                return new float[] { (float) rgb6[0], (float) rgb6[1], (float) rgb6[2], 1.0f };
            case XYZ:
            case XZY:
            case YXZ:
            case YZX:
            case ZXY:
            case ZYX:
                double[] rgb8 = XYZtosRGB(values[cos.indexOf('X')], values[cos.indexOf('Y')], values[cos.indexOf('Z')]);
                return new float[] { (float) rgb8[0], (float) rgb8[1], (float) rgb8[2], 1.0f };
            default:
                throw new IllegalArgumentException("Invalid channel order");
        }
    }
