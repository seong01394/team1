package dev.mvc.team1;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.club.Club;
import dev.mvc.commu.Commu;
import dev.mvc.news.News;
import dev.mvc.survey.Surveys;
import dev.mvc.survey_topic.Surveytopic;
import dev.mvc.tool.Tool;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows: path = "C:/kd/deploy/resort/contents/storage";
        // ▶ file:///C:/kd/deploy/resort/contents/storage
      
        // Ubuntu: path = "/home/ubuntu/deploy/resort/contents/storage";
        // ▶ file:////home/ubuntu/deploy/resort/contents/storage
      
        // C:/kd/deploy/resort/contents/storage ->  /contents/storage -> http://localhost:9091/contents/storage;
        registry.addResourceHandler("/store/storage/**").addResourceLocations("file:///" +  Surveys.getUploadDir());
        registry.addResourceHandler("/club/storage/**").addResourceLocations("file:///" +  Club.getUploadDir());
        registry.addResourceHandler("/store/storage/**").addResourceLocations("file:///" +  Surveytopic.getUploadDir());
        registry.addResourceHandler("/commu/storage/**").addResourceLocations("file:///" +  Commu.getUploadDir());
        registry.addResourceHandler("/news/storage/**").addResourceLocations("file:///" +  News.getUploadDir());
        
        // C:/kd/deploy/resort/food/storage ->  /food/storage -> http://localhost:9091/food/storage;
        // registry.addResourceHandler("/food/storage/**").addResourceLocations("file:///" +  Food.getUploadDir());

        // C:/kd/deploy/resort/trip/storage ->  /trip/storage -> http://localhost:9091/trip/storage;
        // registry.addResourceHandler("/trip/storage/**").addResourceLocations("file:///" +  Trip.getUploadDir());
        
    }
}