package unicode;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Locale;

/**
 * Collator evaluation
 * 
 * @author Paul Kulitski
 */
public class CollatorUtil {

    public static void main(String[] args) {
        Collator collator = Collator.getInstance(Locale.US);
        System.out.println("Collator: " + collator);
        String original = "abc";
        String tested = "ABC";
        
        // collation
        collator.setStrength(Collator.PRIMARY);
        int c1 = collator.compare(original, tested);
        collator.setStrength(Collator.SECONDARY);
        int c2 = collator.compare(original, tested);
        collator.setStrength(Collator.TERTIARY);
        int c3 = collator.compare(original, tested);
        System.out.println(c1 + " " + c2 + " " + c3);
        
        // collaion with key
        CollationKey key1 = collator.getCollationKey(original);
        CollationKey key2 = collator.getCollationKey(tested);
        System.out.println(key1.compareTo(key2));
    }
}
