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
		TRUE=1, FALSE=2, SINK=3, DEADLOCK=4, CURRENT=5, NOT=6, AND=7, OR=8, IMPLIES=9, 
		LEFT_PAREN=10, RIGHT_PAREN=11, WS=12;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'true'", "'false'", "'sink'", "'deadlock'", "'current'", "NOT", "AND", 
		"OR", "IMPLIES", "'('", "')'", "WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "SINK", "DEADLOCK", "CURRENT", "NOT", "AND", "OR", "IMPLIES", 
		"LEFT_PAREN", "RIGHT_PAREN", "WS"
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
		case 11: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\16[\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4"+
		"\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\5\7A\n\7\3\b\3\b\3\b"+
		"\3\b\5\bG\n\b\3\t\3\t\3\t\5\tL\n\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\6"+
		"\rV\n\r\r\r\16\rW\3\r\3\r\2\16\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17"+
		"\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\2\3\2\3\5\13\f\17\17\"\"^\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\3\33\3\2\2\2\5 \3\2\2\2\7&\3\2\2\2\t+\3\2\2\2\13\64\3\2\2\2\r"+
		"@\3\2\2\2\17F\3\2\2\2\21K\3\2\2\2\23M\3\2\2\2\25P\3\2\2\2\27R\3\2\2\2"+
		"\31U\3\2\2\2\33\34\7v\2\2\34\35\7t\2\2\35\36\7w\2\2\36\37\7g\2\2\37\4"+
		"\3\2\2\2 !\7h\2\2!\"\7c\2\2\"#\7n\2\2#$\7u\2\2$%\7g\2\2%\6\3\2\2\2&\'"+
		"\7u\2\2\'(\7k\2\2()\7p\2\2)*\7m\2\2*\b\3\2\2\2+,\7f\2\2,-\7g\2\2-.\7c"+
		"\2\2./\7f\2\2/\60\7n\2\2\60\61\7q\2\2\61\62\7e\2\2\62\63\7m\2\2\63\n\3"+
		"\2\2\2\64\65\7e\2\2\65\66\7w\2\2\66\67\7t\2\2\678\7t\2\289\7g\2\29:\7"+
		"p\2\2:;\7v\2\2;\f\3\2\2\2<=\7p\2\2=>\7q\2\2>A\7v\2\2?A\7#\2\2@<\3\2\2"+
		"\2@?\3\2\2\2A\16\3\2\2\2BC\7c\2\2CD\7p\2\2DG\7f\2\2EG\7(\2\2FB\3\2\2\2"+
		"FE\3\2\2\2G\20\3\2\2\2HI\7q\2\2IL\7t\2\2JL\7~\2\2KH\3\2\2\2KJ\3\2\2\2"+
		"L\22\3\2\2\2MN\7?\2\2NO\7@\2\2O\24\3\2\2\2PQ\7*\2\2Q\26\3\2\2\2RS\7+\2"+
		"\2S\30\3\2\2\2TV\t\2\2\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2XY\3\2"+
		"\2\2YZ\b\r\2\2Z\32\3\2\2\2\7\2@FKW";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}