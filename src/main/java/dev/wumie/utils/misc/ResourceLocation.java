package dev.wumie.utils.misc;

import lombok.Getter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ResourceLocation {
    @Getter private final String namespace;
    @Getter private final String path;

    public ResourceLocation(String path) {
        this("firebot",path);
    }

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public Reader getReader(Charset charset) {
        return new InputStreamReader(this.getInputStream(),charset);
    }

    public Reader getReader() {
        return new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8);
    }

    public InputStream getInputStream() {
        return this.getClass().getResourceAsStream("/assets/"+namespace+"/"+path);
    }

    public long copyTo(File target) throws IOException {
        if (!target.exists()) {
            target.createNewFile();
        }
        return Files.copy(this.getInputStream(),target.toPath());
    }
}
