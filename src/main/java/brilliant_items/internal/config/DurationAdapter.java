package brilliant_items.internal.config;

public class DurationAdapter extends com.google.gson.TypeAdapter<java.time.Duration> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, java.time.Duration v) throws java.io.IOException {
        if (v == null) out.nullValue(); else out.value(v.toString());
    }
    @Override
    public java.time.Duration read(com.google.gson.stream.JsonReader in) throws java.io.IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) { in.nextNull(); return null; }
        return java.time.Duration.parse(in.nextString());
    }
}