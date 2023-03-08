import java.util.Objects;

public class MarkdownTest {
    public static void expectEquals(final String actual, final String expected) {
        if (!Objects.equals(actual, expected)) {
            throw new AssertionError("Wrong answer, expected " + expected + ", but actual " + actual);
        }
    }

    private static int test = 0;

    private static void printTest(final Object obj) {
        System.out.println("Test #" + ++test + ": " + obj);
    }

    private static void expectMarkdownEquals(final Text markdownable, final String expected) {
        printTest(markdownable);
        final StringBuilder sb = new StringBuilder();
        markdownable.toMarkdown(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectMarkdownEquals(final Emphasis markdownable, final String expected) {
        printTest(markdownable);
        final StringBuilder sb = new StringBuilder();
        markdownable.toMarkdown(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectMarkdownEquals(final Strong markdownable, final String expected) {
        printTest(markdownable);
        final StringBuilder sb = new StringBuilder();
        markdownable.toMarkdown(sb);
        expectEquals(sb.toString(), expected);
    }

    private static void expectMarkdownEquals(final Paragraph markdownable, final String expected) {
        printTest(markdownable);
        final StringBuilder sb = new StringBuilder();
        markdownable.toMarkdown(sb);
        expectEquals(sb.toString(), expected);
    }

    public static void main(String[] args) {
        expectMarkdownEquals(new Text("Hello, world"), "Hello, world");
        expectMarkdownEquals(new Emphasis(new Text("Hell"), new Text("o, world")), "*Hello, world*");
        expectMarkdownEquals(new Strong(new Text("Hello"), new Text(","), new Text(" world")), "__Hello, world__");
        expectMarkdownEquals(new Paragraph(new Text("Hello, world")), "Hello, world");
        expectMarkdownEquals(
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
                "1__2*345*6__7");
        expectMarkdownEquals(
                new Paragraph(
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
                ),
                "ab*cd__efgh__ij*kl");
        expectMarkdownEquals(
                new Paragraph(
                        new Text("z"),
                        new Emphasis(new Text("yx")),
                        new Text("w"),
                        new Strong(new Text("vu")),
                        new Text("t"),
                        new Strong(new Text("sr")),
                        new Text("q"),
                        new Emphasis(new Text("po")),
                        new Text("n")
                ),
                "z*yx*w__vu__t__sr__q*po*n");
        expectMarkdownEquals(
                new Paragraph(
                        new Strong(new Text("yx")),
                        new Emphasis(new Text("vu")),
                        new Emphasis(new Text("sr")),
                        new Strong(new Text("po"))
                ),
                "__yx__*vu**sr*__po__");
    }
}
