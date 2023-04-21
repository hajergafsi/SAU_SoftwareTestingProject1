/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for counting the number of operators of the given type inside the java code 
* </p>
*/

package library;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.CommentFinder.Match;

public class OperatorFinder {
	//numerical
	static String[] numericalDoubleOperatorsRegex = new String[] {"\\+=","\\-=","\\/=","\\*=","%=","&=","\\|=","\\^="};
	static String[] numericalDoubleOperators = new String[] {"+=","-=","/=","*=","%=","&=","|=","^="};
	static String[] numericalSingleOperatorsRegex = new String[] {"\\+","\\-","\\*","\\/","%","&","\\|","\\^","=",};
	static String[] numericalSingleOperators = new String[] {"+","-","*","/","%","&","|","^","="};
	
	//logical
	static String[] logicalOperators = new String[]  {"&&","||","!"};
	static String[] logicalOperatorsRegex = new String[]  {"\\&\\&","\\|\\|","!"};
	
	//relational
	static String[] relationalSingleOperators = new String[]  {"<",">" };
	static String[] relationalDoubleOperators = new String[]  {"<=",">=","==","!=" };
	static String[] relationalSingleOperatorsRegex = new String[]  {"\\<","\\>" };
	static String[] relationalDoubleOperatorsRegex = new String[]  {"\\<=","\\>=","==","!=" };
	
	//Single
	static String[] singleOperatorsRegex = new String[] {"\\+","\\-","\\*","\\/","%","&","\\|","\\^","=","!","\\<","\\>"};
	static String[] singleOperators = new String[] {"+","-","*","/","%","&","|","^","=","!","<",">"};	
	
	//Double
	static String[] doubleOperatorsRegex = new String[] {"\\+=","-=","\\/=","\\*=","%=","&=","\\|=","\\^=","&&","\\|\\|","\\<=","\\>=","==","!="};
	static String[] doubleOperators = new String[] {"+=","-=","/=","*=","%=","&=","|=","^=","&&","||","<=",">=","==","!="};	
	
	private String text;
	private String[] operatorsRegexCode;
	private String[] simpleOperators;
	private String[] complexOperators;
	private String[] simpleOperatorsRegex;
	private String[] complexOperatorsRegex;
	
	private String regex;
	private String regex2;
	private int count;
	private EOperator operatorType;
	private SubstringCounter SC;
	Matcher matcher;
	Pattern pattern,pattern2;
	
	 static class Match {
	        int start;
	        int end;
	        String text;
	    }
	    
	List<Match> incrementationMatches;
	
	final String incrementRegex = "[\\(]*(\\s*(--|\\+\\+)\\s*)_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*|[\\(]*_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\)|\\[([^\\]\\\"]|\\\".*\\\")+\\])*(\\s*(--|\\+\\+))";
	
