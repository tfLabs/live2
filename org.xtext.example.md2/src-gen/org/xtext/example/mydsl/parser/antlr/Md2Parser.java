/*
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.xtext.example.mydsl.parser.antlr.internal.InternalMd2Parser;
import org.xtext.example.mydsl.services.Md2GrammarAccess;

public class Md2Parser extends AbstractAntlrParser {

	@Inject
	private Md2GrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalMd2Parser createParser(XtextTokenStream stream) {
		return new InternalMd2Parser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}

	public Md2GrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(Md2GrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
