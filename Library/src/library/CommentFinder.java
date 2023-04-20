/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class removes comments in the .java file to avoid including operator or functions in the total count
* </p>
*/

package library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommentFinder {
    Pattern commentsPattern = Pattern.compile("(//.*?($|\\n|\\\\n))|((?s)\\/\\*(.*?)\\*\\/)", Pattern.MULTILINE | Pattern.DOTALL);
    Pattern stringsPattern = Pattern.compile("(\".*?(?<!\\\\)\")");
    private String javaCode;
    
    static class Match {
        int start;
        String text;
    }
    
    List<Match> commentMatches;
    
    public CommentFinder(String code) {
    	this.javaCode = code;
    	this.commentMatches = new ArrayList<Match>();
    }
    
    public String removeComment(){
    	Matcher commentsMatcher = commentsPattern.matcher(javaCode);
	    while (commentsMatcher.find()) {
	    	
	        Match match = new Match();
	        
	        match.start = commentsMatcher.start();
	        match.text = commentsMatcher.group();
	        commentMatches.add(match);
	    }
	    List<Match> commentsToRemove = new ArrayList<Match>();

	    Matcher stringsMatcher = stringsPattern.matcher(javaCode);
	    while (stringsMatcher.find()) {
	        for (Match comment : commentMatches) {
	            if (comment.start > stringsMatcher.start() && comment.start < stringsMatcher.end())
	                commentsToRemove.add(comment);
	        }
	    }
	    for (Match comment : commentsToRemove)
	        commentMatches.remove(comment);

	    for (Match comment : commentMatches)
	    	javaCode = javaCode.replace(comment.text, "");

	    return javaCode;
    }

}