	public OperatorFinder(EOperator operatorType,String text) {
		
		this.text = text;
		this.operatorType = operatorType;
		this.count = 0;
		if(operatorType == EOperator.relational) {
			this.simpleOperators = relationalSingleOperators;
			this.complexOperatorsRegex = relationalDoubleOperatorsRegex;
			this.simpleOperatorsRegex = relationalSingleOperatorsRegex;
			this.complexOperators = relationalDoubleOperators;
		}else if (operatorType == EOperator.logical) {
			this.simpleOperators = logicalOperators;
			this.operatorsRegexCode = logicalOperatorsRegex;
		}else if (operatorType == EOperator.numerical) {
			this.simpleOperatorsRegex = numericalSingleOperatorsRegex;
			this.complexOperatorsRegex = numericalDoubleOperatorsRegex;
			this.simpleOperators = numericalSingleOperators;
			this.complexOperators = numericalDoubleOperators;
		}
		else if (operatorType == EOperator.single) {
			this.operatorsRegexCode = singleOperatorsRegex;
			this.simpleOperators = singleOperators;
		}
		else if (operatorType == EOperator.doubleOp) {
			this.operatorsRegexCode = doubleOperatorsRegex;
			this.simpleOperators = doubleOperators;
		}
		
		// regex simple ops
	    String regexPrefix = "[^";
	    
	    if(operatorType == EOperator.numerical)regexPrefix+="\\>\\<!";
	    if(operatorType == EOperator.logical)regexPrefix+="=";
	    
		if(operatorType == EOperator.numerical || operatorType == EOperator.relational) {
			for (int i = 0; i < simpleOperatorsRegex.length; i++) {
				if(i == simpleOperatorsRegex.length-1) {
					if(operatorType == EOperator.numerical) {
						regexPrefix += simpleOperatorsRegex[i] + "]+";
					}else {
						regexPrefix += simpleOperatorsRegex[i] + "=]+";
					}
					
				}else {
					regexPrefix += simpleOperatorsRegex[i];
				}			
			}
			
			regex = regexPrefix + "((";
			
			for (int i = 0; i < simpleOperatorsRegex.length; i++) {
				if(i == simpleOperatorsRegex.length-1) {
					regex += simpleOperatorsRegex[i] + ")";
				}else {
					regex += simpleOperatorsRegex[i] + '|';
				}			
			}
			regex += regexPrefix + ")+";
			
			pattern = Pattern.compile(regex,Pattern.MULTILINE);
			
			//regex double ops
			regexPrefix = "[^";
			
			for (int i = 0; i < complexOperatorsRegex.length; i++) {
				if(i == complexOperatorsRegex.length-1) {
					regexPrefix += complexOperatorsRegex[i] + "]+";
				}else {
					regexPrefix += complexOperatorsRegex[i];
				}			
			}
			
			regex2 = regexPrefix + "((";
			
			for (int i = 0; i < complexOperatorsRegex.length; i++) {
				if(i == complexOperatorsRegex.length-1) {
					regex2 += complexOperatorsRegex[i] + ")";
				}else {
					regex2 += complexOperatorsRegex[i] + '|';
				}			
			}
			
			regex2 += regexPrefix + ")+";
			pattern2 = Pattern.compile(regex2,Pattern.MULTILINE);
		}else {
			for (int i = 0; i < operatorsRegexCode.length; i++) {
				if(i == operatorsRegexCode.length-1) {
					if(operatorType == EOperator.doubleOp ||operatorType == EOperator.single ) {
						regexPrefix += operatorsRegexCode[i] + "=]+";
					}else {
						regexPrefix += operatorsRegexCode[i] + "]+";
					}
					
				}else {
					regexPrefix += operatorsRegexCode[i];
				}			
			}
			
			regex = regexPrefix + "((";
			
			for (int i = 0; i < operatorsRegexCode.length; i++) {
				if(i == operatorsRegexCode.length-1) {
					regex += operatorsRegexCode[i] + ")";
				}else {
					regex += operatorsRegexCode[i] + '|';
				}			
			}
			
			regex += regexPrefix + ")+";
			
			pattern = Pattern.compile(regex,Pattern.MULTILINE);			
		}
		
	}
	
	public int detectIncrementingOperators() {
		int opCount = 0;
		Pattern incrementPattern = Pattern.compile(incrementRegex);
		Matcher matcher = incrementPattern.matcher(text);
		this.incrementationMatches = new ArrayList<Match>();
        while (matcher.find()) {
        	Match match = new Match();       
	        match.start = matcher.start();
	        match.text = matcher.group();
	        match.end = matcher.end();
	        incrementationMatches.add(match);
	        this.text = text.replace(match.text,"operator");
	        opCount++;         	
        }
        return opCount;
	}
	
	public int FindOperators() {
		
		
		int inc = detectIncrementingOperators();
		matcher = pattern.matcher(text);
        while (matcher.find()) {
                for (String op: this.simpleOperators) {
                	SC = new SubstringCounter(matcher.group(),op);
                	count += SC.countMatches();
                }            	
        }
        
        if(operatorType == EOperator.numerical || operatorType == EOperator.relational) {
    		matcher = pattern2.matcher(text);
            while (matcher.find()) {
                    for (String op: this.complexOperators) {
                    	SC = new SubstringCounter(matcher.group(),op);
                    	count += SC.countMatches();
                    }            	
            }      	
        }

        if(this.operatorType == EOperator.numerical || this.operatorType ==  EOperator.doubleOp)count+=inc;
        return count;
		
	}
	
	public String getText () {
		return this.text;
	}
}
