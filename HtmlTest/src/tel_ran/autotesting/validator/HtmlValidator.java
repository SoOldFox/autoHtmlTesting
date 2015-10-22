package tel_ran.autotesting.validator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.xml.sax.InputSource;

import nu.validator.messages.MessageEmitter;
import nu.validator.messages.MessageEmitterAdapter;
import nu.validator.messages.TextMessageEmitter;
import nu.validator.servlet.imagereview.ImageCollector;
import nu.validator.source.SourceCode;
import nu.validator.validation.SimpleDocumentValidator;
import nu.validator.xml.SystemErrErrorHandler;

public class HtmlValidator {
	/**
	 * Verifies that a HTML content is valid.
	 * @param htmlContent the HTML content
	 * @return true if it is valid, false otherwise
	 * @throws Exception
	 */
	public static boolean validateHtml( String htmlContent ) throws Exception {

		InputStream in = new ByteArrayInputStream( htmlContent.getBytes( "UTF-8" ));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		SourceCode sourceCode = new SourceCode();
		ImageCollector imageCollector = new ImageCollector(sourceCode);
		boolean showSource = false;
		MessageEmitter emitter = new TextMessageEmitter( out, false );
		MessageEmitterAdapter errorHandler = new MessageEmitterAdapter( sourceCode, showSource, imageCollector, 0, false, emitter );
		errorHandler.setErrorsOnly( true );

		SimpleDocumentValidator validator = new SimpleDocumentValidator();
		validator.setUpMainSchema( "http://s.validator.nu/html5-rdfalite.rnc", new SystemErrErrorHandler());
		validator.setUpValidatorAndParsers( errorHandler, true, false );
		validator.checkHtmlInputSource( new InputSource( in ));

		return 0 == errorHandler.getErrors();
	}
	
	public static boolean validateHtml( InputStream in ) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SourceCode sourceCode = new SourceCode();
		ImageCollector imageCollector = new ImageCollector(sourceCode);
		boolean showSource = false;
		MessageEmitter emitter = new TextMessageEmitter( out, false );
		MessageEmitterAdapter errorHandler = new MessageEmitterAdapter( sourceCode, showSource, imageCollector, 0, false, emitter );
		errorHandler.setErrorsOnly( true );

		SimpleDocumentValidator validator = new SimpleDocumentValidator();
		validator.setUpMainSchema( "http://s.validator.nu/html5-rdfalite.rnc", new SystemErrErrorHandler());
		validator.setUpValidatorAndParsers( errorHandler, true, false );
		validator.checkHtmlInputSource( new InputSource( in ));
		return 0 == errorHandler.getErrors();
	}
	
}
