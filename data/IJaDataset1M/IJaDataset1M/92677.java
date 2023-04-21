package utilities.utilities4testing.generator;

import java.util.Random;

/**
 * Random generator of strings.
 */
public class StringGenerator {

    static String generateString(Random random, int min, int max, int charMin, int charMax) {
        if (min > max) throw new IllegalArgumentException("max need to be bigger than min.");
        if (charMin > charMax) throw new IllegalArgumentException("charMax need to be bigger than charMin.");
        StringBuilder buffer = new StringBuilder();
        int tamanhoDaSenha = min + random.nextInt(max - min + 1);
        for (int i = 0; i < tamanhoDaSenha; i++) {
            int charAleatorio = charMin + random.nextInt(charMax - charMin + 1);
            buffer.append((char) charAleatorio);
        }
        return buffer.toString();
    }

    /**
	 * generate a random string with max >= length >= min with charMax >= chars >=
	 * charMin. eg: generateRandomString(3, 10, 'A', 'Z');
	 * 
	 * @param min
	 *            min of chars
	 * @param max
	 *            max of chars
	 * @param charMin
	 * @param charMax
	 * @return random string
	 */
    public static String generateRandomString(int min, int max, int charMin, int charMax) {
        return generateString(new Random(), min, max, charMin, charMax);
    }

    /**
	 * generate a random string with max >= length >= min with charMax >= chars >=
	 * charMin. eg: generateRandomString(3, 10, 'A', 'Z');
	 * @param seed an indice
	 * @param min
	 *            min of chars
	 * @param max
	 *            max of chars
	 * @param charMin
	 * @param charMax
	 * @return random string
	 */
    public static String generateString(long seed, int min, int max, int charMin, int charMax) {
        return generateString(new Random(seed), min, max, charMin, charMax);
    }

    /**
	 * random string of numbers
	 * 
	 * @param min
	 *            min length of the string
	 * @param max
	 *            max length of the string
	 * @return random string
	 */
    public static String generateRandomNumbersString(int min, int max) {
        return generateRandomString(min, max, '0', '9');
    }

    /**
	 * random string of numbers
	 * @param seed an indice
	 * @param min
	 *            min length of the string
	 * @param max
	 *            max length of the string
	 * @return random string
	 */
    public static String generateNumbersString(long seed, int min, int max) {
        return generateString(seed, min, max, '0', '9');
    }

    /**
	 * random string using 'a' to 'z' characters.
	 * 
	 * @param min
	 *            min length of the string
	 * @param max
	 *            max length of the string
	 * @return random string
	 */
    public static String generateRandomWordString(int min, int max) {
        return generateRandomString(min, max, 'a', 'z');
    }

    /**
	 * random string using 'a' to 'z' characters.
	 * @param seed an indice
	 * @param min
	 *            min length of the string
	 * @param max
	 *            max length of the string
	 * @return random string
	 */
    public static String generateWordString(long seed, int min, int max) {
        return generateString(seed, min, max, 'a', 'z');
    }
}
