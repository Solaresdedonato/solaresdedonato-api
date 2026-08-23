package ar.com.solaresdedonato.api.adapter.in.config.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class ApiPathsInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        try (InputStream is = new ClassPathResource("api-paths.yml").getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(is);
            @SuppressWarnings("unchecked")
            List<String> paths = (List<String>) data.get("paths");

            builder.withDetail("api", Map.of(
                    "kind", "api",
                    "paths", paths));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer api-paths.yml", e);
        }
    }
}
