import markup.*;

public class HtmlListTest extends MarkdownTest {
    private static void expectHtmlEquals(final Text htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final Emphasis htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final Strong htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final Paragraph htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final ListItem htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final OrderedList htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectHtmlEquals(final UnorderedList htmlMarkup, final String expected) {
        printTest(htmlMarkup);
        final StringBuilder sb = new StringBuilder();
        htmlMarkup.toHtml(sb);
        expectEquals(sb.toString(), expected);
    }

    public static void main(final String... args) {
        System.out.println("Testing markdown...");
        resetTestCounter();
        MarkdownTest.main();

        System.out.println("Testing basic html...");
        resetTestCounter();
        expectHtmlEquals(new Text("Hello, world"), "Hello, world");
        expectHtmlEquals(new Emphasis(new Text("Hell"), new Text("o, world")), "<em>Hello, world</em>");
        expectHtmlEquals(new Strong(new Text("Hello"), new Text(","), new Text(" world")), "<strong>Hello, world</strong>");
        expectHtmlEquals(new Paragraph(new Text("Hello, world")), "Hello, world");
        expectHtmlEquals(
                new Paragraph(
                        new Text("1"),
                        new Strong(
                                new Text("2"),
                                new Emphasis(
                                        new Text("3"),
                                        new Text("4"),
                                        new Text("5")
                                ),
                                new Text("6")
                        ),
                        new Text("7")
                ),
                "1<strong>2<em>345</em>6</strong>7");
        final Paragraph nestedMarkupParagraph = new Paragraph(
                new Text("ab"),
                new Emphasis(
                        new Text("cd"),
                        new Strong(
                                new Text("ef"),
                                new Text("gh")
                        ),
                        new Text("ij")
                ),
                new Text("kl")
        );
        final String nestedMarkupParagraphAnswer = "ab<em>cd<strong>efgh</strong>ij</em>kl";
        expectHtmlEquals(nestedMarkupParagraph, nestedMarkupParagraphAnswer);
        final Paragraph markupParagraph = new Paragraph(
                new Text("z"),
                new Emphasis(new Text("yx")),
                new Text("w"),
                new Strong(new Text("vu")),
                new Text("t"),
                new Strong(new Text("sr")),
                new Text("q"),
                new Emphasis(new Text("po")),
                new Text("n")
        );
        final String markupParagraphAnswer = "z<em>yx</em>w<strong>vu</strong>t<strong>sr</strong>q<em>po</em>n";
        expectHtmlEquals(markupParagraph, markupParagraphAnswer);
        expectHtmlEquals(
                new Paragraph(
                        new Strong(new Text("yx")),
                        new Emphasis(new Text("vu")),
                        new Emphasis(new Text("sr")),
                        new Strong(new Text("po"))
                ),
                "<strong>yx</strong><em>vu</em><em>sr</em><strong>po</strong>");

        System.out.println("Testing lists...");
        resetTestCounter();
        final ListItem li1 = new ListItem(new Paragraph(new Text("1.1")), new Paragraph(new Text("1.2")));
        expectHtmlEquals(li1, "<li>1.11.2</li>");
        final ListItem li2 = new ListItem(new Paragraph(new Text("2")));
        expectHtmlEquals(li2, "<li>2</li>");
        final ListItem li3 = new ListItem(nestedMarkupParagraph);
        expectHtmlEquals(li3, "<li>" + nestedMarkupParagraphAnswer + "</li>");
        final ListItem li4 = new ListItem(markupParagraph);
        expectHtmlEquals(li4, "<li>" + markupParagraphAnswer + "</li>");

        expectHtmlEquals(new OrderedList(li1), orderedList("1.11.2"));
        expectHtmlEquals(new UnorderedList(li2), unorderedList("2"));
        expectHtmlEquals(new OrderedList(li3), orderedList(nestedMarkupParagraphAnswer));
        expectHtmlEquals(new UnorderedList(li4), unorderedList(markupParagraphAnswer));

        final OrderedList list1 = new OrderedList(li1, li4, li2);
        final String list1Answer = orderedList("1.11.2", markupParagraphAnswer, "2");
        final UnorderedList list2 = new UnorderedList(li4, li2, li3);
        final String list2Answer = unorderedList(markupParagraphAnswer, "2", nestedMarkupParagraphAnswer);
        expectHtmlEquals(list1, list1Answer);
        expectHtmlEquals(list2, list2Answer);

        final ListItem nestedLi1 = new ListItem(list1, new Paragraph(new Text("!"), new Strong(new Text("?"))), list2);
        final String nestedLi1Answer = "<li>" + list1Answer + "!<strong>?</strong>" + list2Answer + "</li>";
        final ListItem nestedLi2 = new ListItem(nestedMarkupParagraph, new Paragraph(new Emphasis(new Text("^")), new Text("#")), list2);
        final String nestedLi2Answer = "<li>" + nestedMarkupParagraphAnswer + "<em>^</em>#" + list2Answer + "</li>";
        expectHtmlEquals(nestedLi1, nestedLi1Answer);
        expectHtmlEquals(nestedLi2, nestedLi2Answer);

        expectHtmlEquals(
                new OrderedList(nestedLi1, nestedLi2, li1, li2, li3, li4),
                "<ol>" + nestedLi1Answer + nestedLi2Answer + "<li>1.11.2</li><li>2</li><li>" + nestedMarkupParagraphAnswer + "</li><li>" + markupParagraphAnswer + "</li></ol>"
        );
        expectHtmlEquals(
                new UnorderedList(li3, nestedLi2, li1, nestedLi2, li2, li4),
                "<ul><li>" + nestedMarkupParagraphAnswer + "</li>" + nestedLi2Answer + "<li>1.11.2</li>" + nestedLi2Answer + "<li>2</li>" + "<li>" + markupParagraphAnswer + "</li></ul>"
        );
    }

    private static String orderedList(final String... strings) {
        final StringBuilder sb = new StringBuilder("<ol>");
        listItems(sb, strings);
        sb.append("</ol>");
        return sb.toString();
    }

    private static void listItems(final StringBuilder sb, final String... strings) {
        for (final String string : strings) {
            sb.append("<li>").append(string).append("</li>");
        }
    }

    private static String unorderedList(final String... strings) {
        final StringBuilder sb = new StringBuilder("<ul>");
        listItems(sb, strings);
        sb.append("</ul>");
        return sb.toString();
    }
}
