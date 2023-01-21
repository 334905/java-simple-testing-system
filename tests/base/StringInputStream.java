package base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class StringInputStream extends InputStream {
    protected final String string;
    private int codePointOffset = 0;
    protected final Charset charset;
    private ByteBuffer codePointBuffer;

    public StringInputStream(final String string, final Charset charset) {
        this.string = string;
        this.charset = charset;
        codePointBuffer = charset.encode("");
    }

    @Override
    public int read() {
        if (!codePointBuffer.hasRemaining()) {
            if (codePointOffset >= string.length()) {
                return -1;
            }
            final int cp = string.codePointAt(codePointOffset);
            codePointOffset += Character.charCount(cp);
            codePointBuffer = charset.encode(CharBuffer.wrap(Character.toChars(cp)));
            assert codePointBuffer.hasRemaining();
        }
        return codePointBuffer.get();
    }
}
