package org.wowyomad.survey.util;

import java.util.Random;

public class CodeGenerator {
    private static final long min = 1000;
    private static final long max = 9999;

    public static String generateCode() {
        return String.valueOf(new Random().nextLong(min, max));
    }
}
