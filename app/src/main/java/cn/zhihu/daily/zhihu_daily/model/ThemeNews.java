package cn.zhihu.daily.zhihu_daily.model;

import java.util.List;

/**
 * Created by morc on 16-12-9.
 */

public class ThemeNews {
    private List<Summary> stories;
    private String description;
    private String background;
    private String image;
    private String name;

    public List<Summary> getStories() {
        return stories;
    }

    public void setStories(List<Summary> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
