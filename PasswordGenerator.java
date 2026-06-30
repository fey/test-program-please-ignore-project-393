/**
 * Генератор паролей с проверкой надёжности.
 */
public final class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS    = "0123456789";
    private static final String SPECIAL   = "!@#$%^&*";

    private PasswordGenerator() { }

    /**
     * Возвращает следующее псевдослучайное число (LCG).
     *
     * @param seed входное число
     * @return следующее число цепочки
     */
    public static long nextRandom(long seed) {
        return (16807L * seed) % 2147483647L;
    }

    /**
     * Генерирует пароль заданной длины по числу-ключу с явными флагами наборов символов.
     *
     * @param length       длина пароля
     * @param seed         число-ключ (определяет конкретный пароль)
     * @param useUppercase включить ПРОПИСНЫЕ буквы
     * @param useDigits    включить цифры
     * @param useSpecial   включить спецсимволы
     * @return сгенерированный пароль
     */
    public static String generatePassword(
            int length,
            long seed,
            boolean useUppercase,
            boolean useDigits,
            boolean useSpecial
    ) {
        String alphabet = LOWERCASE;
        if (useUppercase) {
            alphabet += UPPERCASE;
        }
        if (useDigits) {
            alphabet += DIGITS;
        }
        if (useSpecial) {
            alphabet += SPECIAL;
        }

        long current = seed;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            current = nextRandom(current);
            result.append(alphabet.charAt((int) (current % alphabet.length())));
        }
        return result.toString();
    }

    private static boolean hasLowercase(String password) {
        for (char c : password.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                return true;
            }
        }
        return false;
    }

    private static boolean hasUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDigit(String password) {
        for (char c : password.toCharArray()) {
            if (c >= '0' && c <= '9') {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpecial(String password) {
        for (char c : password.toCharArray()) {
            if (SPECIAL.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLongEnough(String password) {
        return password.length() >= 8;
    }

    private static int strengthScore(String password) {
        int score = 0;
        if (isLongEnough(password)) {
            score++;
        }
        if (hasLowercase(password)) {
            score++;
        }
        if (hasUppercase(password)) {
            score++;
        }
        if (hasDigit(password)) {
            score++;
        }
        if (hasSpecial(password)) {
            score++;
        }
        return score;
    }

    /**
     * Оценивает надёжность пароля.
     *
     * @param password проверяемый пароль
     * @return строка с вердиктом и оценкой
     */
    public static String checkPassword(String password) {
        int score = strengthScore(password);
        String verdict;
        if (score <= 2) {
            verdict = "Слабый";
        } else if (score == 3) {
            verdict = "Средний";
        } else if (score == 4) {
            verdict = "Надёжный";
        } else {
            verdict = "Очень надёжный";
        }
        return verdict + " пароль (оценка " + score + " из 5)";
    }
}
