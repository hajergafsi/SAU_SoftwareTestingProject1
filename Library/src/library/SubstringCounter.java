/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for counting the number of occurences of a given substring inside a string
* </p>
*/
package library;

public class SubstringCounter {
	private String mainText;
	private String subString;
	
	public SubstringCounter (String mainText,String subString) {
		this.mainText = mainText;
		this.subString = subString;
	}
	
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
 
    /* Counts how many times the substring appears in the larger string. */
    public int countMatches()
    {
        if (isEmpty(mainText) || isEmpty(subString)) {
            return 0;
        }
 
        int index = 0, count = 0;
        while (true)
        {
            index = mainText.indexOf(subString, index);
            if (index != -1)
            {
                count ++;
                index += subString.length();
            }
            else {
                break;
            }
        }
 
        return count;
    }
}
