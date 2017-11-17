package com.povazhnuk;

public class UserNavigation {
    private long ID;
    private String user;
    private String URL;
    private int numberOfSeconds;

    public long getID() {
        return ID;
    }

    public String getUser() {
        return user;
    }

    public String getURL() {
        return URL;
    }

    public int getNumberOfSeconds() {
        return numberOfSeconds;
    }

    public static Builder newBuilder() {
        return new UserNavigation().new Builder();
    }

    @Override
    public String toString() {
        return "UserNavigation{" +
                "ID=" + ID +
                ", user='" + user + '\'' +
                ", URL='" + URL + '\'' +
                ", numberOfSeconds=" + numberOfSeconds +
                '}';
    }

    public class Builder {

        private Builder() {}

        public Builder setID(long ID) {
            UserNavigation.this.ID = ID;
            return this;
        }

        public Builder setUser(String user) {
            UserNavigation.this.user = user;
            return this;
        }

        public Builder setURL(String URL) {
            UserNavigation.this.URL = URL;
            return this;
        }

        public Builder setNumberOfSeconds(int numberOfSeconds) {
            UserNavigation.this.numberOfSeconds = numberOfSeconds;
            return this;
        }

        public UserNavigation build() {
            return UserNavigation.this;
        }
    }
}
