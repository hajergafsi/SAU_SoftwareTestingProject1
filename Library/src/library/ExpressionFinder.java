/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for finding expressions containing one or more operators of different types
* </p>
*/

package library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionFinder{	
	
	private String code;
	final String regex = "\\s*([\\(\\[]*((\\s*(--|\\+\\+)\\s*)?_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*(\\s*(--|\\+\\+))?|\\\"[^\\\"]*\\\")\\s*)+((\\s|\\\\n|\\\\r)*(\\+|\\+\\+|-|--|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|<|<=|>|>=|==|!=)(\\s|\\\\n|\\\\r)*([\\(]*((\\s*(--|\\+\\+)\\s*)?_*([A-Za-z0-9]|__)+(\\.\\w|\\w|,|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*(\\s*(--|\\+\\+))?|\\\"[^\\\"]*\\\")(\\s|\\\\n|\\\\r)*\\)*\\]*)+(\\s|\\\\n|\\\\r)*\\)*)+(;|\\{)";
	final String incrementationCaseRegex = "[\\(\\[]*(\\s*(--|\\+\\+)\\s*)_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*|[\\(]*_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*(\\s*(--|\\+\\+))\\s*(;|\\{|\\)|\\,|\\])";
	private int numCount;
	private int logicalCount;
	private int relationalCount;
	private int doubleCount;
	private int singleCount;

	private OperatorFinder OF;
	
	Matcher matcher;
	Pattern pattern,incPattern;
	public ExpressionFinder(String code ) {
		this.code = code;
		this.numCount = 0;
		this.doubleCount = 0;
		this.logicalCount = 0;
		this.relationalCount = 0;
		this.singleCount = 0;
		pattern = Pattern.compile(regex,Pattern.MULTILINE);
		incPattern = Pattern.compile(incrementationCaseRegex,Pattern.MULTILINE);
	}	
	
	
	public int Analyze(EOperator operatorType) {
		matcher = pattern.matcher(code);
		//Removes expressions that are operations on incremented operators
        String cleaned = code;
        while (matcher.find()) {
        	OF = new OperatorFinder(operatorType,matcher.group());
        	switch (operatorType) {
        	case numerical: 
        		numCount+= OF.FindOperators();
        		break;
        	case logical: 
        		logicalCount+= OF.FindOperators();
        		break;
        	case relational: 
        		relationalCount+= OF.FindOperators();
            	break;
        	case doubleOp: 
        		doubleCount+= OF.FindOperators();
        		break;
        	case single: 
        		singleCount+= OF.FindOperators();
        		break;
        	default: break;
        	}
        	cleaned = cleaned.substring(0, matcher.start()) + OF.getText() + cleaned.substring(matcher.end());
        }	
        
        if(operatorType == EOperator.numerical || operatorType == EOperator.doubleOp) {
        	matcher = incPattern.matcher(cleaned);
        	while (matcher.find()) {
            	OF = new OperatorFinder(operatorType,matcher.group());
            	switch (operatorType) {
            	case numerical: 
            		numCount+= OF.FindOperators();
            		break;
            	case doubleOp: 
            		doubleCount+= OF.FindOperators();
            		break;
            	default: break;
            	}
        	}
        }
        
        switch (operatorType) {
    	case numerical: 
    		return numCount;
    	case logical: 
    		return logicalCount;
    	case relational: 
    		return relationalCount;
    	case doubleOp: 
    		return doubleCount;
    	case single: 
    		return singleCount;
    	default: return 0;
    	}
		
	}


}