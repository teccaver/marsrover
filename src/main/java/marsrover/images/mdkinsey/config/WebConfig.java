package marsrover.images.mdkinsey.config;

import marsrover.images.mdkinsey.converters.CameraToCameraData;
import marsrover.images.mdkinsey.converters.RoverToRoverData;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CameraToCameraData());
        registry.addConverter(new RoverToRoverData());

    }
}
