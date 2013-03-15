// Generated from Ltl.g4 by ANTLR 4.0

package de.prob.ltl.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LtlLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, SINK=3, DEADLOCK=4, CURRENT=5, NOT=6, GLOBALLY=7, FINALLY=8, 
		NEXT=9, HISTORICALLY=10, ONCE=11, YESTERDAY=12, AND=13, OR=14, IMPLIES=15, 
		UNTIL=16, WEAKUNTIL=17, RELEASE=18, SINCE=19, TRIGGER=20, LEFT_CURLY=21, 
		RIGHT_CURLY=22, PREDICATE=23, LEFT_BRACKET=24, RIGHT_BRACKET=25, ACTION=26, 
		ENABLED=27, LEFT_PAREN=28, RIGHT_PAREN=29, WS=30;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'true'", "'false'", "'sink'", "'deadlock'", "'current'", "NOT", "GLOBALLY", 
		"FINALLY", "NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", 
		"UNTIL", "WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "'{'", "'}'", "PREDICATE", 
		"'['", "']'", "ACTION", "ENABLED", "'('", "')'", "WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "SINK", "DEADLOCK", "CURRENT", "NOT", "GLOBALLY", "FINALLY", 
		"NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", "UNTIL", 
		"WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "LEFT_CURLY", "RIGHT_CURLY", 
		"PREDICATE", "LEFT_BRACKET", "RIGHT_BRACKET", "ACTION", "ENABLED", "LEFT_PAREN", 
		"RIGHT_PAREN", "WS"
	};


	public LtlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Ltl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 29: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4 \u00bb\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t"+
		"\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36"+
		"\t\36\4\37\t\37\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\5\7e\n\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\5\16w\n\16\3\17\3\17\3\17\5\17|\n\17"+
		"\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\3\30\3\30\3\30\7\30\u0092\n\30\f\30\16\30\u0095\13\30"+
		"\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\7\33\u00a0\n\33\f\33\16"+
		"\33\u00a3\13\33\3\33\3\33\3\34\3\34\3\34\7\34\u00aa\n\34\f\34\16\34\u00ad"+
		"\13\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37\6\37\u00b6\n\37\r\37\16\37\u00b7"+
		"\3\37\3\37\2 \3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13"+
		"\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25"+
		"\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67\35\19\36\1;\37\1"+
		"= \2\3\2\6\4}}\177\177\4]]__\3*+\5\13\f\17\17\"\"\u00c3\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2"+
		"\2\3?\3\2\2\2\5D\3\2\2\2\7J\3\2\2\2\tO\3\2\2\2\13X\3\2\2\2\rd\3\2\2\2"+
		"\17f\3\2\2\2\21h\3\2\2\2\23j\3\2\2\2\25l\3\2\2\2\27n\3\2\2\2\31p\3\2\2"+
		"\2\33v\3\2\2\2\35{\3\2\2\2\37}\3\2\2\2!\u0080\3\2\2\2#\u0082\3\2\2\2%"+
		"\u0084\3\2\2\2\'\u0086\3\2\2\2)\u0088\3\2\2\2+\u008a\3\2\2\2-\u008c\3"+
		"\2\2\2/\u008e\3\2\2\2\61\u0098\3\2\2\2\63\u009a\3\2\2\2\65\u009c\3\2\2"+
		"\2\67\u00a6\3\2\2\29\u00b0\3\2\2\2;\u00b2\3\2\2\2=\u00b5\3\2\2\2?@\7v"+
		"\2\2@A\7t\2\2AB\7w\2\2BC\7g\2\2C\4\3\2\2\2DE\7h\2\2EF\7c\2\2FG\7n\2\2"+
		"GH\7u\2\2HI\7g\2\2I\6\3\2\2\2JK\7u\2\2KL\7k\2\2LM\7p\2\2MN\7m\2\2N\b\3"+
		"\2\2\2OP\7f\2\2PQ\7g\2\2QR\7c\2\2RS\7f\2\2ST\7n\2\2TU\7q\2\2UV\7e\2\2"+
		"VW\7m\2\2W\n\3\2\2\2XY\7e\2\2YZ\7w\2\2Z[\7t\2\2[\\\7t\2\2\\]\7g\2\2]^"+
		"\7p\2\2^_\7v\2\2_\f\3\2\2\2`a\7p\2\2ab\7q\2\2be\7v\2\2ce\7#\2\2d`\3\2"+
		"\2\2dc\3\2\2\2e\16\3\2\2\2fg\7I\2\2g\20\3\2\2\2hi\7H\2\2i\22\3\2\2\2j"+
		"k\7Z\2\2k\24\3\2\2\2lm\7J\2\2m\26\3\2\2\2no\7Q\2\2o\30\3\2\2\2pq\7[\2"+
		"\2q\32\3\2\2\2rs\7c\2\2st\7p\2\2tw\7f\2\2uw\7(\2\2vr\3\2\2\2vu\3\2\2\2"+
		"w\34\3\2\2\2xy\7q\2\2y|\7t\2\2z|\7~\2\2{x\3\2\2\2{z\3\2\2\2|\36\3\2\2"+
		"\2}~\7?\2\2~\177\7@\2\2\177 \3\2\2\2\u0080\u0081\7W\2\2\u0081\"\3\2\2"+
		"\2\u0082\u0083\7Y\2\2\u0083$\3\2\2\2\u0084\u0085\7T\2\2\u0085&\3\2\2\2"+
		"\u0086\u0087\7U\2\2\u0087(\3\2\2\2\u0088\u0089\7V\2\2\u0089*\3\2\2\2\u008a"+
		"\u008b\7}\2\2\u008b,\3\2\2\2\u008c\u008d\7\177\2\2\u008d.\3\2\2\2\u008e"+
		"\u0093\5+\26\2\u008f\u0092\n\2\2\2\u0090\u0092\5/\30\2\u0091\u008f\3\2"+
		"\2\2\u0091\u0090\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093"+
		"\u0094\3\2\2\2\u0094\u0096\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0097\5-"+
		"\27\2\u0097\60\3\2\2\2\u0098\u0099\7]\2\2\u0099\62\3\2\2\2\u009a\u009b"+
		"\7_\2\2\u009b\64\3\2\2\2\u009c\u00a1\5\61\31\2\u009d\u00a0\n\3\2\2\u009e"+
		"\u00a0\5\65\33\2\u009f\u009d\3\2\2\2\u009f\u009e\3\2\2\2\u00a0\u00a3\3"+
		"\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4\3\2\2\2\u00a3"+
		"\u00a1\3\2\2\2\u00a4\u00a5\5\63\32\2\u00a5\66\3\2\2\2\u00a6\u00a7\7g\2"+
		"\2\u00a7\u00ab\59\35\2\u00a8\u00aa\n\4\2\2\u00a9\u00a8\3\2\2\2\u00aa\u00ad"+
		"\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ae\3\2\2\2\u00ad"+
		"\u00ab\3\2\2\2\u00ae\u00af\5;\36\2\u00af8\3\2\2\2\u00b0\u00b1\7*\2\2\u00b1"+
		":\3\2\2\2\u00b2\u00b3\7+\2\2\u00b3<\3\2\2\2\u00b4\u00b6\t\5\2\2\u00b5"+
		"\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2"+
		"\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00ba\b\37\2\2\u00ba>\3\2\2\2\f\2dv{\u0091"+
		"\u0093\u009f\u00a1\u00ab\u00b7";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}