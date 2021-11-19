package operator.window;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import com.google.gson.Gson;

/**
 * 
 * @author Lenovo
 *
 */
public class PageEventSchema implements DeserializationSchema<PageEvent>, SerializationSchema<PageEvent> {
	private static final Gson gson = new Gson();

    @Override
    public PageEvent deserialize(byte[] bytes) throws IOException {
        return gson.fromJson(new String(bytes,"UTF-8"), PageEvent.class);
    }

    @Override
    public boolean isEndOfStream(PageEvent metricEvent) {
        return false;
    }

    @Override
    public byte[] serialize(PageEvent metricEvent) {
        return gson.toJson(metricEvent).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public TypeInformation<PageEvent> getProducedType() {
        return TypeInformation.of(PageEvent.class);
    }
}

