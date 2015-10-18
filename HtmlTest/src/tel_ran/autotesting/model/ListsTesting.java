package tel_ran.autotesting.model;

import java.util.Random;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import tel_ran.autotesting.validator.HtmlValidator;

public class ListsTesting extends Task {

	
	private static final String[] LIST_TYPES = {"ul","ol"};
	private static final String[] INLINE_TAGS = {"b","i","dfn","strong","span"};
	private static final String[] NO_WRAPPING = {"p","div","pre"};
	private static final String LIST_ITEM = "li";
	private static final int MIN_LIST_ITEMS = 3;
	private static final int MAX_LIST_ITEMS = 6;
	private static final int LIMIT_LIST_ITEMS = 20;
	private static final Random RND = new Random(System.currentTimeMillis());
	private static final String CONDITION_TEXT = "Find and correct the mistake.";
	private static final String COMMENT_NODE = "#comment";

	private String listType;
	private int nItems;
	
	public ListsTesting() {
		listType = getAnyValue(LIST_TYPES);
		nItems = MIN_LIST_ITEMS + RND.nextInt(MAX_LIST_ITEMS-MIN_LIST_ITEMS+1);
		setCondition(CONDITION_TEXT);
		genereateData();
	}

	public ListsTesting(String listType, int nItems) {
		for(String lt:LIST_TYPES)
			if(listType.equalsIgnoreCase(lt)) {
				this.listType = lt;
				break;
			}
		if(this.listType == null) {
			listType = getAnyValue(LIST_TYPES);
		}
		if(nItems < MIN_LIST_ITEMS)
			this.nItems = MIN_LIST_ITEMS;
		else if (nItems > LIMIT_LIST_ITEMS)
			this.nItems = LIMIT_LIST_ITEMS;
		setCondition(CONDITION_TEXT);
		genereateData();
	}

	public void genereateData() {
		Document doc = generateClearDoc();
		int rndItem = RND.nextInt(nItems);
		String keyString = "";
		for(Element el:doc.getElementsByTag(LIST_ITEM)){
			int curIndex = el.elementSiblingIndex(); 
			if (curIndex != rndItem-1 && curIndex != rndItem) {
				keyString = el.ownText();
				el.prependChild(getAnyElement(INLINE_TAGS,lorem.getKeyWord()+" - "));
				break;
			}
		}
		Document clearDoc = doc.clone();
		Element noWrapEl = getAnyElement(NO_WRAPPING);
		clearDoc.body().prependChild(new Comment(noWrapEl.tagName(),""));
		for(Element el:doc.getElementsByTag(LIST_ITEM)){
			if(el.elementSiblingIndex() == rndItem) {
				el.wrap(noWrapEl.toString());
				break;
			}
		}
		matching = clearDoc.html();
		if(!keyString.isEmpty()) {
			data = doc.html().replaceAll(keyString+"</" + LIST_ITEM + ">", keyString);
		} else {
			data = doc.html();
		}
	}
	
	
	private Document generateClearDoc() {
		Document doc = prepareHtmlDoc();
		Element body = doc.body();
		Element list = body.appendElement(listType);
		if(listType == "ol")
			list.attr("start", ((Integer) RND.nextInt(MAX_LIST_ITEMS)).toString());
		for(int i = 0; i < nItems; i++) {
			Element item = list.appendElement(LIST_ITEM);
			item.text(lorem.getWords(1, MAX_LIST_ITEMS));
		}
	
		return doc;
	}
	
	@Override
	public boolean checkSolution(String solution) throws Exception {
		return checkSolution(solution, getMatching());
	}

	@Override
	public boolean checkSolution(String solution, String matching) throws Exception {
		if(!HtmlValidator.validateHtml(solution))
			return false;
		Element solBody = Parser.parse(solution, "").body();
		Element matchBody = Parser.parse(matching, "").body();
		for(Node node:matchBody.childNodes()) {
			if(node.nodeName() == COMMENT_NODE) {
				for(Element noWrapEl : solBody.getElementsByTag(((Comment) node).getData())) {
					noWrapEl.unwrap();
				}
				break;
			}
		}
		Elements solEls = solBody.getAllElements();
		Elements mathcEls = matchBody.getAllElements();
		int matchSize = mathcEls.size();
		if(matchSize != solEls.size())
			return false;
		for(int i = 0; i < matchSize; i++) {
			if(solEls.get(i).tagName() != mathcEls.get(i).tagName())
				return false;
			if(!solEls.get(i).attributes().equals(mathcEls.get(i).attributes()))
				return false;
			if(!solEls.get(i).ownText().matches(mathcEls.get(i).ownText().replaceAll("\\s+", "\\\\s*?")))
				return false;
		}
		return true;
	}

	private void setCondition(String conditionText) {
		condition = conditionText;
	}


}
