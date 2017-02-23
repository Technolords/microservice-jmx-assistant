package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Host {
    private String username;
    private String password;
    private String host;

    @XmlAttribute (name = "username", required = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlAttribute (name = "password", required = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlValue
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
