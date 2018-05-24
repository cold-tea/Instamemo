package com.heavenlyhell.instamemo.models;

/**
 * Created by sande on 3/6/2018.
 */

public class UserAccountSetting {
    private String description;
    private String display_name;
    private String posts;
    private String following;
    private String followers;
    private String username;
    private String website;
    private String profile_photo;

    public UserAccountSetting(String description, String display_name, String posts, String following, String followers,
                              String username, String website, String profile_photo) {
        this.description = description;
        this.display_name = display_name;
        this.posts = posts;
        this.following = following;
        this.followers = followers;
        this.username = username;
        this.website = website;
        this.profile_photo = profile_photo;
    }

    public UserAccountSetting() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    @Override
    public String toString() {
        return "UserAccountSetting{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", posts='" + posts + '\'' +
                ", following='" + following + '\'' +
                ", followers='" + followers + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                '}';
    }
}
