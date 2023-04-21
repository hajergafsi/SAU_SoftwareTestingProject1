package library;

import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({AmbiguousStringRemoverTest.class,OperandAnalyzerTest.class,ExpressionFinderTest.class,CommentFinderTest.class,DocumentTest.class,SubstringCounterTest.class,OperatorFinderTest.class})
@IncludeTags("ParameterizedTest")
public class ParameterizedTests {

}
