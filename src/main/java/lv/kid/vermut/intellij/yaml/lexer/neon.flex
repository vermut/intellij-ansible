package lv.kid.vermut.intellij.yaml.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static lv.kid.vermut.intellij.yaml.lexer.NeonTokenTypes.*;
%%
%class _NeonLexer
%implements FlexLexer
%public
// %debug
%unicode
%function advance
%type IElementType
%{
    private int yycolumn = 0;
    private int a = 0;
	private void retryInState(int newState) {
        yybegin(newState);
        yypushback(yylength());
	}
%}

STRING = \'[^\'\n,]*\'|\"(\\.|[^\"{}\\\n,])*\"
COMMENT = \#.*
YAML_HEADER = ---.*
YAML_TAG = %.*
INDENT = \n[\t ]*
LITERAL_START = [^#,=\[\]{}\x00-\x20!`]
JINJA_START = "{{" | "{%"
JINJA_STOP = "}}" | "%}"
WHITESPACE = [\t ]+
Identifier = [:jletter:] [:jletterdigit:]*
NEWLINE = \r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]

%state IN_LITERAL
%state IN_ASSIGNMENT
%state IN_ASSIGNMENT_LITERAL
%%

<YYINITIAL> {
    {JINJA_START}    {  a=111;return NEON_LBRACE_JINJA;    }
    {JINJA_STOP}     {  a=112;return NEON_RBRACE_JINJA;    }

    {Identifier} "=" {
          a=2;retryInState(IN_ASSIGNMENT);
    }

    {STRING} {
        return NEON_STRING;
    }

    ^{YAML_TAG} {
        return NEON_TAG;
    }

    {YAML_HEADER} {
        return NEON_HEADER;
    }

    "!"{Identifier} {
            return NEON_TAG;
    }

    "-" / [ \t\n] { return NEON_ARRAY_BULLET; }
    "-" $ { return NEON_ARRAY_BULLET; }
    ":" / [ \t\n,\]})] { return NEON_COLON; }
    ":" $ { return NEON_COLON; }
    ","   { return NEON_ITEM_DELIMITER; }
    ">" | "|" {WHITESPACE}* { return NEON_LINE_CONTINUATION; }

    "{" { return NEON_LBRACE_CURLY; }
    "}" { return NEON_RBRACE_CURLY; }
    "[" { return NEON_LBRACE_SQUARE; }
    "]" { return NEON_RBRACE_SQUARE; }
    "=" { return NEON_ASSIGNMENT; }

    {COMMENT} {
        return NEON_COMMENT;
    }

    {INDENT} {
        return NEON_INDENT;
    }

    {LITERAL_START} {
        yybegin(IN_LITERAL);
        return NEON_LITERAL;
    }

    {WHITESPACE} {
        return NEON_WHITESPACE;
    }

    . {
        return NEON_UNKNOWN;
    }
}

<IN_LITERAL> {
    {WHITESPACE}* {JINJA_START} | {WHITESPACE}* {JINJA_STOP}  { a=3011;retryInState(YYINITIAL); }
    [^,:\]{}\x00-\x20]+    { a=302; }
    [ \t]+[^#,:\]{}\x00-\x20] { a=303; }
    ":"[ \t\n,\]})]         { a=304;retryInState(YYINITIAL); }
    ":"$                    { a=305;retryInState(YYINITIAL); }
    ":"                     { a=306; }
    .|\n                    { a=307;retryInState(YYINITIAL); }
}

<IN_ASSIGNMENT_LITERAL> {
    {WHITESPACE}* {JINJA_START} | {WHITESPACE}* {JINJA_STOP}   { a=5011;retryInState(IN_ASSIGNMENT); }
    {WHITESPACE}+ {Identifier} {WHITESPACE}* "="    { a=501; retryInState(IN_ASSIGNMENT); }
    "="             { a=502; retryInState(IN_ASSIGNMENT); }
    {NEWLINE}            { a=503;retryInState(IN_ASSIGNMENT); }
    // Just to make next one faster
    [:jletter:]* [:jletterdigit:]*               { a=504; }
    .               { a=505; }
}

<IN_ASSIGNMENT> {
    {STRING}         {  a=401;return NEON_STRING; }
    "="              {  a=402;return NEON_ASSIGNMENT; }
    {WHITESPACE}     {  a=403;return NEON_WHITESPACE;    }
    {JINJA_START}    {  a=411;return NEON_LBRACE_JINJA;    }
    {JINJA_STOP}     {  a=412;return NEON_RBRACE_JINJA;    }

    {LITERAL_START}  {
        a=405;retryInState(IN_ASSIGNMENT_LITERAL);
        return NEON_LITERAL;
    }

    .|\n { a=406;retryInState(YYINITIAL); }
}
