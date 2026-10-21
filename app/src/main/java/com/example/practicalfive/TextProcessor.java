package com.example.practicalfive;

/**
 * Validates and normalises the municipality name typed by the user.
 *
 * <p>This is a regular Java class: it holds no Android dependencies, no layout and it never
 * extends {@code Activity}. Keeping the rules here means both activities share one
 * implementation instead of duplicating the validation logic.</p>
 */
public final class TextProcessor {

    public static final String BEIJING = "Beijing";
    public static final String SHANGHAI = "Shanghai";
    public static final String CHONGQING = "Chongqing";
    public static final String TIANJIN = "Tianjin";

    /**
     * Supported municipalities in the same order as {@code R.array.municipalities_array}
     * (Beijing, Shanghai, Chongqing, Tianjin).
     */
    private static final String[] SUPPORTED = {BEIJING, SHANGHAI, CHONGQING, TIANJIN};

    private TextProcessor() {
        // Utility class: no instances needed.
    }

    /**
     * Validates a municipality name typed by the user.
     *
     * @param municipality raw text, possibly null, empty, padded with spaces or in any case
     * @return the correctly capitalised name for a supported municipality,
     *         or {@code null} when the input is null, blank or unsupported
     */
    public static String processMunicipality(String municipality) {
        if (municipality == null) {
            return null;
        }
        String candidate = municipality.trim();
        if (candidate.isEmpty()) {
            return null;
        }
        for (String supported : SUPPORTED) {
            if (supported.equalsIgnoreCase(candidate)) {
                return supported;
            }
        }
        return null;
    }

    /**
     * Maps a municipality name onto its position in {@code municipalities_array}.
     *
     * @param municipality name in any case, or the already normalised name
     * @return 0 for Beijing, 1 for Shanghai, 2 for Chongqing, 3 for Tianjin, otherwise -1
     */
    public static int indexOfMunicipality(String municipality) {
        String normalised = processMunicipality(municipality);
        if (normalised == null) {
            return -1;
        }
        for (int i = 0; i < SUPPORTED.length; i++) {
            if (SUPPORTED[i].equals(normalised)) {
                return i;
            }
        }
        return -1;
    }
}
