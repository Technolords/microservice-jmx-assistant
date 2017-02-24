package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlElement;

import net.technolords.micro.model.jaxb.output.FileOutput;
import net.technolords.micro.model.jaxb.output.LogOutput;
import net.technolords.micro.model.jaxb.output.RedisOutput;

public class Output {
    private LogOutput logOutput;
    private FileOutput fileOutput;
    private RedisOutput redisOutput;

    @XmlElement (name = "log")
    public LogOutput getLogOutput() {
        return logOutput;
    }

    public void setLogOutput(LogOutput logOutput) {
        this.logOutput = logOutput;
    }

    @XmlElement (name = "file")
    public FileOutput getFileOutput() {
        return fileOutput;
    }

    public void setFileOutput(FileOutput fileOutput) {
        this.fileOutput = fileOutput;
    }

    @XmlElement (name = "redis")
    public RedisOutput getRedisOutput() {
        return redisOutput;
    }

    public void setRedisOutput(RedisOutput redisOutput) {
        this.redisOutput = redisOutput;
    }
}
