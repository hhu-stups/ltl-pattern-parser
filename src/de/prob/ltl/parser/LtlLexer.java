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
		UNTIL=16, WEAKUNTIL=17, RELEASE=18, SINCE=19, TRIGGER=20, LEFT_PAREN=21, 
		RIGHT_PAREN=22, WS=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'true'", "'false'", "'sink'", "'deadlock'", "'current'", "NOT", "GLOBALLY", 
		"FINALLY", "NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", 
		"UNTIL", "WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "'('", "')'", "WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "SINK", "DEADLOCK", "CURRENT", "NOT", "GLOBALLY", "FINALLY", 
		"NEXT", "HISTORICALLY", "ONCE", "YESTERDAY", "AND", "OR", "IMPLIES", "UNTIL", 
		"WEAKUNTIL", "RELEASE", "SINCE", "TRIGGER", "LEFT_PAREN", "RIGHT_PAREN", 
		"WS"
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
		case 22: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\31\u0087\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"+
		"\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\5\7W\n\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\5\16i\n\16\3\17\3\17\3\17\5\17n\n\17"+
		"\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\3\30\6\30\u0082\n\30\r\30\16\30\u0083\3\30\3\30\2\31\3"+
		"\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r"+
		"\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27"+
		"\1-\30\1/\31\2\3\2\3\5\13\f\17\17\"\"\u008a\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2"+
		"\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\3\61\3\2\2\2\5\66\3\2\2"+
		"\2\7<\3\2\2\2\tA\3\2\2\2\13J\3\2\2\2\rV\3\2\2\2\17X\3\2\2\2\21Z\3\2\2"+
		"\2\23\\\3\2\2\2\25^\3\2\2\2\27`\3\2\2\2\31b\3\2\2\2\33h\3\2\2\2\35m\3"+
		"\2\2\2\37o\3\2\2\2!r\3\2\2\2#t\3\2\2\2%v\3\2\2\2\'x\3\2\2\2)z\3\2\2\2"+
		"+|\3\2\2\2-~\3\2\2\2/\u0081\3\2\2\2\61\62\7v\2\2\62\63\7t\2\2\63\64\7"+
		"w\2\2\64\65\7g\2\2\65\4\3\2\2\2\66\67\7h\2\2\678\7c\2\289\7n\2\29:\7u"+
		"\2\2:;\7g\2\2;\6\3\2\2\2<=\7u\2\2=>\7k\2\2>?\7p\2\2?@\7m\2\2@\b\3\2\2"+
		"\2AB\7f\2\2BC\7g\2\2CD\7c\2\2DE\7f\2\2EF\7n\2\2FG\7q\2\2GH\7e\2\2HI\7"+
		"m\2\2I\n\3\2\2\2JK\7e\2\2KL\7w\2\2LM\7t\2\2MN\7t\2\2NO\7g\2\2OP\7p\2\2"+
		"PQ\7v\2\2Q\f\3\2\2\2RS\7p\2\2ST\7q\2\2TW\7v\2\2UW\7#\2\2VR\3\2\2\2VU\3"+
		"\2\2\2W\16\3\2\2\2XY\7I\2\2Y\20\3\2\2\2Z[\7H\2\2[\22\3\2\2\2\\]\7Z\2\2"+
		"]\24\3\2\2\2^_\7J\2\2_\26\3\2\2\2`a\7Q\2\2a\30\3\2\2\2bc\7[\2\2c\32\3"+
		"\2\2\2de\7c\2\2ef\7p\2\2fi\7f\2\2gi\7(\2\2hd\3\2\2\2hg\3\2\2\2i\34\3\2"+
		"\2\2jk\7q\2\2kn\7t\2\2ln\7~\2\2mj\3\2\2\2ml\3\2\2\2n\36\3\2\2\2op\7?\2"+
		"\2pq\7@\2\2q \3\2\2\2rs\7W\2\2s\"\3\2\2\2tu\7Y\2\2u$\3\2\2\2vw\7T\2\2"+
		"w&\3\2\2\2xy\7U\2\2y(\3\2\2\2z{\7V\2\2{*\3\2\2\2|}\7*\2\2},\3\2\2\2~\177"+
		"\7+\2\2\177.\3\2\2\2\u0080\u0082\t\2\2\2\u0081\u0080\3\2\2\2\u0082\u0083"+
		"\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0085\3\2\2\2\u0085"+
		"\u0086\b\30\2\2\u0086\60\3\2\2\2\7\2Vhm\u0083";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}