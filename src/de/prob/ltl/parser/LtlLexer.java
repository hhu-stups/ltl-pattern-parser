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
		TRUE=1, FALSE=2, SINK=3, DEADLOCK=4, CURRENT=5, LEFT_PAREN=6, RIGHT_PAREN=7, 
		WS=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'true'", "'false'", "'sink'", "'deadlock'", "'current'", "'('", "')'", 
		"WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "SINK", "DEADLOCK", "CURRENT", "LEFT_PAREN", "RIGHT_PAREN", 
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
		case 7: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\n?\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4"+
		"\t\t\t\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\7\3\7\3\b\3\b\3\t\6\t:\n\t\r\t\16\t;\3\t\3\t\2\n\3\3\1\5\4\1\7\5\1\t"+
		"\6\1\13\7\1\r\b\1\17\t\1\21\n\2\3\2\3\5\13\f\17\17\"\"?\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\3\23\3\2\2\2\5\30\3\2\2\2\7\36\3\2\2\2\t#\3\2\2\2\13,"+
		"\3\2\2\2\r\64\3\2\2\2\17\66\3\2\2\2\219\3\2\2\2\23\24\7v\2\2\24\25\7t"+
		"\2\2\25\26\7w\2\2\26\27\7g\2\2\27\4\3\2\2\2\30\31\7h\2\2\31\32\7c\2\2"+
		"\32\33\7n\2\2\33\34\7u\2\2\34\35\7g\2\2\35\6\3\2\2\2\36\37\7u\2\2\37 "+
		"\7k\2\2 !\7p\2\2!\"\7m\2\2\"\b\3\2\2\2#$\7f\2\2$%\7g\2\2%&\7c\2\2&\'\7"+
		"f\2\2\'(\7n\2\2()\7q\2\2)*\7e\2\2*+\7m\2\2+\n\3\2\2\2,-\7e\2\2-.\7w\2"+
		"\2./\7t\2\2/\60\7t\2\2\60\61\7g\2\2\61\62\7p\2\2\62\63\7v\2\2\63\f\3\2"+
		"\2\2\64\65\7*\2\2\65\16\3\2\2\2\66\67\7+\2\2\67\20\3\2\2\28:\t\2\2\29"+
		"8\3\2\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<=\3\2\2\2=>\b\t\2\2>\22\3\2\2"+
		"\2\4\2;";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}