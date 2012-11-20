package hjelpeklasser;

public class InputFilter {

    private static final char erstattMed = '_';
    private static final String spesialtegn = "- ,.";

    public static String filtrer(String tekst) {
        if (tekst == null) {
            return null;
        }
        StringBuffer resultat = new StringBuffer(tekst);
        for (int i = 0; i < tekst.length(); i++) {
            char tegn = tekst.charAt(i);
            /* isLetterOrDigit() bruker tegnsettet som maskinen er satt opp med */
            if (!(Character.isLetterOrDigit(tegn) || spesialtegn.indexOf(tegn) >= 0)) {
                resultat.setCharAt(i, erstattMed);
            }
        }
        return resultat.toString();
    }

    public static String[] filtrer(String[] tekster) {
        if (tekster == null) {
            return null;
        }
        for (int i = 0; i < tekster.length; i++) {
            tekster[i] = filtrer(tekster[i]);
        }
        return tekster;
    }

    /* Testprogram */
    public static void main(String[] args) {
        String test = "æøåÆØÅ ., bare lovlige tegn erstattes med \n"
                + InputFilter.filtrer("æøåÆØÅ ., bare lovlige tegn") + "\n"
                + "litt av hvert %&/(=)?>A>< erstattes med\n"
                + InputFilter.filtrer("litt av hvert %&/(=)?>A><");
        javax.swing.JOptionPane.showMessageDialog(null, test);

        String[] tekster = {"æøåÆØÅ ., bare lovlige tegn",
            "litt av hvert %&/(=)?>A><"};

        InputFilter.filtrer(tekster);
        test = "";
        for (String t : tekster) {
            test += t + "\n";
        }
        javax.swing.JOptionPane.showMessageDialog(null, test);
    }
}