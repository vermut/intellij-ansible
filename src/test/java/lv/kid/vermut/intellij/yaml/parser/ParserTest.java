package lv.kid.vermut.intellij.yaml.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.PlatformTestCase;
import org.junit.Assert;

public class ParserTest extends ParsingTestCase {

    public ParserTest() {
        super("", "yml", new NeonParserDefinition());
        PlatformTestCase.doAutodetectPlatformPrefix();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/data/parser";
    }

    protected void myDoTest(boolean checkResult, boolean suppressErrors) {
        doTest(true);
        if (!suppressErrors) {
            Assert.assertFalse(
                    "PsiFile contains error elements",
                    toParseTreeText(myFile, true, includeRanges()).contains("PsiErrorElement")
            );
        }
    }

    public void test01() {
        myDoTest(true, false);
    }

    public void test02() {
        myDoTest(true, false);
    }

    public void test03() {
        myDoTest(true, false);
    }

    public void test04() {
        myDoTest(true, false);
    }

    public void test05() {
        myDoTest(true, false);
    }

    public void test06() {
        myDoTest(true, false);
    }

    public void test07() {
        myDoTest(true, false);
    }

    public void test08() {
        myDoTest(true, false);
    }

    public void test09() {
        myDoTest(true, false);
    }

    public void test10() {
        myDoTest(true, false);
    }

    public void test11() {
        myDoTest(true, true);
    }

    public void test12() {
        myDoTest(true, true);
    }

    public void test13() {
        myDoTest(true, false);
    }

    public void test14() {
        myDoTest(true, true);
    }

    public void test15() {
        myDoTest(true, true);
    }

    public void test16() {
        myDoTest(true, true);
    }

    public void test17() {
        myDoTest(true, false);
    }

    public void test18() {
        myDoTest(true, true);
    }

    public void testIssue31() {
        myDoTest(true, false);
    }

    public void testIssue31_simple() {
        myDoTest(true, false);
    }

    public void testIssue40() {
        myDoTest(true, false);
    }

    public void testIssue42() {
        myDoTest(true, false);
    }

    public void testIssue41() {
        myDoTest(true, false);
    }
}
