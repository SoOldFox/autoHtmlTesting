package tel_ran.autotesting.model;

import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import com.thedeanda.lorem.LoremIpsum;

public abstract class Task {
	private static final String DEFAULT_BASE_URI = "";
	private static final String DEFAULT_DOC_TYPE = "html";
	private static final String DEFAULT_TYPE_PUBLIC = "";
	private static final String DEFAULT_SYSTEM_ID = "";
	private static final String TAG_TITLE = "title";
	private static final String TAG_META = "meta";
	private static final String TAG_H1 = "h1";
	private static final String META_HTTP_EQUIV_KEY = "http-equiv";
	private static final String META_HTTP_EQUIV_VAL = "Content-Type";
	private static final String META_CONTENT_KEY = "content";
	private static final String META_CONTENT_VAL = "text/html; charset=utf-8";
	private static final Random RND = new Random(System.currentTimeMillis());

	protected static final LoremIpsum lorem = LoremIpsum.getInstance();
	protected String condition;
	protected String data;
	protected String matching;

	abstract public boolean checkSolution(String solution) throws Exception;
	abstract public boolean checkSolution(String solution, String matching) throws Exception;
	
	public String getCondition() {
		return condition;
	}

	public String getData() {
		return data;
	}

	public String getMatching() {
		return matching;
	}

	protected Element getAnyElement(String[] values, String elText) {
		return new Element(getAnyTag(values), "").text(elText);
	}
	
	protected Element getAnyElement(String[] values) {
		return new Element(getAnyTag(values), "");
	}
	
	protected Tag getAnyTag(String[] values) {
		return Tag.valueOf(getAnyValue(values));
	}
	
	protected String getAnyValue(String[] values) {
		return values[RND.nextInt(values.length)];
	}


	protected Document prepareHtmlDoc() {

		Document doc = Document.createShell(DEFAULT_BASE_URI);
		doc.prependChild(new DocumentType(DEFAULT_DOC_TYPE, DEFAULT_TYPE_PUBLIC, DEFAULT_SYSTEM_ID, DEFAULT_BASE_URI));
		doc.head().appendElement(TAG_TITLE).text(lorem.getTitle(1, 3));
		doc.head().appendElement(TAG_META).attr(META_HTTP_EQUIV_KEY, META_HTTP_EQUIV_VAL)
										.attr(META_CONTENT_KEY, META_CONTENT_VAL);
		doc.body().appendElement(TAG_H1).text(lorem.getTitle(2, 5));
		return doc;
	}
}
