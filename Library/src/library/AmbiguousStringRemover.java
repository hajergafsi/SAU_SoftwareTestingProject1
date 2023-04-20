/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class replaces strings that contain operators such as the format "a+b=c;" in order to avoid including them inside the operator count
* </p>
*/
package library;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.CommentFinder.Match;

public class AmbiguousStringRemover {
	private String code;
	final String regex = "\\\"([\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\))*|\\\"[^\\\"]*\\\"))*((\\s|\\\\n|\\\\r)*(\\+|\\+\\+|-|--|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|<|<=|>|>=|==|!=)(\\s|\\\\n|\\\\r)*([\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\))*|\\\"[^\\\"]*\\\"\\)))*(\\s|\\\\n|\\\\r)*\\)*)+(\\s|\\\\n|\\\\r)*\\)*(;|\\{)?\\\"";
	
	static class Match {
        int start;
        String text;
    }
    
    List<Match> stringMatches;
	
	Matcher matcher;
	Pattern pattern;
	
	public AmbiguousStringRemover(String code) {
		this.code = code;
		this.stringMatches = new ArrayList<Match>();
		pattern = Pattern.compile(regex,Pattern.MULTILINE);
	}
	
	public String replaceAmbiguous () {
		matcher = pattern.matcher(code);
	    while (matcher.find()) {
	    	
	        Match match = new Match();
	        
	        match.start = matcher.start();
	        match.text = matcher.group();
	        stringMatches.add(match);
	    }
	    List<Match> stringsToReplace = new ArrayList<Match>();

	    Matcher stringsMatcher = pattern.matcher(code);
	    while (stringsMatcher.find()) {
	        for (Match str : stringMatches) {
	            if (str.start > stringsMatcher.start() && str.start < stringsMatcher.end())
	            	stringsToReplace.add(str);
	        }
	    }
	    for (Match str : stringsToReplace)
	    	stringsToReplace.remove(str);

	    for (Match str1 : stringMatches)
	    	code = code.replace(str1.text, "param");

	    return code;
	}
	
}