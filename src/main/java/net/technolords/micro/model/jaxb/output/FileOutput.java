package net.technolords.micro.model.jaxb.output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class FileOutput {
    private String file;
    private String mode;

    @XmlValue
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @XmlAttribute (name = "mode")
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
