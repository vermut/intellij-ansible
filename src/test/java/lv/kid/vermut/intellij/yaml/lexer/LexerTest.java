package lv.kid.vermut.intellij.yaml.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.UsefulTestCase;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.annotations.NonNls;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static lv.kid.vermut.intellij.yaml.lexer.NeonTokenTypes.*;

/**
 *
 */
@SuppressWarnings({"unchecked", "JUnit4AnnotatedMethodInJUnit3TestCase"})
public class LexerTest extends UsefulTestCase {
    // which lexer to test
    private static NeonLexer createLexer() {
        return new NeonLexer();
    }

    /*** helpers ***/

    /**
     * Test that lexing a given piece of code will give particular tokens
     *
     * @param text           Sample piece of neon code
     * @param expectedTokens List of tokens expected from lexer
     */
    protected static void doTest(@NonNls String text, @NonNls Pair<IElementType, String>[] expectedTokens) {
        Lexer lexer = createLexer();
        doTest(text, expectedTokens, lexer);
    }

    private static void doTest(String text, Pair<IElementType, String>[] expectedTokens, Lexer lexer) {
        lexer.start(text);
        int idx = 0;
        while (lexer.getTokenType() != null) {
            if (idx >= expectedTokens.length) fail("Too many tokens from lexer; unexpected " + lexer.getTokenType());

            Pair expected = expectedTokens[idx++];

            String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
            assertEquals(expected.snd, tokenText);

            assertEquals("Wrong token " + tokenText, expected.fst, lexer.getTokenType());
            lexer.advance();
        }

        if (idx < expectedTokens.length)
            fail("Not enough tokens from lexer, expected " + expectedTokens.length + " but got only " + idx);
    }


    /***
     * tests here
     ***/
    @Test
    public void testSimpleYaml() throws Exception {
        doTest("%YAML 1.2\n" +
                "---\n\n" +
                "name: 'Jan'", new Pair[]{
                Pair.of(NEON_TAG, "%YAML 1.2"),
                Pair.of(NEON_INDENT, "\n"),
                Pair.of(NEON_HEADER, "---"),
                Pair.of(NEON_INDENT, "\n"),
                Pair.of(NEON_INDENT, "\n"),
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_STRING, "'Jan'"),
        });
    }

    @Test
    public void testStringWithEqualSign() throws Exception {
        doTest("name: a == b", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "a == b"),
        });
    }

    @Test
    public void testStringWithCurly() throws Exception {
        doTest("name: {ansible: var }", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_CURLY, "{"),
                Pair.of(NEON_LITERAL, "ansible"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_CURLY, "}")
        });
    }

    @Test
    public void testContinuation() throws Exception {
        doTest("name: >\n" +
                "   var", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LINE_CONTINUATION, ">"),
                Pair.of(NEON_INDENT, "\n   "),
                Pair.of(NEON_LITERAL, "var"),
        });
    }

    @Test
    public void testStringWithJinjaVars() throws Exception {
        doTest("name: some text {{ var1 }} \"{{ var2 }} \\\" {{var3}}\"", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "some text"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var1"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "\""),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var2"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "\\\""),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_LITERAL, "var3"),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_LITERAL, "\""),
        });
    }

    public void testArrayWithJinjaVars() throws Exception {
        doTest("name: {foo: \"bar\",ansible: \"{{ var2 }} \\\" {{var3}}\" }", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_CURLY, "{"),
                Pair.of(NEON_LITERAL, "foo"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_STRING, "\"bar\""),
                Pair.of(NEON_ITEM_DELIMITER, ","),
                Pair.of(NEON_LITERAL, "ansible"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "\""),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var2"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "\\\""),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_LITERAL, "var3"),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_LITERAL, "\""),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_CURLY, "}"),
        });
    }

    public void testStringWithMultiAssigments() throws Exception {
        doTest("name: foo=baz var={{ value + 2 }} msg=text with {{ data }}", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "foo"),
                Pair.of(NEON_ASSIGNMENT, "="),
                Pair.of(NEON_LITERAL, "baz"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var"),
                Pair.of(NEON_ASSIGNMENT, "="),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "value + 2"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "msg"),
                Pair.of(NEON_ASSIGNMENT, "="),
                Pair.of(NEON_LITERAL, "text with"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "data"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
        });
    }

    public void testStringWithJinjaVars2() throws Exception {
        doTest("name: key=\"pref-{{ var }}\" key2=\"pref-{{var}}\"", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "key"),
                Pair.of(NEON_ASSIGNMENT, "="),
                Pair.of(NEON_LITERAL, "\"pref-"),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_LITERAL, "var"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_STRING, "\" key2=\""),
                Pair.of(NEON_LITERAL, "pref-"),
                Pair.of(NEON_LBRACE_JINJA, "{{"),
                Pair.of(NEON_LITERAL, "var"),
                Pair.of(NEON_RBRACE_JINJA, "}}"),
                Pair.of(NEON_LITERAL, "\""),
        });
    }


    @Test
    public void testSimple() throws Exception {
        doTest("name: 'Jan'", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " "),
                Pair.of(NEON_STRING, "'Jan'"),
        });
    }

    @Test
    public void testTabAfterKey() throws Exception {
        doTest("name: \t'Jan'\nsurname:\t \t 'Dolecek'", new Pair[]{
                Pair.of(NEON_LITERAL, "name"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, " \t"),
                Pair.of(NEON_STRING, "'Jan'"),
                Pair.of(NEON_INDENT, "\n"),
                Pair.of(NEON_LITERAL, "surname"),
                Pair.of(NEON_COLON, ":"),
                Pair.of(NEON_WHITESPACE, "\t \t "),
                Pair.of(NEON_STRING, "'Dolecek'"),
        });
    }

    @Test
    public void test01() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test02() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test03() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test04() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test05() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test06() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test07() throws Exception {
        doTestFromFile();
    }

    @Test
    public void test19() throws Exception {
        doTestFromFile();
    }

    @Test
    public void testIssue31() throws Exception {
        doTestFromFile();
    }

    @Test
    public void testIssue31_simple() throws Exception {
        doTestFromFile();
    }


    public void doTestFromFile() throws Exception {
        String code = doLoadFile("src/test/data/parser", getTestName(false) + ".yml");

        Lexer lexer = createLexer();
        StringBuilder sb = new StringBuilder();

        lexer.start(code);
        while (lexer.getTokenType() != null) {
            sb.append(lexer.getTokenType().toString());
            sb.append("\n");
            lexer.advance();
        }

//		System.out.println(sb);

        // Match to original
        String lexed = doLoadFile("src/test/data/parser", getTestName(false) + ".lexed");
        assertEquals(lexed, sb.toString());
    }

    private static String doLoadFile(String myFullDataPath, String name) throws IOException {
        String fullName = myFullDataPath + File.separatorChar + name;
        String text = FileUtil.loadFile(new File(fullName), CharsetToolkit.UTF8);
        text = StringUtil.convertLineSeparators(text);
        return text;
    }

}
