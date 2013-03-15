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
		RIGHT_CURLY=22, PREDICATE=23, LEFT_PAREN=24, RIGHT_PAREN=25, WS=26;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'true'", "'false'", "'sink'", "'deadlock'", "'current'", "NOT", "GLOBALLY", 
		"FINALLY", "NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", 
		"UNTIL", "WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "'{'", "'}'", "PREDICATE", 
		"'('", "')'", "WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "SINK", "DEADLOCK", "CURRENT", "NOT", "GLOBALLY", "FINALLY", 
		"NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", "UNTIL", 
		"WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "LEFT_CURLY", "RIGHT_CURLY", 
		"PREDICATE", "LEFT_PAREN", "RIGHT_PAREN", "WS"
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
		case 25: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\34\u009b\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"+
		"\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\5\7]\n\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\5\16"+
		"o\n\16\3\17\3\17\3\17\5\17t\n\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3"+
		"\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\7\30\u008a"+
		"\n\30\f\30\16\30\u008d\13\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\6\33\u0096"+
		"\n\33\r\33\16\33\u0097\3\33\3\33\2\34\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r"+
		"\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21"+
		"\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65"+
		"\34\2\3\2\4\4}}\177\177\5\13\f\17\17\"\"\u00a0\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\3\67\3\2\2\2\5<\3\2\2\2\7B\3\2\2\2\tG\3\2\2\2\13P\3"+
		"\2\2\2\r\\\3\2\2\2\17^\3\2\2\2\21`\3\2\2\2\23b\3\2\2\2\25d\3\2\2\2\27"+
		"f\3\2\2\2\31h\3\2\2\2\33n\3\2\2\2\35s\3\2\2\2\37u\3\2\2\2!x\3\2\2\2#z"+
		"\3\2\2\2%|\3\2\2\2\'~\3\2\2\2)\u0080\3\2\2\2+\u0082\3\2\2\2-\u0084\3\2"+
		"\2\2/\u0086\3\2\2\2\61\u0090\3\2\2\2\63\u0092\3\2\2\2\65\u0095\3\2\2\2"+
		"\678\7v\2\289\7t\2\29:\7w\2\2:;\7g\2\2;\4\3\2\2\2<=\7h\2\2=>\7c\2\2>?"+
		"\7n\2\2?@\7u\2\2@A\7g\2\2A\6\3\2\2\2BC\7u\2\2CD\7k\2\2DE\7p\2\2EF\7m\2"+
		"\2F\b\3\2\2\2GH\7f\2\2HI\7g\2\2IJ\7c\2\2JK\7f\2\2KL\7n\2\2LM\7q\2\2MN"+
		"\7e\2\2NO\7m\2\2O\n\3\2\2\2PQ\7e\2\2QR\7w\2\2RS\7t\2\2ST\7t\2\2TU\7g\2"+
		"\2UV\7p\2\2VW\7v\2\2W\f\3\2\2\2XY\7p\2\2YZ\7q\2\2Z]\7v\2\2[]\7#\2\2\\"+
		"X\3\2\2\2\\[\3\2\2\2]\16\3\2\2\2^_\7I\2\2_\20\3\2\2\2`a\7H\2\2a\22\3\2"+
		"\2\2bc\7Z\2\2c\24\3\2\2\2de\7J\2\2e\26\3\2\2\2fg\7Q\2\2g\30\3\2\2\2hi"+
		"\7[\2\2i\32\3\2\2\2jk\7c\2\2kl\7p\2\2lo\7f\2\2mo\7(\2\2nj\3\2\2\2nm\3"+
		"\2\2\2o\34\3\2\2\2pq\7q\2\2qt\7t\2\2rt\7~\2\2sp\3\2\2\2sr\3\2\2\2t\36"+
		"\3\2\2\2uv\7?\2\2vw\7@\2\2w \3\2\2\2xy\7W\2\2y\"\3\2\2\2z{\7Y\2\2{$\3"+
		"\2\2\2|}\7T\2\2}&\3\2\2\2~\177\7U\2\2\177(\3\2\2\2\u0080\u0081\7V\2\2"+
		"\u0081*\3\2\2\2\u0082\u0083\7}\2\2\u0083,\3\2\2\2\u0084\u0085\7\177\2"+
		"\2\u0085.\3\2\2\2\u0086\u008b\5+\26\2\u0087\u008a\n\2\2\2\u0088\u008a"+
		"\5/\30\2\u0089\u0087\3\2\2\2\u0089\u0088\3\2\2\2\u008a\u008d\3\2\2\2\u008b"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008e\3\2\2\2\u008d\u008b\3\2"+
		"\2\2\u008e\u008f\5-\27\2\u008f\60\3\2\2\2\u0090\u0091\7*\2\2\u0091\62"+
		"\3\2\2\2\u0092\u0093\7+\2\2\u0093\64\3\2\2\2\u0094\u0096\t\3\2\2\u0095"+
		"\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2"+
		"\2\2\u0098\u0099\3\2\2\2\u0099\u009a\b\33\2\2\u009a\66\3\2\2\2\t\2\\n"+
		"s\u0089\u008b\u0097";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}