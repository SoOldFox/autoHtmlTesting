package tel_ran.autotesting.model;

import java.util.Random;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import tel_ran.autotesting.validator.HtmlValidator;

public class TagNesting extends Task{

	private static final String CONDITION_TEXT = "Find and correct the mistake.";
	private static final String[][] TAG_PAIRS = {{"h2","span"},{"h2","a"},
												{"h3","a"},{"h3","span"},
												{"div","h2"},{"div","h3"},{"div", "p"},{"div", "a"},{"div", "span"},
												{"p","i"},{"p","b"}, {"p","a"},{"p","strong"},
												{"a","b"},{"a","i"}};
	private String tag0;
	private String tag1;
	
	public TagNesting() {
		setRandomTags();
		setCondition(CONDITION_TEXT);
		genereateData();
	}

	public TagNesting(String tagName0, String tagName1) {
		setTag0(tagName0);
		setTag1(tagName1);
		setCondition(CONDITION_TEXT);
		genereateData();
	}
	
	public void genereateData() {
		Document clearDoc = generateClearDoc(); 
		matching = clearDoc.html();
		data = generateTaskData(matching);
	}

	private Document generateClearDoc() {
		Document doc = prepareHtmlDoc();
		Element body = doc.body();
		Element parent = body.appendElement(tag0);
		if(tag0 == "div")
			parent.attr("id", lorem.getKeyWord());
		Element child = parent.appendElement(tag1);
		if(tag1 == "a") {
			child.attr("href", lorem.getUrl());
		} else if(tag1 == "span") {
			child.attr("id", lorem.getKeyWord());
		}
		child.text(lorem.getTitle(5, 10));
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
		
		Elements solEls = Parser.parse(solution, "").body().getAllElements();
		Elements mathcEls = Parser.parse(matching, "").body().getAllElements();
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

	private String generateTaskData(String clearString) {
		String taskPattern = "</" + tag1 + ">\\s*</" + tag0 + ">";
		String replacement = "</" + tag0 + "></" + tag1 + ">";
		return clearString.replaceFirst(taskPattern,replacement);
	}

	private void setCondition(String conditionText) {
		condition = conditionText;
		
	}
	
	public void setRandomTags() {
		int pairInd = new Random(System.currentTimeMillis()).nextInt(TAG_PAIRS.length);
		tag0 = TAG_PAIRS[pairInd][0];
		tag1 = TAG_PAIRS[pairInd][1];
	}

	public void setTag1(String tagName1) {
		if(Tag.isKnownTag(tagName1))
			tag1 = tagName1;
	}

	public void setTag0(String tagName0) {
		if(Tag.isKnownTag(tagName0))
			tag0 = tagName0;
	}
	
	public String getTag0() {
		return tag0;
	}

	public String getTag1() {
		return tag1;
	}

}
