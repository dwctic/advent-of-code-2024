package aoc.dcw;

import ch.qos.logback.classic.Level;

public class AoCDay {

    public void setLoggerLevel(Level l) {
        try {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) this.getClass().getField("logger").get(this);
            logger.setLevel(l);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {}
    }

}
