package com.povazhnuk;

/**
 * Class to handle object from csv file
 */
public class UserNavigation {
    private long ID;
    private String user;
    private String URL;
    private double numberOfSeconds;

    public long getID() {
        return ID;
    }

    public String getUser() {
        return user;
    }

    public String getURL() {
        return URL;
    }

    public double getNumberOfSeconds() {
        return numberOfSeconds;
    }

    public static Builder newBuilder() {
        return new UserNavigation().new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNavigation that = (UserNavigation) o;

        return (user != null ? !user.equals(that.user) : that.user != null) &&
                (URL != null ? URL.equals(that.URL) : that.URL == null);
    }

    @Override
    public int hashCode() {
        int result;
        result = user != null ? user.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (URL != null ? URL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return user + ',' + URL + ','+ numberOfSeconds;
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

        public Builder setNumberOfSeconds(double numberOfSeconds) {
            UserNavigation.this.numberOfSeconds = numberOfSeconds;
            return this;
        }

        public UserNavigation build() {
            return UserNavigation.this;
        }
    }
}
