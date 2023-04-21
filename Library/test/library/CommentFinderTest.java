/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the CommentFinder class
* </p>
*/


package library;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import library.OperandAnalyzerTest.IOperand;
import library.OperandAnalyzerTest.IOperator;


@ExtendWith(MockitoExtension.class)
class CommentFinderTest {
	private CommentFinder CF;
	
	interface IGenerator {
		public String generate();
 	}
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@Tag("IntegrationTest")
	@DisplayName("Comment detecter test")
	@CsvFileSource(resources = "./comments.csv",delimiter= ':')
	void commentRegexTest(String comment, String afterFormat) throws IOException {
		CF = new CommentFinder(comment);
		String formatted = CF.removeComment();
		assertEquals(formatted, afterFormat);
	}
	
	@Test
	@DisplayName("Comment detector test with mock")
	@Tag("MockitoTest")
	void CommentTest() {
		IGenerator commentGenerator = Mockito.mock(IGenerator.class);
		IGenerator codeGenerator = Mockito.mock(IGenerator.class);
		Mockito.when(codeGenerator.generate()).thenReturn("function(String a, String b){/***this is a comment***/}\n");
		Mockito.when(commentGenerator.generate()).thenReturn("/*********this is a comment**********/");
		String text = commentGenerator.generate() + "\n" + codeGenerator.generate() + "\n" + commentGenerator.generate();
		CF = new CommentFinder(text);
		assertEquals(CF.removeComment(),"\nfunction(String a, String b){}\n\n");
	}

}
