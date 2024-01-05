// Generated from /home/tiduswr/java-projects/compilador-uepb/antlr-compiler-uepb/src/main/antlr4/com/uepb/UEPBLexer.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class UEPBLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PalChave=1, NumInt=2, NumReal=3, Ident=4, String=5, OpRel=6, OpArt=7, 
		AbrePar=8, FechaPar=9, AbreChave=10, FechaChave=11, Delim=12, Virgula=13, 
		PontoVirgula=14, EspacoBranco=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PalChave", "NumInt", "NumReal", "Ident", "String", "SeqEsc", "OpRel", 
			"OpArt", "AbrePar", "FechaPar", "AbreChave", "FechaChave", "Delim", "Virgula", 
			"PontoVirgula", "EspacoBranco"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "'('", "')'", "'{'", 
			"'}'", "':'", "','", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PalChave", "NumInt", "NumReal", "Ident", "String", "OpRel", "OpArt", 
			"AbrePar", "FechaPar", "AbreChave", "FechaChave", "Delim", "Virgula", 
			"PontoVirgula", "EspacoBranco"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public UEPBLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "UEPBLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u000f\u0090\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0003\u0000D\b\u0000\u0001\u0001\u0003\u0001G\b\u0001\u0001\u0001\u0004"+
		"\u0001J\b\u0001\u000b\u0001\f\u0001K\u0001\u0002\u0003\u0002O\b\u0002"+
		"\u0001\u0002\u0004\u0002R\b\u0002\u000b\u0002\f\u0002S\u0001\u0002\u0001"+
		"\u0002\u0004\u0002X\b\u0002\u000b\u0002\f\u0002Y\u0001\u0003\u0001\u0003"+
		"\u0005\u0003^\b\u0003\n\u0003\f\u0003a\t\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0005\u0004f\b\u0004\n\u0004\f\u0004i\t\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006{\b\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0000\u0000\u0010\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0000\r\u0006\u000f"+
		"\u0007\u0011\b\u0013\t\u0015\n\u0017\u000b\u0019\f\u001b\r\u001d\u000e"+
		"\u001f\u000f\u0001\u0000\u0006\u0002\u0000++--\u0003\u000009AZaz\u0002"+
		"\u0000\'\'\\\\\u0002\u0000!!==\u0003\u0000*+--//\u0003\u0000\t\n\r\r "+
		" \u00a3\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0001C\u0001\u0000\u0000\u0000\u0003"+
		"F\u0001\u0000\u0000\u0000\u0005N\u0001\u0000\u0000\u0000\u0007[\u0001"+
		"\u0000\u0000\u0000\tb\u0001\u0000\u0000\u0000\u000bl\u0001\u0000\u0000"+
		"\u0000\rz\u0001\u0000\u0000\u0000\u000f|\u0001\u0000\u0000\u0000\u0011"+
		"~\u0001\u0000\u0000\u0000\u0013\u0080\u0001\u0000\u0000\u0000\u0015\u0082"+
		"\u0001\u0000\u0000\u0000\u0017\u0084\u0001\u0000\u0000\u0000\u0019\u0086"+
		"\u0001\u0000\u0000\u0000\u001b\u0088\u0001\u0000\u0000\u0000\u001d\u008a"+
		"\u0001\u0000\u0000\u0000\u001f\u008c\u0001\u0000\u0000\u0000!\"\u0005"+
		"v\u0000\u0000\"#\u0005a\u0000\u0000#D\u0005r\u0000\u0000$%\u0005i\u0000"+
		"\u0000%&\u0005n\u0000\u0000&D\u0005t\u0000\u0000\'(\u0005f\u0000\u0000"+
		"()\u0005l\u0000\u0000)*\u0005o\u0000\u0000*+\u0005a\u0000\u0000+D\u0005"+
		"t\u0000\u0000,-\u0005s\u0000\u0000-.\u0005t\u0000\u0000./\u0005r\u0000"+
		"\u0000/0\u0005i\u0000\u000001\u0005n\u0000\u00001D\u0005g\u0000\u0000"+
		"23\u0005i\u0000\u00003D\u0005f\u0000\u000045\u0005w\u0000\u000056\u0005"+
		"h\u0000\u000067\u0005i\u0000\u000078\u0005l\u0000\u00008D\u0005e\u0000"+
		"\u00009:\u0005p\u0000\u0000:;\u0005r\u0000\u0000;<\u0005i\u0000\u0000"+
		"<=\u0005n\u0000\u0000=D\u0005t\u0000\u0000>?\u0005b\u0000\u0000?@\u0005"+
		"r\u0000\u0000@A\u0005e\u0000\u0000AB\u0005a\u0000\u0000BD\u0005k\u0000"+
		"\u0000C!\u0001\u0000\u0000\u0000C$\u0001\u0000\u0000\u0000C\'\u0001\u0000"+
		"\u0000\u0000C,\u0001\u0000\u0000\u0000C2\u0001\u0000\u0000\u0000C4\u0001"+
		"\u0000\u0000\u0000C9\u0001\u0000\u0000\u0000C>\u0001\u0000\u0000\u0000"+
		"D\u0002\u0001\u0000\u0000\u0000EG\u0007\u0000\u0000\u0000FE\u0001\u0000"+
		"\u0000\u0000FG\u0001\u0000\u0000\u0000GI\u0001\u0000\u0000\u0000HJ\u0002"+
		"09\u0000IH\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KI\u0001\u0000"+
		"\u0000\u0000KL\u0001\u0000\u0000\u0000L\u0004\u0001\u0000\u0000\u0000"+
		"MO\u0007\u0000\u0000\u0000NM\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000"+
		"\u0000OQ\u0001\u0000\u0000\u0000PR\u000209\u0000QP\u0001\u0000\u0000\u0000"+
		"RS\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000"+
		"\u0000TU\u0001\u0000\u0000\u0000UW\u0005.\u0000\u0000VX\u000209\u0000"+
		"WV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000"+
		"\u0000YZ\u0001\u0000\u0000\u0000Z\u0006\u0001\u0000\u0000\u0000[_\u0002"+
		"az\u0000\\^\u0007\u0001\u0000\u0000]\\\u0001\u0000\u0000\u0000^a\u0001"+
		"\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000"+
		"`\b\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bg\u0005\'\u0000"+
		"\u0000cf\u0003\u000b\u0005\u0000df\b\u0002\u0000\u0000ec\u0001\u0000\u0000"+
		"\u0000ed\u0001\u0000\u0000\u0000fi\u0001\u0000\u0000\u0000ge\u0001\u0000"+
		"\u0000\u0000gh\u0001\u0000\u0000\u0000hj\u0001\u0000\u0000\u0000ig\u0001"+
		"\u0000\u0000\u0000jk\u0005\'\u0000\u0000k\n\u0001\u0000\u0000\u0000lm"+
		"\u0005\\\u0000\u0000mn\u0005\'\u0000\u0000n\f\u0001\u0000\u0000\u0000"+
		"o{\u0005>\u0000\u0000pq\u0005>\u0000\u0000q{\u0005=\u0000\u0000r{\u0005"+
		"<\u0000\u0000st\u0005<\u0000\u0000t{\u0005=\u0000\u0000uv\u0005=\u0000"+
		"\u0000v{\u0005=\u0000\u0000wx\u0005!\u0000\u0000x{\u0005=\u0000\u0000"+
		"y{\u0007\u0003\u0000\u0000zo\u0001\u0000\u0000\u0000zp\u0001\u0000\u0000"+
		"\u0000zr\u0001\u0000\u0000\u0000zs\u0001\u0000\u0000\u0000zu\u0001\u0000"+
		"\u0000\u0000zw\u0001\u0000\u0000\u0000zy\u0001\u0000\u0000\u0000{\u000e"+
		"\u0001\u0000\u0000\u0000|}\u0007\u0004\u0000\u0000}\u0010\u0001\u0000"+
		"\u0000\u0000~\u007f\u0005(\u0000\u0000\u007f\u0012\u0001\u0000\u0000\u0000"+
		"\u0080\u0081\u0005)\u0000\u0000\u0081\u0014\u0001\u0000\u0000\u0000\u0082"+
		"\u0083\u0005{\u0000\u0000\u0083\u0016\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0005}\u0000\u0000\u0085\u0018\u0001\u0000\u0000\u0000\u0086\u0087\u0005"+
		":\u0000\u0000\u0087\u001a\u0001\u0000\u0000\u0000\u0088\u0089\u0005,\u0000"+
		"\u0000\u0089\u001c\u0001\u0000\u0000\u0000\u008a\u008b\u0005;\u0000\u0000"+
		"\u008b\u001e\u0001\u0000\u0000\u0000\u008c\u008d\u0007\u0005\u0000\u0000"+
		"\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008f\u0006\u000f\u0000\u0000"+
		"\u008f \u0001\u0000\u0000\u0000\u000b\u0000CFKNSY_egz\u0001\u0006\u0000"+
		"\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}