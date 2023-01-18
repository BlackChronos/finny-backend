package com.github.sbooster.templates.backend.rsocket.security.crypt;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("NullableProblems")
public class Jackson2JsonUnderByteCryptDecoder extends AbstractJackson2Decoder {
    private static final StringDecoder STRING_DECODER = StringDecoder.textPlainOnly(Arrays.asList(",", "\n"), false);
    private static final ResolvableType STRING_TYPE = ResolvableType.forClass(String.class);


    public Jackson2JsonUnderByteCryptDecoder() {
        super(Jackson2ObjectMapperBuilder.json().build());
    }

    @Override
    public Object decode(DataBuffer dataBuffer, ResolvableType targetType, MimeType mimeType, Map<String, Object> hints) throws DecodingException {
        String string = dataBuffer.toString(Charset.defaultCharset());
        String decrypted = ByteCryptUtils.decrypt(string);
        byte[] bytes = decrypted.getBytes(Charset.defaultCharset());
        return super.decode(dataBuffer.factory().wrap(bytes), targetType, mimeType, hints);
    }

    @Override
    protected Flux<DataBuffer> processInput(Publisher<DataBuffer> input,
                                            ResolvableType elementType,
                                            @Nullable MimeType mimeType,
                                            @Nullable Map<String, Object> hints) {
        Flux<DataBuffer> flux = Flux.from(input);
        if (mimeType == null) {
            return flux;
        }
        Charset charset = mimeType.getCharset();
        if (charset == null || StandardCharsets.UTF_8.equals(charset) || StandardCharsets.US_ASCII.equals(charset)) {
            return flux;
        }
        MimeType textMimeType = new MimeType(MimeTypeUtils.TEXT_PLAIN, charset);
        Flux<String> decoded = STRING_DECODER.decode(input, STRING_TYPE, textMimeType, null);
        return decoded.map(s ->
                DefaultDataBufferFactory.sharedInstance.wrap(
                        ByteCryptUtils.decrypt(s).getBytes(StandardCharsets.UTF_8)
                ));
    }
}
